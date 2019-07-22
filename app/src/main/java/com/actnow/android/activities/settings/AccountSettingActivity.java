package com.actnow.android.activities.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.UserDetailsResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class AccountSettingActivity extends AppCompatActivity {

    TextView mNameAccount, mAccountEmail, mEmailUnderAccount;
    ImageView mImageProfile;
    Button mDeletAccount;
    String name;
    //String id;
    UserPrefUtils session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_account_setting);
        header();
        initializeViews();

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
        tv_title.setText("ACCOUNT");
        TextView tv_settingDone = (TextView) findViewById(R.id.tv_settingDone);
        tv_settingDone.setVisibility(GONE);
        TextView tv_settingEdit = (TextView) findViewById(R.id.tv_settingEdit);
        tv_settingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get(UserPrefUtils.ID);
                String name = userId.get(UserPrefUtils.NAME);
                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                Intent i = new Intent(AccountSettingActivity.this, EditAccountActivity.class);
                i.putExtra("id", id);
                i.putExtra("name", name);
                i.putExtra("email", accountEmail);
                startActivity(i);
            }
        });
    }

    private void initializeViews() {
        mNameAccount = findViewById(R.id.tv_nameAccount);
        mAccountEmail = findViewById(R.id.tv_emailAccount);
        mEmailUnderAccount = findViewById(R.id.tv_accountEmailunder);
        mImageProfile = (ImageView) findViewById(R.id.imge_profileAccount);
    /*    mDeletAccount = (Button) findViewById(R.id.bt_deleteAccount);
        mDeletAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountSettingActivity.this, "Account Will be Deleted", Toast.LENGTH_SHORT).show();
            }
        });*/
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        System.out.println("id" + id);
        Call<UserDetailsResponse> call = ANApplications.getANApi().checkTheUserDetailsResponse(id);
        call.enqueue(new Callback<UserDetailsResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                System.out.println("xxx" + response.raw());
                if (response.isSuccessful()) {
                    System.out.println("data" + response.raw());
                    mNameAccount.setText(response.body().getName());
                    mAccountEmail.setText(response.body().getEmail());
                    mEmailUnderAccount.setText(response.body().getEmail());
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!");
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Log.d("CallBack ", t.toString());
            }
        });
    }

}
