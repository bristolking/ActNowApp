package com.actnow.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.CheckOtpResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    EditText mNewPassword, mConfromPassword;
    Button mChangeSubmit;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_change_password);
        initializeViews();
    }

    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mNewPassword = findViewById(R.id.et_newpassword);
        mConfromPassword = findViewById(R.id.et_confromPassword);
        mChangeSubmit = findViewById(R.id.bt_changesubmit);
        mChangeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showProgressDialog();
                attemptChangepasswordLogin();
            }
        });
    }

    private void attemptChangepasswordLogin() {
        mNewPassword.setError(null);
        String newPassword = mNewPassword.getText().toString();
        mConfromPassword.setError(null);
        String confromPassword = mConfromPassword.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(newPassword)) {
            mNewPassword.setError(getString(R.string.error_required));
            cancel = true;
            focusView = mNewPassword;
        }
        if (TextUtils.isEmpty(confromPassword)) {
            mConfromPassword.setError(getString(R.string.error_required));
            cancel = true;
            focusView = mConfromPassword;
        }
        if (cancel) {
            focusView.requestFocus();

        } else {
            Intent intent = getIntent();
            String mobile = intent.getStringExtra("mobileNumber");
            //System.out.println("password" + mobile + newPassword);
            changePassWrodRequst(mobile, newPassword);
        }

    }

    private void changePassWrodRequst(String mobile_number, String password) {
        Call<CheckOtpResponse> call = ANApplications.getANApi().changePassword(mobile_number, password);
        call.enqueue(new Callback<CheckOtpResponse>() {
            @Override
            public void onResponse(Call<CheckOtpResponse> call, Response<CheckOtpResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                      //  hideProgressDialog();
                        Intent i = new Intent(ChangePasswordActivity.this, SignInActivity.class);
                        startActivity(i);
                    } else {
                        Snackbar.make(mContentLayout, "Invalid Password", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<CheckOtpResponse> call, Throwable t) {
                Log.d("ChangePasswordActivity",t.toString());


            }
        });
    }
//    private void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(getApplicationContext());
//            mProgressDialog.setMessage(getString(R.string.loading));
//            mProgressDialog.setIndeterminate(true);
//            mProgressDialog.setCancelable(false);
//        }
//        mProgressDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.hide();
//        }
//    }
}
