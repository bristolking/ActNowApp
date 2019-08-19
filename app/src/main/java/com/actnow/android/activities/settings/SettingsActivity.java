package com.actnow.android.activities.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;
import com.actnow.android.activities.settings.notification.NotificationActivity;
import com.actnow.android.utils.UserPrefUtils;

import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.TEXT_ALIGNMENT_CENTER;


public class SettingsActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    String id;
    TextView mEmilUnderAccount;
    final Context context = this;

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
        ImageView imgeBack = (ImageView) findViewById(R.id.image_backNotification);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.txt_titleNotification);
        tv_title.setText("Settings");
        final ImageView imageMenu = (ImageView) findViewById(R.id.img_menuNotification);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.restore_all_defalut);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.TOP|TEXT_ALIGNMENT_CENTER | Gravity.RIGHT;
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                wlp.x = 1; // The new position of the X coordinates
                wlp.y = 1; // The new position of the Y coordinates
                wlp.width = 610;// Width
                window.setAttributes(wlp);
                dialog.show();
            }

        });

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
    public void onBackPressed() {
        super.onBackPressed();
    }

}
