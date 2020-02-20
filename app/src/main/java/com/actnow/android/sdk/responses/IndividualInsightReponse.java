package com.actnow.android.sdk.responses;

import java.util.List;

public class IndividualInsightReponse {
    private String success;
    private String message;
    private String no_of_tasks;
    private List<IndividualMembersReponse> members;

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

    public String getNo_of_tasks() {
        return no_of_tasks;
    }

    public void setNo_of_tasks(String no_of_tasks) {
        this.no_of_tasks = no_of_tasks;
    }

    public List<IndividualMembersReponse> getMembers() {
        return members;
    }

    public void setMembers(List<IndividualMembersReponse> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "IndividualInsightReponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_tasks='" + no_of_tasks + '\'' +
                ", members=" + members +
                '}';
    }
}
