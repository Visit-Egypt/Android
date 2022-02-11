package com.visitegypt.domain.usecase;
import android.util.Log;

import com.visitegypt.domain.model.Message;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.repository.ChatbotRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ChatbotUseCase extends SingleUseCase<Message> {
    private static final String TAG = "Chatbot usecase";

    private String message;
    ChatbotRepository chatbotRepository;

    public void setMessage(String message) {
        Log.d(TAG, "Chatbot usecase messsageeee:  " + message);
        this.message = message;
    }
    private Message msg=new Message();
    @Inject
    public ChatbotUseCase(ChatbotRepository chatbotRepository) {
        this.chatbotRepository = chatbotRepository;
    }

    @Override
    protected Single<Message> buildSingleUseCase() {
        return chatbotRepository.chatbotReceiveRequest(msg);
    }
}