package com.actnow.android.sdk.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeLineTaskList {
    private String success;
    private String message;
    private String no_of_records;
    private List<TimeLineRecordsTaskList> timeline_records;

    @SerializedName("success")
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    @SerializedName("message")

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @SerializedName("no_of_records")
    public String getNo_of_records() {
        return no_of_records;
    }

    public void setNo_of_records(String no_of_records) {
        this.no_of_records = no_of_records;
    }
    public List<TimeLineRecordsTaskList> getTimeline_records() {
        return timeline_records;
    }

    public void setTimeline_records(List<TimeLineRecordsTaskList> timeline_records) {
        this.timeline_records = timeline_records;
    }

    @Override
    public String toString() {
        return "TimeLineTaskList{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", timeline_records=" + timeline_records +
                '}';
    }
}
