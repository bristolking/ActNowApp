package com.actnow.android.sdk.responses;

import java.util.List;

public class TaskListResponse {

    private  String  success;
    private  String  message;
    private  String  no_of_records;
    private List<TaskListRecords> task_records;

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

    public List<TaskListRecords> getTask_records() {
        return task_records;
    }

    public void setTask_records(List<TaskListRecords> task_records) {
        this.task_records = task_records;
    }

    @Override
    public String toString() {
        return "TaskListResponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", task_records=" + task_records +
                '}';
    }
}
