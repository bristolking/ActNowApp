package com.actnow.android.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;
import com.actnow.android.activities.settings.notification.NotificationActivity;
import com.actnow.android.utils.UserPrefUtils;

import java.util.HashMap;

import static android.view.Gravity.NO_GRAVITY;



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
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                showPopupmenu(view);

            }

        });

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void   showPopupmenu(View view) {
        PopupMenu popupMenu = new PopupMenu( this,view, Gravity.RIGHT|NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate( R.menu.restore_all_defaults ,popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.restore:
                          Toast.makeText( getApplicationContext(),"Default setting retored",Toast.LENGTH_SHORT).show();
                        //Snackbar.make( mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT ).show();
                        return  true;
                    default:
                        return  false;
                }
            }
        } );
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

        View viewAbout = (View)findViewById(R.id.setting_about);
        viewAbout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Work in Progress!",Toast.LENGTH_LONG).show();
            }
        } );
        View viewSysnc = (View)findViewById(R.id.setting_sysnc);
        viewSysnc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Work in Progress!",Toast.LENGTH_LONG).show();
            }
        } );
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
