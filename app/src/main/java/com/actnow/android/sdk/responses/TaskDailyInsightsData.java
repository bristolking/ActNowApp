package com.actnow.android.sdk.responses;

public class TaskDailyInsightsData {
    private String time;
    private String ctasks;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCtasks() {
        return ctasks;
    }

    public void setCtasks(String ctasks) {
        this.ctasks = ctasks;
    }

    @Override
    public String toString() {
        return "TaskDailyInsightsData{" +
                "time='" + time + '\'' +
                ", ctasks='" + ctasks + '\'' +
                '}';
    }
}
