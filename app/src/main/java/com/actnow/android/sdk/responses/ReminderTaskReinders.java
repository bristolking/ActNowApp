package com.actnow.android.sdk.responses;

 public  class ReminderTaskReinders {
     private String reminder_task_id;
     private String task_code;
     private String reminder_date;
     private String user_id;
     private String status;
     private String created_date;
     private String remind_to;
     private String user_name;

     public String getReminder_task_id() {
         return reminder_task_id;
     }

     public void setReminder_task_id(String reminder_task_id) {
         this.reminder_task_id = reminder_task_id;
     }

     public String getTask_code() {
         return task_code;
     }

     public void setTask_code(String task_code) {
         this.task_code = task_code;
     }

     public String getReminder_date() {
         return reminder_date;
     }

     public void setReminder_date(String reminder_date) {
         this.reminder_date = reminder_date;
     }

     public String getUser_id() {
         return user_id;
     }

     public void setUser_id(String user_id) {
         this.user_id = user_id;
     }

     public String getStatus() {
         return status;
     }

     public void setStatus(String status) {
         this.status = status;
     }

     public String getCreated_date() {
         return created_date;
     }

     public void setCreated_date(String created_date) {
         this.created_date = created_date;
     }

     public String getRemind_to() {
         return remind_to;
     }

     public void setRemind_to(String remind_to) {
         this.remind_to = remind_to;
     }

     public String getUser_name() {
         return user_name;
     }

     public void setUser_name(String user_name) {
         this.user_name = user_name;
     }

     @Override
     public String toString() {
         return "ReminderTaskReinders{" +
                 "reminder_task_id='" + reminder_task_id + '\'' +
                 ", task_code='" + task_code + '\'' +
                 ", reminder_date='" + reminder_date + '\'' +
                 ", user_id='" + user_id + '\'' +
                 ", status='" + status + '\'' +
                 ", created_date='" + created_date + '\'' +
                 ", remind_to='" + remind_to + '\'' +
                 ", user_name='" + user_name + '\'' +
                 '}';
     }
 }
