package com.example.loginregister.models;

public class Bar {

    private String name;
    private String type;
    private String location;
    private String schedule;
    private Integer dto,dto_points,points;
    private Float latitude, longitude;


    public Bar(String name,String type,String location,String schedule, Integer dto, Integer dto_points, Integer points, Float latitude, Float longitude) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.schedule = schedule;
        this.dto = dto;
        this.dto_points = dto_points;
        this.points = points;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", schedule='" + schedule + '\'' +
                ", dto=" + dto +
                ", dto_points=" + dto_points +
                ", points=" + points +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
