package com.actnow.android.offline;

public class TaskOffline {

    private String task_id;
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
    private String remindars_count;
    private String project_name;
    private String repeat_type;
    private String repeat_months;
    private String repeat_weeks;
    private String repeat_days;
    private String parent_task_code;
    private String orng_code;

    public TaskOffline(String task_id, String name, String task_code, String project_code, String priority, String due_date, String task_members, String status, String approval_status, String created_by, String created_date, String updated_by, String updated_date, String remindars_count, String project_name, String repeat_type, String repeat_months, String repeat_weeks, String repeat_days, String parent_task_code, String orng_code) {
        this.task_id = task_id;
        this.name = name;
        this.task_code = task_code;
        this.project_code = project_code;
        this.priority = priority;
        this.due_date = due_date;
        this.task_members = task_members;
        this.status = status;
        this.approval_status = approval_status;
        this.created_by = created_by;
        this.created_date = created_date;
        this.updated_by = updated_by;
        this.updated_date = updated_date;
        this.remindars_count = remindars_count;
        this.project_name = project_name;
        this.repeat_type = repeat_type;
        this.repeat_months = repeat_months;
        this.repeat_weeks = repeat_weeks;
        this.repeat_days = repeat_days;
        this.parent_task_code = parent_task_code;
        this.orng_code = orng_code;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
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

    public String getRemindars_count() {
        return remindars_count;
    }

    public void setRemindars_count(String remindars_count) {
        this.remindars_count = remindars_count;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
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

    public String getRepeat_weeks() {
        return repeat_weeks;
    }

    public void setRepeat_weeks(String repeat_weeks) {
        this.repeat_weeks = repeat_weeks;
    }

    public String getRepeat_days() {
        return repeat_days;
    }

    public void setRepeat_days(String repeat_days) {
        this.repeat_days = repeat_days;
    }

    public String getParent_task_code() {
        return parent_task_code;
    }

    public void setParent_task_code(String parent_task_code) {
        this.parent_task_code = parent_task_code;
    }

    public String getOrng_code() {
        return orng_code;
    }

    public void setOrng_code(String orng_code) {
        this.orng_code = orng_code;
    }

    public TaskOffline() {

    }

    @Override
    public String toString() {
        return "TaskOffline{" +
                "task_id='" + task_id + '\'' +
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
                ", remindars_count='" + remindars_count + '\'' +
                ", project_name='" + project_name + '\'' +
                ", repeat_type='" + repeat_type + '\'' +
                ", repeat_months='" + repeat_months + '\'' +
                ", repeat_weeks='" + repeat_weeks + '\'' +
                ", repeat_days='" + repeat_days + '\'' +
                ", parent_task_code='" + parent_task_code + '\'' +
                ", orng_code='" + orng_code + '\'' +
                '}';
    }
}