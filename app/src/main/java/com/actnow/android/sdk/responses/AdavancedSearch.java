package com.actnow.android.sdk.responses;

import java.util.List;

public class AdavancedSearch {
    private String success;
    private String message;
    private String no_of_records;
    private List<AdavancedTaskRecords> task_records;

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

    public String getNo_of_records() {
        return no_of_records;
    }

    public void setNo_of_records(String no_of_records) {
        this.no_of_records = no_of_records;
    }

    public List<AdavancedTaskRecords> getTask_records() {
        return task_records;
    }

    public void setTask_records(List<AdavancedTaskRecords> task_records) {
        this.task_records = task_records;
    }

    @Override
    public String toString() {
        return "AdavancedSearch{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", task_records=" + task_records +
                '}';
    }
}
