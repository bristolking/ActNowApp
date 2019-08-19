package com.actnow.android.sdk.responses;

public class Task {
    private String id;
    private String name;
    private String task_code;
    private String project_code;
    private String priority;
    private String due_date;
    private String task_members;
    private String status;
    private String approval_status;
    private String created_by;
    private String created_date;
    private String updated_by;
    private String updated_date;
    private String orgn_code;
    private String repeat_type;
    private String repeat_months;
    private String repeat_week;
    private String repeat_days;
    private String parenrt_task_code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask_code() {
        return task_code;
    }

    public void setTask_code(String task_code) {
        this.task_code = task_code;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getTask_members() {
        return task_members;
    }

    public void setTask_members(String task_members) {
        this.task_members = task_members;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getOrgn_code() {
        return orgn_code;
    }

    public void setOrgn_code(String orgn_code) {
        this.orgn_code = orgn_code;
    }

    public String getRepeat_type() {
        return repeat_type;
    }

    public void setRepeat_type(String repeat_type) {
        this.repeat_type = repeat_type;
    }

    public String getRepeat_months() {
        return repeat_months;
    }

    public void setRepeat_months(String repeat_months) {
        this.repeat_months = repeat_months;
    }

    public String getRepeat_week() {
        return repeat_week;
    }

    public void setRepeat_week(String repeat_week) {
        this.repeat_week = repeat_week;
    }

    public String getRepeat_days() {
        return repeat_days;
    }

    public void setRepeat_days(String repeat_days) {
        this.repeat_days = repeat_days;
    }

    public String getParenrt_task_code() {
        return parenrt_task_code;
    }

    public void setParenrt_task_code(String parenrt_task_code) {
        this.parenrt_task_code = parenrt_task_code;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", task_code='" + task_code + '\'' +
                ", project_code='" + project_code + '\'' +
                ", priority='" + priority + '\'' +
                ", due_date='" + due_date + '\'' +
                ", task_members='" + task_members + '\'' +
                ", status='" + status + '\'' +
                ", approval_status='" + approval_status + '\'' +
                ", created_by='" + created_by + '\'' +
                ", created_date='" + created_date + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", orgn_code='" + orgn_code + '\'' +
                ", repeat_type='" + repeat_type + '\'' +
                ", repeat_months='" + repeat_months + '\'' +
                ", repeat_week='" + repeat_week + '\'' +
                ", repeat_days='" + repeat_days + '\'' +
                ", parenrt_task_code='" + parenrt_task_code + '\'' +
                '}';
    }
}
