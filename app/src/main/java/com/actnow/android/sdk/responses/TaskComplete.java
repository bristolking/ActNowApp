package com.actnow.android.sdk.responses;

public class TaskComplete {
    private String success;
    private String message;

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
        return "TaskComplete{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
