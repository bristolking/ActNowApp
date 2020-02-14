package com.actnow.android.sdk.responses;

import java.util.List;

public class ProjectInsightsReponse {
    private  String  success;
    private  String  message;
    private  String  no_of_records;

    private List<ProjectInsightsRecords> projectInsightsRecords;

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

    public List<ProjectInsightsRecords> getProjectInsightsRecords() {
        return projectInsightsRecords;
    }

    public void setProjectInsightsRecords(List<ProjectInsightsRecords> projectInsightsRecords) {
        this.projectInsightsRecords = projectInsightsRecords;
    }

    @Override
    public String toString() {
        return "ProjectInsightsReponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", projectInsightsRecords=" + projectInsightsRecords +
                '}';
    }
}
