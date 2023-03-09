package com.example.loginregister;

public class Bar {

    String name;

    Integer dto,dto_points,points;

    Float latitude,logitude;


    public Bar(String name, Integer dto, Integer dto_points, Integer points, Float latitude, Float logitude) {
        this.name = name;
        this.dto = dto;
        this.dto_points = dto_points;
        this.points = points;
        this.latitude = latitude;
        this.logitude = logitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDto() {
        return dto;
    }

    public void setDto(Integer dto) {
        this.dto = dto;
    }

    public Integer getDto_points() {
        return dto_points;
    }

    public void setDto_points(Integer dto_points) {
        this.dto_points = dto_points;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLogitude() {
        return logitude;
    }

    public void setLogitude(Float logitude) {
        this.logitude = logitude;
    }
}
