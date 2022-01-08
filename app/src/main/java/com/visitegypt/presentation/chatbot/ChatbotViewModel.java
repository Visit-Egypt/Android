package com.visitegypt.presentation.chatbot;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.usecase.ChatbotUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatbotViewModel extends ViewModel {
    MutableLiveData botResponseMutableLiveData = new MutableLiveData<String>();
    private static final String TAG = "Chatbot View Model";
    private final ChatbotUseCase chatbotUseCase;

    @Inject
    public ChatbotViewModel(ChatbotUseCase chatbotUseCase) {
        this.chatbotUseCase = chatbotUseCase;
    }

    public void setMessage(String msg) {
        Log.d(TAG, "Chatbot View Model b set message:" + msg);
        chatbotUseCase.setMessage(msg);
        Log.d(TAG, "Chatbot View Model set message:" + msg);
        chatbotUseCase.execute(
                chatbotResponse -> {
                    Log.d(TAG, "waittttttttt" + chatbotResponse.getResponse());
                    botResponseMutableLiveData.setValue(chatbotResponse.getResponse());
                }, throwable -> Log.e(TAG, "chatbot error: " + throwable.getMessage())
        );
    }
}