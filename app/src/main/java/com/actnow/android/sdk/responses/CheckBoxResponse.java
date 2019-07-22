package com.actnow.android.sdk.responses;

import java.util.List;

public class CheckBoxResponse {
    private String success;
    private String message;
    private String no_of_records;
    private List<OrgnUserRecordsCheckBox> orgn_users_records;

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

    public String getNo_of_records() {
        return no_of_records;
    }

    public void setNo_of_records(String no_of_records) {
        this.no_of_records = no_of_records;
    }

    public List<OrgnUserRecordsCheckBox> getOrgn_users_records() {
        return orgn_users_records;
    }

    public void setOrgn_users_records(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        this.orgn_users_records = orgn_users_records;
    }

    @Override
    public String toString() {
        return "CheckBoxResponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", no_of_records='" + no_of_records + '\'' +
                ", orgn_users_records=" + orgn_users_records +
                '}';
    }
}
