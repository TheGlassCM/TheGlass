package com.example.loginregister.models;

import android.net.Uri;

public class User {

    private String username;
    private String photo;
    private Integer rank;
    private Float points;

    public User(String username, String photo, Float points, Integer rank) {
        this.username = username;
        this.photo = photo;
        this.points = points;
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoto() {
        return photo;
    }

    public Float getPoints() {
        return points;
    }

    public Integer getRank() {
        return rank;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
