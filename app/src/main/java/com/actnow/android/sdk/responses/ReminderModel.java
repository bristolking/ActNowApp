package com.actnow.android.sdk.responses;

public class ReminderModel {
    public String date;
    public String time;
    public String name;

    public ReminderModel(String date) {
        this.date= date;


    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ReminderModel{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
