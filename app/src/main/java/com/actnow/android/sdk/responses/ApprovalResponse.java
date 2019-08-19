package com.actnow.android.sdk.responses;

public class ApprovalResponse {
    private  String name;
    private  String date;
    private  String priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ApprovalResponse(String name, String date, String priority){
        this.name = name;
        this.date = date;
        this.priority = priority;

    }
    @Override
    public String toString() {
        return "ApprovalResponse{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
