package com.actnow.android.sdk.responses;

import java.util.List;

public class TaskInsightYearly {
    private String success;
    private String  message;
    private String no_of_tasks;
    private String no_of_ctasks;
    private List<TaskInsightDataYear> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNo_of_tasks() {
        return no_of_tasks;
    }

    public void setNo_of_tasks(String no_of_tasks) {
        this.no_of_tasks = no_of_tasks;
    }

    public String getNo_of_ctasks() {
        return no_of_ctasks;
    }

    public void setNo_of_ctasks(String no_of_ctasks) {
        this.no_of_ctasks = no_of_ctasks;
    }

    public List<TaskInsightDataYear> getData() {
        return data;
    }

    public void setData(List<TaskInsightDataYear> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TaskInsightYearly{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_tasks='" + no_of_tasks + '\'' +
                ", no_of_ctasks='" + no_of_ctasks + '\'' +
                ", data=" + data +
                '}';
    }
}
