package com.example.loginregister.models;

public class Offer {

    public String userName;
    public String offer;

    public Offer(String userName, String offer) {
        this.userName = userName;
        this.offer = offer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }
}
