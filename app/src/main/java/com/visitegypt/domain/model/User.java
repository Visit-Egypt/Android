package com.visitegypt.domain.model;

public class User {
    // TODO define the rest of the variables
    private String name;
    private String password;

    public User() {
    }

    // TODO finish constructor
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // TODO make the rest of the setters and getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
