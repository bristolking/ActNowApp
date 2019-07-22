package com.actnow.android.sdk.api;

import com.actnow.android.sdk.responses.CheckOtpReponse;
import com.actnow.android.sdk.responses.OverDueTaskListResponse;
import com.actnow.android.sdk.responses.PriortyTaskListResponse;
import com.actnow.android.sdk.responses.ProjectAddResponse;
import com.actnow.android.sdk.responses.ProjectDetailsById;
import com.actnow.android.sdk.responses.ProjectEditResponse;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.SendOtpResponse;
import com.actnow.android.sdk.responses.SignInResponse;
import com.actnow.android.sdk.responses.SignUpResponse;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.TaskAddResponse;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.sdk.responses.UpdateProfileResponses;
import com.actnow.android.sdk.responses.UserDetailsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ANApi {
    @FormUrlEncoded
    @POST("app/login")
    Call<SignInResponse> userSignIn(@Field(Parameters.MOBILENO) String mail, @Field(Parameters.PASSWORD) String pass);

    @FormUrlEncoded
    @POST("app/signup")
    Call<SignUpResponse> userSignUp(@Field(Parameters.NAME) String name, @Field(Parameters.EMAIL) String mail, @Field(Parameters.MOBILENO) String mobileNumber, @Field(Parameters.ORGANIZATIONNAME) String organizationName, @Field(Parameters.PASSWORD) String password);

    @GET("app/send_otp/{mobile_number}")
    Call<SendOtpResponse> sendOtp(@Path("mobile_number") String mobile_number);

    @GET("app/check_otp/{mobile_number}/{otp}")
    Call<CheckOtpReponse> checkOTP(@Path("mobile_number") String mobile_number, @Path("otp") String otp);

    @GET("app/change_password/{mobile_number}/{password}")
    Call<CheckOtpReponse> changePassword(@Path("mobile_number") String mobile_number, @Path("password") String password);

    @GET("app/tasks/priority/{id}")
    Call<PriortyTaskListResponse> checkPriorityTaskList(@Path("id") String id);

    @GET("app/tasks/overdue/{id}")
    Call<OverDueTaskListResponse> checkOverDueTaskList(@Path("id") String id);

    @GET("app/project/list/{id}")
    Call<ProjectListResponse> checkProjectListResponse(@Path("id") String id);

    @FormUrlEncoded
    @POST("app/project/add/{id}")
    Call<ProjectAddResponse> checkProjectAddReponse(@Path("id") String id, @Field(Parameters.NAME) String name, @Field(Parameters.PROJECT_MEMBERS) String project_membrs);

    @FormUrlEncoded
    @POST("app/project/edit/{id}/{projectId}")
    Call<ProjectEditResponse> checkProjectEditResponse(@Path("id") String id, @Path("projectId") String projectId, @Field("name") String name);

    @GET("app/project/get/{id}/{projectCode}")
    Call<ProjectDetailsById> checkProjectdetailsbyid(@Path("id") String id, @Path("projectCode") String project_code);

    @GET("app/organization/users/{id}")
    Call<CheckBoxResponse> checktheSpinnerResponse(@Path("id") String id);

    @FormUrlEncoded
    @POST("app/task/add/{id}")
    Call<TaskAddResponse> checkTaskAddResponse(@Path("id") String id, @Field(Parameters.NAME) String name, @Field(Parameters.DUE_DATE) String due_date, @Field(Parameters.TASKMEMBERS) String task_members,@Field(Parameters.PRIORITY) String priority);

    @GET("app/task/list/{id}")
    Call<TaskListResponse> checkTheTaskListResponse(@Path("id") String id);


    @GET("app/user_details/{id}")
    Call<UserDetailsResponse> checkTheUserDetailsResponse(@Path("id")String id);

    @FormUrlEncoded
    @POST("app/update_profile/{id}")
    Call<UpdateProfileResponses> checkUpdateProfile(@Path("id")String id,@Field(Parameters.NAME) String name, @Field(Parameters.EMAIL) String email, @Field(Parameters.PASSWORD) String password);

}
