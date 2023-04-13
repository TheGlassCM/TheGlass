package com.example.loginregister.models;

public class Transaction {

    public String barId,userId;

    public String timeStamp;

    public Transaction(String barId, String userId, String timeStamp) {
        this.barId = barId;
        this.userId = userId;
        this.timeStamp = timeStamp;
    }

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
