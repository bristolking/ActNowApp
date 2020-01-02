package com.actnow.android.offline;

public class ProjectListOffline {
    private String projectname;
    private String prjectcode;


    public ProjectListOffline(String projectname, String prjectcode) {
        this.projectname = projectname;
        this.prjectcode = prjectcode;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getPrjectcode() {
        return prjectcode;
    }

    public void setPrjectcode(String prjectcode) {
        this.prjectcode = prjectcode;
    }

    @Override
    public String toString() {
        return "ProjectListOffline{" +
                "projectname='" + projectname + '\'' +
                ", prjectcode='" + prjectcode + '\'' +
                '}';
    }



}
