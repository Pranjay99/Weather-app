package com.example.weatherapp;

public class weatherRVmodel {
    private String time;
    private String temp;
    private String icon;
    private String speed;

    public weatherRVmodel(String time, String temp, String icon, String speed) {

        this.time = time;
        this.temp = temp;
        this.icon = icon;
        this.speed = speed;
    }

    public String getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
    }

    public String getIcon() {
        return icon;
    }

    public String getSpeed() {
        return speed;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
