package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("message")
    private String message;

    @SerializedName("response")
    private String response;
    @SerializedName("reco")
    private String reco;

    public Message() {
    }

    public String getResponse() {
        return response;
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
    }

}
