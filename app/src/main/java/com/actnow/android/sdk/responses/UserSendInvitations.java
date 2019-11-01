package com.actnow.android.sdk.responses;

public class UserSendInvitations {
    private String success;
    private String message;

    public UserSendInvitations(String success, String message) {
        this.success = success;
        this.message = message;
    }

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

    @Override
    public String toString() {
        return "UserSendInvitations{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
