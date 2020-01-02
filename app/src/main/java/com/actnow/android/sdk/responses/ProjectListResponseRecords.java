package com.actnow.android.sdk.responses;

public class ProjectListResponseRecords {

    public static final  String  TABLE_NAME = "actNowProject_db";
    public static String PROJECTID ="project_id";
    public static String NAME ="name";
    public static String PROJECTCODE ="project_code";
    public static String COLOR ="color";
    public static String PRIORITY ="priority";
    public static String DUEDATE ="due_date";
    public static String PROJECTMEMBERS ="project_members";
    public static String CREATEDBY ="created_by";
    public static String CREATEDDATE ="created_date";
    public static String UPDATEDBY ="updated_by";
    public static String UPDATEDDATE ="updated_date";
    public static String ORNGCODE ="orng_code";

    private  String project_id;
    private  String name;
    private  String project_code;
    private  String color;
    private  String priority;
    private  String due_date;
    private  String project_members;
    private  String created_by;
    private  String created_date;
    private  String updated_by;
    private  String updated_date;
    private  String orgn_code;

    public static final  String CREATE_TABLE = "CREATE TABLE actNowProject_db(project_id bigint(75) UNIQUE,name varchar(150), project_code varchar(150),color varchar(150),priority varchar(150), due_date varchar(150),project_members varchar(150),created_by varchar(150),created_date varchar(150),updated_by varchar(150), updated_date varchar(150), orgn_code varchar(150))";



    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getProject_members() {
        return project_members;
    }

    public void setProject_members(String project_members) {
        this.project_members = project_members;
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

    @Override
    public String toString() {
        return "ProjectListResponseRecords{" +
                "project_id='" + project_id + '\'' +
                ", name='" + name + '\'' +
                ", project_code='" + project_code + '\'' +
                ", colorWhite='" + color + '\'' +
                ", priority='" + priority + '\'' +
                ", due_date='" + due_date + '\'' +
                ", project_members='" + project_members + '\'' +
                ", created_by='" + created_by + '\'' +
                ", created_date='" + created_date + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", orgn_code='" + orgn_code + '\'' +
                '}';
    }


}
