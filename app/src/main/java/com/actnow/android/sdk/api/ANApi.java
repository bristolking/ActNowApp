package com.actnow.android.sdk.api;

import com.actnow.android.sdk.responses.CheckOtpResponse;
import com.actnow.android.sdk.responses.OverDueTaskListResponse;
import com.actnow.android.sdk.responses.PriortyTaskListResponse;
import com.actnow.android.sdk.responses.ProjectAddResponse;
import com.actnow.android.sdk.responses.ProjectEditResponse;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ReminderAdd;
import com.actnow.android.sdk.responses.ReminderResponse;
import com.actnow.android.sdk.responses.SendOtpResponse;
import com.actnow.android.sdk.responses.SignInResponse;
import com.actnow.android.sdk.responses.SignUpResponse;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.TaskAddResponse;
import com.actnow.android.sdk.responses.TaskComplete;
import com.actnow.android.sdk.responses.TaskEditResponse;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.sdk.responses.UpdateProfileResponses;
import com.actnow.android.sdk.responses.UserDetailsResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ANApi {
    @FormUrlEncoded
    @POST("app/login")
    Call<SignInResponse> userSignIn(@Field(Parameters.MOBILENO) String mail, @Field(Parameters.PASSWORD) String pass);

    @FormUrlEncoded
    @POST("app/signup")
    Call<SignUpResponse> userSignUp(@Field(Parameters.NAME) String name, @Field(Parameters.EMAIL) String mail, @Field(Parameters.MOBILENO) String mobileNumber,@Field(Parameters.PASSWORD) String password);

    @GET("app/send_otp/{mobile_number}")
    Call<SendOtpResponse> sendOtp(@Path("mobile_number") String mobile_number);

    @GET("app/check_otp/{mobile_number}/{otp}")
    Call<CheckOtpResponse> checkOTP(@Path("mobile_number") String mobile_number, @Path("otp") String otp);

    @GET("app/change_password/{mobile_number}/{password}")
    Call<CheckOtpResponse> changePassword(@Path("mobile_number") String mobile_number, @Path("password") String password);

    @GET("app/user_details/{id}")
    Call<UserDetailsResponse> checkTheUserDetailsResponse(@Path("id")String id);

    @Multipart
    @POST("app/update_profile/{id}")
    //Call<UpdateProfileResponses> checkUpdateProfile(@Part("id") RequestBody id, @Part(Parameters.NAME) RequestBody name, @Part(Parameters.EMAIL) RequestBody email, @Part(Parameters.PASSWORD) RequestBody password, @Part MultipartBody.Part file);
    Call<UpdateProfileResponses> checkUpdateProfile(@Part("id") String id, @Part(Parameters.NAME) String name, @Part(Parameters.EMAIL) String  email, @Part(Parameters.PASSWORD) String password, @Part MultipartBody.Part image_path);
    @FormUrlEncoded
    @POST("app/organization/add/{id}")
    Call<SendOtpResponse> checkOrgCode(@Path("id")String id,@Field(Parameters.NAME)String name);

    @GET("app/organization/users/{id}")
    Call<CheckBoxResponse> checktheSpinnerResponse(@Path("id") String id);

    @GET("app/tasks/priority/{id}")
    Call<PriortyTaskListResponse> checkPriorityTaskList(@Path("id") String id);
    @GET("app/tasks/overdue/{id}")
    Call<OverDueTaskListResponse> checkOverDueTaskList(@Path("id") String id);


    @GET("app/project/list/{id}")
    Call<ProjectListResponse> checkProjectListResponse(@Path("id") String id);
    @FormUrlEncoded
    @POST("app/project/add/{id}")
    Call<ProjectAddResponse> checkProjectAddReponse(@Path("id") String id, @Field(Parameters.NAME) String name, @Field(Parameters.COLOR) String color);
    @FormUrlEncoded
    @POST("app/project/edit/{id}/{code}")
    Call<ProjectEditResponse> checkProjectEditResponse(@Path("id") String id,@Path("code") String project_code,@Field(Parameters.NAME) String name,@Field(Parameters.COLOR) String color,@Field("orgn_code") String orgn_code );
    @FormUrlEncoded
    @POST("app/project/comments/{id}/{code}")
    Call<ResponseBody> checkProjectCommentList(@Path("id") String id, @Path("code") String project_code, @Field("orgn_code")String orgn_code);


    /*All The Task APIs */
    @FormUrlEncoded
    @POST("app/task/add/{id}")
    Call<TaskAddResponse> checkTaskAddResponse(@Path("id") String id, @Field(Parameters.NAME) String name, @Field(Parameters.DUE_DATE) String due_date,@Field("priority") String priority, @Field("project_code") String project_code, @Field("orgn_code")String orgn_code,@Field( "repeat_type")String repeat_type,@Field( "week_days")String week_days,@Field("days")String days,@Field( "months")String months);
    @FormUrlEncoded
    @POST("app/task/edit/{id}/{task_code}")
    Call<TaskEditResponse> checkTheTaskEditReponse(@Path("id")String id,@Path( "task_code")String task_code,@Field("name")String name,@Field("due_date")String due_date,@Field( "priority")String priority,@Field("project_code")String project_code,@Field("orgn_code")String orgn_code,@Field("repeat_type")String repeat_type,@Field( "week_days")String week_days,@Field("days")String days,@Field("months")String months);
    @GET("app/task/list/{id}")
    Call<TaskListResponse> checkTheTaskListResponse(@Path("id") String id);
    @FormUrlEncoded
    @POST("app/task/comments/{id}/{code}")
    Call<ResponseBody> checkTheTaskCommentList(@Path("id")String id,@Path("code")String task_code,@Field("orgn_code") String orgn_code);
    @FormUrlEncoded
    @POST("app/task/complete/{id}/{code}")
    Call<TaskComplete> checkTheTaskComplete(@Path("id")String id, @Path( "code")String task_code, @Field("orgn_code")String orgn_code);
    @FormUrlEncoded
    @POST("app/task/approve/{id}/{task_code}")
    Call<TaskComplete> checkTheTaskApprove(@Path("id")String id,@Path("task_code")String task_code,@Field("orgn_code") String orgn_code);
    @FormUrlEncoded
    @POST("app/task/disapprove/{id}/{task_code}")
    Call<TaskComplete> checkTheDisApprove(@Path( "id") String id,@Path("task_code")String task_code,@Field("orgn_code")String orgn_code);

    /*Comment Api TASk and Project */

    @Multipart
    @POST("app/comment/add/{id}")
    Call<TaskComplete> checkTheCommentAdd(@Part("id")String id,@Part("orgn_code")String orgn_code,@Part("comment")String comment,@Part("project_code")String project_code,@Part("task_code")String task_code,@Part MultipartBody.Part  files);

    @FormUrlEncoded
    @POST("app/comment/edit/{id}")
    Call<TaskComplete>  checkTheTaskEdit(@Path("id")String id,@Field("comment_id")String comment_id,@Field("orgn_code")String orgn_code,@Field("comment")String comment,@Field("task_code") String task_code,@Field("project_code") String project_code);
    @POST("app/comment/delete/{id}")
    Call<TaskComplete> checkTheTaskDelete(@Path("id")String id,@Field("orgn_code")String  orgn_code,@Field("comment_id")String comment_id);


    /*Reminder API delete and Savee*/
    @GET("app/task_reminders/{id}/{task_code}/{orgn_code}")
    Call<ReminderResponse> checkTheReminderList(@Path("id")String id,@Path("task_code")String task_code,@Path("orgn_code") String orgn_code);

    @FormUrlEncoded
    @POST("app/task/save_reminder/{id}/{task_code}")
    Call<ReminderAdd> checTheReminderAdd(@Path("id")String id,@Path("task_code")String task_code,@Field("reminder_date")String reminder_date,@Field("remind_to")String remind_to,@Field("orgn_code")String orgn_code);

    @GET("app/task/delete_reminder/{id}/{reminder_task_id}/{orgn_code}")
    Call<ReminderAdd> checkTheReminderDelete(@Path("id")String id, @Path("reminder_task_id")String reminder_task_id,@Path("orgn_code")String orgn_code);



    /*
    @GET("app/project/get/{id}/{projectCode}")
    Call<ProjectDetailsById> checkProjectdetailsbyid(@Path("id") String id, @Path("projectCode") String project_code);*/

    /*
    @FormUrlEncoded
    @POST("app/project/comments/{id}/{code}")
    Call<ProjectCommentListResponse> checkProject(@Path("id") String id, @Path("code") String project_code, @Field("orgn_code")String orgn_code);*/

  /*  @FormUrlEncoded
    @POST("app/comment/add/{id}")
    Call<CommentAdd> checkTheAddComment(@Path("id") String id,@Field("comment")String comment,@Field("project_id") String project_id,@Field("task_id")String task_id,@Part MultipartBody.Part surveyImagesParts);
*/
}
