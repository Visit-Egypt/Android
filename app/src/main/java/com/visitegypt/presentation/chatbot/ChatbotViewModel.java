package com.visitegypt.presentation.chatbot;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Message;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.XPUpdate;
import com.visitegypt.domain.usecase.ChatbotUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.GetWeatherByCity;
import com.visitegypt.domain.usecase.UpdateChatBotPlacePlaceActivity;
import com.visitegypt.domain.usecase.UpdateUserChatbotArtifactUseCase;
import com.visitegypt.utils.Constants;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatbotViewModel extends ViewModel {

    SingleLiveEvent botResponseMutableLiveData = new SingleLiveEvent<String>();

    MutableLiveData<XPUpdate> chatBotPlaceMutableLiveData = new MutableLiveData<>();
    MutableLiveData<XPUpdate> chatBotArtifactsMutableLiveData = new MutableLiveData<>();
    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    private static final String TAG = "Chatbot View Model";

    private UpdateUserChatbotArtifactUseCase updateUserChatbotArtifactUseCase;
    private UpdateChatBotPlacePlaceActivity updateChatBotPlacePlaceActivity;
    private GetUserUseCase getUserUseCase;
    private GetWeatherByCity getWeatherByCity;

    private ChatbotUseCase chatbotUseCase;
    private Message message = new Message();

    private String placeId;

    private SharedPreferences sharedPreferences;

    @Inject
    public ChatbotViewModel(ChatbotUseCase chatbotUseCase,
                            UpdateChatBotPlacePlaceActivity updateChatBotPlacePlaceActivity,
                            UpdateUserChatbotArtifactUseCase updateUserChatbotArtifactUseCase,
                            GetUserUseCase getUserUseCase,
                            GetWeatherByCity getWeatherByCity,
                            SharedPreferences sharedPreferences) {
        this.chatbotUseCase = chatbotUseCase;
        this.updateChatBotPlacePlaceActivity = updateChatBotPlacePlaceActivity;
        this.updateUserChatbotArtifactUseCase = updateUserChatbotArtifactUseCase;
        this.getUserUseCase = getUserUseCase;
        this.sharedPreferences = sharedPreferences;
        this.getWeatherByCity = getWeatherByCity;
    }

    public void setMessage(String msg) {
        message.setMsg(msg);
        chatbotUseCase.setMessage(message);
        chatbotUseCase.execute(
                chatbotResponse -> {
                    if (chatbotResponse.getResponse().matches("Weather\\s[a-zA-Z]+")) {
                        String[] split = chatbotResponse.getResponse().split("\\s");
                        String city = split[1];
                        getWeatherByCity.setCity(city);
                        getWeatherByCity.execute(
                                weatherModel -> {
                                    String weather = weatherModel.getWeather().get(0).getMain();
                                    botResponseMutableLiveData.setValue("The weather in " + city + " is" + weather+" and temperature is" );
                                }
                        , throwable -> {
                            Log.e(TAG, "getWeatherByCity: ", throwable);
                        });
//                        getWeather(city);
                    }else if (chatbotResponse.getResponse().equals("Weather ")) {
                        Log.d(TAG, "setMessage:weather");
                    }
                    else{
                        botResponseMutableLiveData.setValue(chatbotResponse.getResponse());
                    }


                }, throwable -> Log.e(TAG, "chatbot error: " + throwable.getMessage())
        );
    }

    public void updateChatBotArtifact() {
        if (placeId == null)
            Log.e(TAG, "updateChatBotArtifact: you must call setPlaceId()");
        updateUserChatbotArtifactUseCase.setPlaceId(placeId);
        updateUserChatbotArtifactUseCase.execute(xpUpdate -> {
            chatBotArtifactsMutableLiveData.setValue(xpUpdate);
        }, throwable -> {
            Log.e(TAG, "updateChatBotArtifact: " + throwable.getMessage());
            chatBotArtifactsMutableLiveData.setValue(null);

        });
    }

    public void updateChatBotPlace() {
        if (placeId == null)
            Log.e(TAG, "updateChatBotPlace: you must call setPlaceId()");
        updateChatBotPlacePlaceActivity.setPlaceId(placeId);
        updateChatBotPlacePlaceActivity.execute(xpUpdate -> {
            chatBotPlaceMutableLiveData.setValue(xpUpdate);
        }, throwable -> {
            Log.e(TAG, "updateChatBotPlace: " + throwable.getMessage());
            chatBotPlaceMutableLiveData.setValue(null);
        });
    }

    public void getUser() {
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        String email = sharedPreferences.getString(Constants.SHARED_PREF_EMAIL, "");
        getUserUseCase.setUser(userId, email);
        getUserUseCase.execute(user -> {
            userMutableLiveData.setValue(user);
        }, throwable -> {
            userMutableLiveData.setValue(null);
            Log.e(TAG, "getUser: ", throwable);
        });
    }


    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}