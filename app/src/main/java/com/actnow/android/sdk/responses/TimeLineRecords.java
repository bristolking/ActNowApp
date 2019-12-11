package com.actnow.android.sdk.responses;

public  class TimeLineRecords {
    private String timeline_id;
    private String action;
    private String action_code;
    private String project_code;
    private String created_at;
    private String  created_by;

    public String getTimeline_id() {
        return timeline_id;
    }

    public void setTimeline_id(String timeline_id) {
        this.timeline_id = timeline_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction_code() {
        return action_code;
    }

    public void setAction_code(String action_code) {
        this.action_code = action_code;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Override
    public String toString() {
        return "TimeLineRecords{" +
                "timeline_id='" + timeline_id + '\'' +
                ", action='" + action + '\'' +
                ", action_code='" + action_code + '\'' +
                ", project_code='" + project_code + '\'' +
                ", created_at='" + created_at + '\'' +
                ", created_by='" + created_by + '\'' +
                '}';
    }
}
