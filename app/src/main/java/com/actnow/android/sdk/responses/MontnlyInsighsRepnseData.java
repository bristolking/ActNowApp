package com.actnow.android.sdk.responses;

public class MontnlyInsighsRepnseData {
    private String day;
    private String  ctasks;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCtasks() {
        return ctasks;
    }

    public void setCtasks(String ctasks) {
        this.ctasks = ctasks;
    }

    @Override
    public String toString() {
        return "MontnlyInsighsRepnseData{" +
                "day='" + day + '\'' +
                ", ctasks='" + ctasks + '\'' +
                '}';
    }
}
