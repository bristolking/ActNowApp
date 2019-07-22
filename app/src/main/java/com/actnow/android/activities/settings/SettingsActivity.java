package com.actnow.android.activities.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.activities.settings.notification.NotificationActivity;
import com.actnow.android.utils.UserPrefUtils;

import java.util.HashMap;

import static android.view.View.GONE;


public class SettingsActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    String id;
    TextView mEmilUnderAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_settings);
        header();
        initializeViews();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            String accountEmail = (String) b.get("email");
            mEmilUnderAccount.setText("" + accountEmail);
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
        tv_title.setText("Settings");
        TextView tv_settingDone = (TextView) findViewById(R.id.tv_settingDone);
        tv_settingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        TextView tv_settingEdit = (TextView) findViewById(R.id.tv_settingEdit);
        tv_settingEdit.setVisibility(GONE);
    }

    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mEmilUnderAccount = findViewById(R.id.tv_underEmailAccountSetting);
        View mReViewAccount = (View) findViewById(R.id.re_reAccount);
        mReViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAccount();
            }
        });
        View reViewHelp = (View) findViewById(R.id.re_help);
        reViewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, HelpFeedBackActivity.class);
                startActivity(i);

            }
        });
        View reViewActNowPremium = (View) findViewById(R.id.re_actNowPremium);
        reViewActNowPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, PremiumActivity.class);
                startActivity(i);

            }
        });
        View reNotification = (View) findViewById(R.id.re_notification);
        reNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification = new Intent(SettingsActivity.this, NotificationActivity.class);
                startActivity(notification);

            }
        });
        Button mSettingLogout = (Button) findViewById(R.id.setting_logout);
        mSettingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });

    }

    private void activityAccount() {
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get(UserPrefUtils.ID);
        Intent i = new Intent(SettingsActivity.this, AccountSettingActivity.class);
        i.putExtra("id", id);
        startActivity(i);
    }

}
