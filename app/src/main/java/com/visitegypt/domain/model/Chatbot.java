package com.visitegypt.domain.model;

public class Chatbot {
    private String message;
    private String sender;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Chatbot(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }
}
