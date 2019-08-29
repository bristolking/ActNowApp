package com.actnow.android.sdk.responses;

import com.google.gson.annotations.SerializedName;

public  class ProjectCommentRecordsList {
    private String comment_id;
    private String project_id;
    private String task_id;
    private String comment;
    private String files;
    private String recordings;
    private String user_id;
    private String created_date;
    private String updated_date;
    private String user_name;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getRecordings() {
        return recordings;
    }

    public void setRecordings(String recordings) {
        this.recordings = recordings;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "ProjectCommentRecordsList{" +
                "comment_id='" + comment_id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", task_id='" + task_id + '\'' +
                ", comment='" + comment + '\'' +
                ", files='" + files + '\'' +
                ", recordings='" + recordings + '\'' +
                ", user_id='" + user_id + '\'' +
                ", created_date='" + created_date + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
