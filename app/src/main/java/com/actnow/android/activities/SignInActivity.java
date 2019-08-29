package com.actnow.android.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.tasks.TaskAddListActivity;
//import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.sdk.responses.SignInResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView,mContentLayout;
    EditText mEmail,mPass;
    Button mLogin,mSignUp;
    TextView mForgotPassWord;
    CheckBox mDisclaimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        if(session.isLoggedIn()){
            activityMe();
        }
        setContentView(R.layout.activity_sign_in);
        initializeViews();
    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mEmail = findViewById(R.id.et_loginEmail);
        mPass = findViewById(R.id.et_loginPassword);
        mDisclaimer = (CheckBox)findViewById(R.id.chk_disclaimer);
        mForgotPassWord= findViewById(R.id.forgot_password);
        mForgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityForgot();
            }
        });
        mLogin = findViewById(R.id.bt_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        mSignUp = findViewById(R.id.bt_signup);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitySignUp();
            }
        });
    }
    private void attemptLogin(){
        mEmail.setError(null);
        String email = mEmail.getText().toString();
        mPass.setError(null);
        String pass  = mPass.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if(TextUtils.isEmpty(email)){
            mEmail.setError(getString(R.string.error_required));
            focusView = mEmail;
            cancel = true;
        } else if (!AndroidUtils.isEmailorMobileValid(email)){
            mEmail.setError("Invalid Mobile/Email field");
            focusView = mEmail;
            cancel = true;
        }
        if(TextUtils.isEmpty(pass)){
            mPass.setError(getString(R.string.error_required));
            focusView = mPass;
            cancel = true;
        }
        if(cancel){
            focusView.requestFocus();
        } else {
            if (mDisclaimer.isChecked()) {
                AndroidUtils.showProgress(true,mProgressView,mContentLayout);
                requestLogin(email, pass);
            } else {
                Snackbar.make(mContentLayout, "Accept Terms of Service & Privacy Policy", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
    private void requestLogin(String username, String password){
        Call<SignInResponse> call = ANApplications.getANApi().userSignIn(username,password);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                AndroidUtils.showProgress(false,mProgressView,mContentLayout);
                if (response.isSuccessful()){
                    if (response.body().getSuccess().equals("true")){
                        SignInResponse response1 = response.body();
                        session.createLoginSession(response1.getId(),response1.getName(),response1.getEmail(),response1.getMobile_number(),response1.getOrgn_code(),response1.getUser_type(),response1.getProvider_id(),response1.getProvider_name());
                        System.out.println("response1"+getTaskId());
                        //AndroidUtils.displayToast(getApplicationContext(),"Your account has been successfully created.");
                       activityMe();
                    } else {
                        Snackbar.make(mContentLayout, "Invalid credentials", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }
    private void activityMe(){
        Intent i =new Intent(SignInActivity.this, TodayTaskActivity.class);
        startActivity(i);
        finish();
    }

    private void activitySignUp(){
        Intent me = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(me);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        finish();
    }
    private void activityForgot() {
        Intent forgot = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
        startActivity(forgot);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        finish();
    }
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            moveTaskToBack(true);
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            Snackbar.make(mContentLayout, "Press once again to exit", Snackbar.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}
