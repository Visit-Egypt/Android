package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Message;

import io.reactivex.rxjava3.core.Single;

public interface ChatbotRepository {

    Single <Message> chatbotReceiveRequest (Message message);
}
