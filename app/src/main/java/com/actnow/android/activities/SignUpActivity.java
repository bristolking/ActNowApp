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

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.SignUpResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText msignUpName,msignUpEmail,msignUpMobile,mOrganizationName,msignUpPassword;
    Button msignUpButton;
    UserPrefUtils session;
    View mProgressView,mContentLayout;
    CheckBox mDisclaimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_sign_up);
        initializeViews();
    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        msignUpName = findViewById(R.id.et_signUpname);
        msignUpEmail= findViewById(R.id.et_signUpEmail);
        msignUpPassword=findViewById(R.id.et_sinUpPassword);
        msignUpMobile =findViewById(R.id.et_signUpmobile);
        msignUpButton= findViewById(R.id.bt_signUp);
        mDisclaimer = (CheckBox)findViewById(R.id.chk_disclaimer);

        msignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
    }
    private void attemptSignUp() {
        msignUpName.setError(null);
        String name= msignUpName.getText().toString();
        msignUpEmail.setError(null);
        String email=msignUpEmail.getText().toString();
        msignUpMobile.setError(null);
        String mobile=msignUpMobile.getText().toString();
        msignUpPassword.setError(null);
        String password= msignUpPassword.getText().toString();
        boolean cancel= false;
        View focusView= null;
        if (TextUtils.isEmpty(name)){
            msignUpName.setError(getString(R.string.error_required));
            focusView=msignUpName;
            cancel=true;
        }if (TextUtils.isEmpty(email)){
            msignUpEmail.setError(getString(R.string.error_required));
            focusView=msignUpEmail;
            cancel=true;
        }if (TextUtils.isEmpty(mobile)){
            msignUpMobile.setError(getString(R.string.error_required));
            focusView=msignUpMobile;
            cancel=true;

        }if (TextUtils.isEmpty(password)){
            msignUpPassword.setError(getString(R.string.error_required));
            focusView=msignUpPassword;
            cancel=true;
        }if(cancel){
            focusView.requestFocus();
        }else{
              if (mDisclaimer.isChecked()) {
                  AndroidUtils.showProgress(true,mProgressView,mContentLayout);
                  requestSignUp(name,email,mobile,password);
              } else {
                Snackbar.make(mContentLayout, "Accept Terms of Service & Privacy Policy", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
    private void requestSignUp(String userName,String userEmail,String mobileNumber,String userPassword ){
        System.out.println( "logindata"+ userEmail+userName+ mobileNumber+userPassword);
        Call<SignUpResponse> call = ANApplications.getANApi().userSignUp(userName,userEmail,mobileNumber,userPassword);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                AndroidUtils.showProgress(false,mProgressView,mContentLayout);
                if (response.isSuccessful()){
                    if (response.body().getSuccess().equals("true")){
                        SignUpResponse response2= response.body();
                        session.createLoginSession(response2.getId(),response2.getName(),response2.getEmail(),response2.getMobile_number(),response2.getOrgn_code(),response2.getUser_type(),response2.getProvider_id(),response2.getProvider_name(),response2.getImage_path());
                        activityLogin();
                        AndroidUtils.displayToast(getApplicationContext(),"Your account has been successfully created.");

                    } else {
                        Snackbar.make(mContentLayout, "Invalid credentials", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }
    private void activityLogin() {
        Intent login=new Intent(SignUpActivity.this,OrngActivity.class);
        startActivity(login);
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
