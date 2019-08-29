package com.actnow.android.sdk.responses;

import java.util.List;

public class ProjectCommentListResponse {
    private String success;
    private String message;
    private String no_of_records;
    private List<ProjectCommentRecordsList>comment_records;

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

    public List<ProjectCommentRecordsList> getComment_records() {
        return comment_records;
    }

    public void setComment_records(List<ProjectCommentRecordsList> comment_records) {
        this.comment_records = comment_records;
    }

    @Override
    public String toString() {
        return "ProjectCommentListResponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", comment_records=" + comment_records +
                '}';
    }
}
