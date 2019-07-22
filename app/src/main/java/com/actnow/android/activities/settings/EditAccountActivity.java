package com.actnow.android.activities.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.UpdateProfileResponses;
import com.actnow.android.utils.UserPrefUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class EditAccountActivity extends AppCompatActivity {
    EditText mEditName,mEditEmail,mEditPassWord;
    TextView mName,mEmail;
    ImageView mProfilePhoto;
    UserPrefUtils session;
    String id;
    String name;
    String accountEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_edit_account);
        header();
        initializeViews();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = (String) b.get("id");
            name = (String) b.get("name");
            mName.setText("" + name);
            accountEmail = (String) b.get("email");
            mEmail.setText("" + accountEmail);
            System.out.println("passsed" + name + id + accountEmail + accountEmail);
        }

    }
    private void header() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_backsetting);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.txt_titlesetting);
        tv_title.setText("EDIT ACCOUNT");
        TextView tv_settingDone = (TextView) findViewById(R.id.tv_settingDone);
        tv_settingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountUpadte();
            }
        });
        TextView tv_settingEdit = (TextView) findViewById(R.id.tv_settingEdit);
        tv_settingEdit.setVisibility(GONE);
    }
    private void initializeViews() {
        mName = findViewById(R.id.tv_name);
        mEmail = findViewById(R.id.tv_email);
        mProfilePhoto = findViewById(R.id.image_photo);
        mEditName = findViewById(R.id.et_editName);
        mEditEmail = findViewById(R.id.et_editEmail);
        mEditPassWord = findViewById(R.id.et_editPassword);
    }
    private void accountUpadte() {
        mEditName.setError(null);
        String name = mEditName.getText().toString();
        mEditEmail.setError(null);
        String email = mEditEmail.getText().toString();
        mEditPassWord.setError(null);
        String password = mEditPassWord.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            mEditName.setError(getString(R.string.error_required));
            focusView = mEditName;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEditEmail.setError(getString(R.string.error_required));
            focusView = mEditEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mEditPassWord.setError(getString(R.string.error_required));
            focusView = mEditPassWord;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
             requstToUpadteProfile(id,name,email,password);
        }
    }

    private void requstToUpadteProfile(String id,String name, String email, String password) {

        Call<UpdateProfileResponses> call = ANApplications.getANApi().checkUpdateProfile(id,name,email,password);
        call.enqueue(new Callback<UpdateProfileResponses>() {
            @Override
            public void onResponse(Call<UpdateProfileResponses> call, Response<UpdateProfileResponses> response) {
                System.out.println("call"+response.raw());
                if (response.isSuccessful()){
                    System.out.println("profile"+ response.raw());
                    if (response.body().getSuccess().equals("true")){
                        System.out.println("data"+ response.body().getSuccess());
                        UpdateProfileResponses responses = response.body();
                        Intent i =new Intent(EditAccountActivity.this,SettingsActivity.class);
                        startActivity(i);
                    }else {

                    }
                }else {

                }
            }
            @Override
            public void onFailure(Call<UpdateProfileResponses> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);

            }
        });
    }


}
