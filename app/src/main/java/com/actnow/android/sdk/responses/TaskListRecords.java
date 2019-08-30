package com.actnow.android.sdk.responses;

 public class TaskListRecords {
     private String task_id;
     private String task_code;
     private String name;
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

     public String getTask_id() {
         return task_id;
     }

     public void setTask_id(String task_id) {
         this.task_id = task_id;
     }

     public String getTask_code() {
         return task_code;
     }

     public void setTask_code(String task_code) {
         this.task_code = task_code;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
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

     @Override
     public String toString() {
         return "TaskListRecords{" +
                 "task_id='" + task_id + '\'' +
                 ", task_code='" + task_code + '\'' +
                 ", name='" + name + '\'' +
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
                 '}';
     }
 }