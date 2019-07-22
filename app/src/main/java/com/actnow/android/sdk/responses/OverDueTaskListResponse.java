package com.actnow.android.sdk.responses;

import java.util.List;

public class OverDueTaskListResponse {
    private String success;
    private String message;
    private List<OverDueTaskRecords> overdue_tasks;

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

    public List<OverDueTaskRecords> getOverdue_tasks() {
        return overdue_tasks;
    }

    public void setOverdue_tasks(List<OverDueTaskRecords> overdue_tasks) {
        this.overdue_tasks = overdue_tasks;
    }

    @Override
    public String toString() {
        return "OverDueTaskListResponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", overdue_tasks=" + overdue_tasks +
                '}';
    }
}
