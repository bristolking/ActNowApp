package com.actnow.android.sdk.responses;

import java.util.List;

public class ReminderResponse {
    private String success;
    private String message;
    private String no_of_records;
    private List<ReminderTaskReinders> task_reminders ;

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

    public List<ReminderTaskReinders> getTask_reminders() {
        return task_reminders;
    }

    public void setTask_reminders(List<ReminderTaskReinders> task_reminders) {
        this.task_reminders = task_reminders;
    }

    @Override
    public String toString() {
        return "ReminderResponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", task_reminders=" + task_reminders +
                '}';
    }
}