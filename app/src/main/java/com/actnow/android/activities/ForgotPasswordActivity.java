package com.actnow.android.activities;

import android.app.ProgressDialog;
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
import com.actnow.android.sdk.responses.SendOtpResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText mForgotMobile;
    Button mForgotSubmit;
    UserPrefUtils session;
    View mProgressView,mContentLayout;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_forgot_password);
        initializeViews();
    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mForgotMobile = findViewById(R.id.et_forgotmobile);
        mForgotSubmit = findViewById(R.id.bt_forgotsendCode);
        mForgotSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSendOtp();
            }
        });
    }
    private void attemptSendOtp() {
        mForgotMobile.setError(null);
      final   String mobile= mForgotMobile.getText().toString();
        boolean cancel= false;
        View focusView = null;
        if (TextUtils.isEmpty(mobile)){
            mForgotMobile.setError(getString(R.string.error_required));
            focusView= mForgotMobile;
            cancel=true;
        }if (cancel){
            focusView.requestFocus();
        }else {
           requstSendOtp(mobile);
           //System.out.println("number"+mobile );
        }
    }
    private  void requstSendOtp(final String mobileNumber){
        showProgressDialog();
        Call<SendOtpResponse> call = ANApplications.getANApi().sendOtp(mobileNumber);
        call.enqueue(new Callback<SendOtpResponse>() {
            @Override
            public void onResponse(Call<SendOtpResponse> call, Response<SendOtpResponse> response) {
                AndroidUtils.showProgress(false,mProgressView,mContentLayout);
                if (response.isSuccessful()){
                    if (response.body().getSuccess().equals("true")){
                        hideProgressDialog();
                        Intent otp =new Intent(getApplicationContext(),OTPActivity.class);
                        otp.putExtra("mobileNumber",mobileNumber);
                        startActivity(otp);
                    }else {
                        Snackbar.make(mContentLayout, "Invalid MobileNumber", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<SendOtpResponse> call, Throwable t) {
                Log.d("ForgotPasswordActivity",t.toString());
            }
        });
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}
