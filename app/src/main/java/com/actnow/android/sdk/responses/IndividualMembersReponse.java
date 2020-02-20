package com.actnow.android.sdk.responses;

public class IndividualMembersReponse {
    private String id;
    private String name;
    private String email;
    private String mobile_number;
    private String provider_id;
    private String provider_name;
    private String orgn_code;
    private String password;
    private String image_path;
    private String user_type;
    private String otp;
    private String status;
    private String email_verified_at;
    private String verified;
    private String remember_token;
    private String refresh_token;
    private String created_at;
    private String updated_at ;
    private String other_orgns;
    private String timezone;
    private String completed ;
    private String approval;
    private String ongoing;
    private String pending;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getOther_orgns() {
        return other_orgns;
    }

    public void setOther_orgns(String other_orgns) {
        this.other_orgns = other_orgns;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getOngoing() {
        return ongoing;
    }

    public void setOngoing(String ongoing) {
        this.ongoing = ongoing;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    @Override
    public String toString() {
        return "IndividualMembersReponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile_number='" + mobile_number + '\'' +
                ", provider_id='" + provider_id + '\'' +
                ", provider_name='" + provider_name + '\'' +
                ", orgn_code='" + orgn_code + '\'' +
                ", password='" + password + '\'' +
                ", image_path='" + image_path + '\'' +
                ", user_type='" + user_type + '\'' +
                ", otp='" + otp + '\'' +
                ", status='" + status + '\'' +
                ", email_verified_at='" + email_verified_at + '\'' +
                ", verified='" + verified + '\'' +
                ", remember_token='" + remember_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", other_orgns='" + other_orgns + '\'' +
                ", timezone='" + timezone + '\'' +
                ", completed='" + completed + '\'' +
                ", approval='" + approval + '\'' +
                ", ongoing='" + ongoing + '\'' +
                ", pending='" + pending + '\'' +
                '}';
    }
}
