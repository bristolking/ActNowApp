package com.actnow.android.sdk.responses;

public class TaskDelete {

    private String  success;
    private String  message;

    public TaskDelete(String success, String message) {
        this.success = success;
        this.message = message;
    }

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

    @Override
    public String toString() {
        return "TaskDelete{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
