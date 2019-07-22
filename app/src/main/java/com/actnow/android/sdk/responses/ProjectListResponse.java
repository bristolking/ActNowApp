package com.actnow.android.sdk.responses;

import java.util.List;

public class ProjectListResponse {
    private String success;
    private String message;
    private String no_of_records;
    private List<ProjectListResponseRecords> project_records;

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

    public List<ProjectListResponseRecords> getProject_records() {
        return project_records;
    }

    public void setProject_records(List<ProjectListResponseRecords> project_records) {
        this.project_records = project_records;
    }

    @Override
    public String toString() {
        return "ProjectListResponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", project_records=" + project_records +
                '}';
    }
}
