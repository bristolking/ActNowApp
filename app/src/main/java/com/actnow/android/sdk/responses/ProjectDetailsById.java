package com.actnow.android.sdk.responses;

import java.util.List;

public class ProjectDetailsById {
    private String success;
    private String message;
    private String no_of_records;
    private List<ProjectTaskRecordsById> project_task_records;

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

    public List<ProjectTaskRecordsById> getProject_task_records() {
        return project_task_records;
    }

    public void setProject_task_records(List<ProjectTaskRecordsById> project_task_records) {
        this.project_task_records = project_task_records;
    }

    @Override
    public String toString() {
        return "ProjectDetailsById{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", project_task_records=" + project_task_records +
                '}';
    }
}
