package com.actnow.android.sdk.responses;

public class SignUpResponse {
    private String success;
    private String message;
    private String id;
    private String name;
    private String email;
    private String mobile_number;
    private String provider_id;
    private String provider_name;
    private String orgn_code;
    private String db_name;
    private String user_type;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getOrgn_code() {
        return orgn_code;
    }

    public void setOrgn_code(String orgn_code) {
        this.orgn_code = orgn_code;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "SignUpResponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile_number='" + mobile_number + '\'' +
                ", provider_id='" + provider_id + '\'' +
                ", provider_name='" + provider_name + '\'' +
                ", orgn_code='" + orgn_code + '\'' +
                ", db_name='" + db_name + '\'' +
                ", user_type='" + user_type + '\'' +
                '}';
    }
}
