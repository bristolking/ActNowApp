package com.actnow.android.sdk.responses;

public class TaskWeeklyInsightsData {
    private String day;
    private String day_name;
    private String ctasks;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay_name() {
        return day_name;
    }

    public void setDay_name(String day_name) {
        this.day_name = day_name;
    }

    public String getCtasks() {
        return ctasks;
    }

    public void setCtasks(String ctasks) {
        this.ctasks = ctasks;
    }

    @Override
    public String toString() {
        return "TaskWeeklyInsightsData{" +
                "day='" + day + '\'' +
                ", day_name='" + day_name + '\'' +
                ", ctasks='" + ctasks + '\'' +
                '}';
    }
}
