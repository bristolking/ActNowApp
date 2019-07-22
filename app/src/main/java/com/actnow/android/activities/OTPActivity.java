package com.actnow.android.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.CheckOtpReponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
    EditText mOtpMobileNumber;
    Button mOtpSubmit;
    UserPrefUtils session;
    View mProgressView,mContentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_otp);
        initializeViews();
    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mOtpMobileNumber = findViewById(R.id.et_otpMobileNumber);
        mOtpSubmit = findViewById(R.id.bt_otpSubmit);
        mOtpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCheckOTP();
            }
        });
    }
    private void attemptCheckOTP() {
        mOtpMobileNumber.setError(null);
        String otp= mOtpMobileNumber.getText().toString();
        boolean cancel= false;
        View focusView= null;
        if (TextUtils.isEmpty(otp)){
            mOtpMobileNumber.setError(getString(R.string.error_required));
            cancel=true;
            focusView= mOtpMobileNumber;
        }if (cancel){
            focusView.requestFocus();

        }else {
            Intent i = getIntent();
           final String mobile= i.getStringExtra("mobileNumber");
            requestCheckOTP(mobile,otp);
        }

    }
    private  void requestCheckOTP(final String mobile_number, String otp ){
        Call<CheckOtpReponse> call= ANApplications.getANApi().checkOTP(mobile_number,otp);
        call.enqueue(new Callback<CheckOtpReponse>() {
            @Override
            public void onResponse(Call<CheckOtpReponse> call, Response<CheckOtpReponse> response) {
                AndroidUtils.showProgress(false,mProgressView,mContentLayout);
                if (response.isSuccessful()){
                    //System.out.println("response"+response.raw());
                    if (response.body().getSuccess().equals("true")){
                       Intent i = new Intent(OTPActivity.this, ChangePasswordActivity.class);
                        i.putExtra("mobileNumber",mobile_number);
                        startActivity(i);
                        finish();
                    }else {
                        Snackbar.make(mContentLayout, "Invalid OTP", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<CheckOtpReponse> call, Throwable t) {
                Log.d("OTPActivity",t.toString());


            }
        });
    }
}
