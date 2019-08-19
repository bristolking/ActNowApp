package com.actnow.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.CheckOtpResponse;
import com.actnow.android.sdk.responses.SendOtpResponse;
import com.actnow.android.sdk.responses.SignInResponse;
import com.actnow.android.utils.UserPrefUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrngActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    EditText mOrngCode;
    Button mOrngCodeSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_orng);
        initializeViews();
    }

    private void initializeViews() {
        mOrngCode = findViewById(R.id.et_orngCodeEdit);
        mOrngCodeSubmit = findViewById(R.id.bt_orngCode);
        mOrngCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCreateOrngCode();
            }
        });
    }

    private void attemptCreateOrngCode() {
        mOrngCode.setError(null);
        String ornCode = mOrngCode.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(ornCode)) {
            mOrngCode.setError(getString(R.string.error_required));
            focusView = mOrngCode;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            HashMap<String, String> userId = session.getUserDetails();
            String id = userId.get(UserPrefUtils.ID);
            requestCreateOrnCode(id, ornCode);
            System.out.println("signup" + id + ornCode);
        }
    }

    private void requestCreateOrnCode(String id, String name) {
        System.out.println("data"+ id+name);
        Call<SendOtpResponse> call = ANApplications.getANApi().checkOrgCode(id, name);
        call.enqueue(new Callback<SendOtpResponse>() {
            @Override
            public void onResponse(Call<SendOtpResponse> call, Response<SendOtpResponse> response) {
                System.out.println("api"+ response.raw());
                if (response.isSuccessful()){
                    System.out.println("res"+ response.raw());
                    if (response.body().getSuccess().equals("true")){
                        System.out.println("ok"+ response.message());
                        Intent i = new Intent(OrngActivity.this,TodayTaskActivity.class);
                        startActivity(i);
                    }
                }
            }
            @Override
            public void onFailure(Call<SendOtpResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }
}

