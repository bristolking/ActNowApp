package com.actnow.android.sdk.responses;

import java.util.List;

public class PriortyTaskListResponse {
    private String success;
    private String message;
    private String no_of_records;
    private List<PriorityTaskListRecords> task_records;

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

    public List<PriorityTaskListRecords> getTask_records() {
        return task_records;
    }

    public void setTask_records(List<PriorityTaskListRecords> task_records) {
        this.task_records = task_records;
    }

    @Override
    public String toString() {
        return "PriortyTaskListResponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", task_records=" + task_records +
                '}';
    }
}
