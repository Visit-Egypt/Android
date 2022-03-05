package com.visitegypt.presentation.chatbot;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Message;
import com.visitegypt.domain.usecase.ChatbotUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatbotViewModel extends ViewModel {
    SingleLiveEvent botResponseMutableLiveData = new SingleLiveEvent<String>();
    private static final String TAG = "Chatbot View Model";
    private ChatbotUseCase chatbotUseCase;
    private Message message = new Message();
    @Inject
    public ChatbotViewModel(ChatbotUseCase chatbotUseCase) {
        this.chatbotUseCase = chatbotUseCase;
    }

    public void setMessage(String msg) {
        message.setMsg(msg);
        chatbotUseCase.setMessage(message);
        chatbotUseCase.execute(
                chatbotResponse -> {
                    botResponseMutableLiveData.setValue(chatbotResponse.getResponse());
                }
                , throwable -> Log.e(TAG, "chatbot error: " + throwable.getMessage())
        );

    }


}