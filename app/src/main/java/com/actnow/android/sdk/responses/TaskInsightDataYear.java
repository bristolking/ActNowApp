package com.actnow.android.sdk.responses;

public class TaskInsightDataYear {
    private String month;
    private String month_name;
    private String ctasks;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth_name(String month_name) {
        return this.month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public String getCtasks() {
        return ctasks;
    }

    public void setCtasks(String ctasks) {
        this.ctasks = ctasks;
    }

    @Override
    public String toString() {
        return "TaskInsightDataYear{" +
                "month='" + month + '\'' +
                ", month_name='" + month_name + '\'' +
                ", ctasks='" + ctasks + '\'' +
                '}';
    }
}
