package com.visitegypt.data.repository;

import android.util.Log;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Message;
import com.visitegypt.domain.repository.ChatbotRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ChatbotRepositoryImp implements ChatbotRepository {
    private static final String TAG = "Chatbot Repository";
    private RetrofitService retrofitService;
    @Inject
    public ChatbotRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;

    }

    @Override
    public Single<Message> chatbotReceiveRequest(Message message) {
        Log.d(TAG,"Chatbot Repository msg:"+message);
        return retrofitService.chatbotReceiveRequest(message);
    }
}
