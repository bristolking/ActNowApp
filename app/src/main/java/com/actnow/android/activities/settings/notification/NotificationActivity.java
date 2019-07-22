package com.actnow.android.activities.settings.notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;
import static android.view.View.GONE;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
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
        tv_title.setText("Notifications");
        TextView tv_settingDone = (TextView) findViewById(R.id.tv_settingDone);
        tv_settingDone.setVisibility(GONE);
        TextView tv_settingEdit = (TextView) findViewById(R.id.tv_settingEdit);
        tv_settingEdit.setVisibility(GONE);
    }
    private void initializeViews() {
        View  view = (View)findViewById(R.id.re_dailyReview);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(NotificationActivity.this,DailyReviewActivity.class);
                startActivity(i);
                finish();
            }
        });
        View sharingView =(View)findViewById(R.id.re_sharing);
        sharingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(NotificationActivity.this,SharingActivity.class);
                startActivity(i);
                finish();

            }
        });
        View subScribedEmailView =(View)findViewById(R.id.re_subScribedEmail);
        subScribedEmailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(NotificationActivity.this,SubscribedemailActivity.class);
                startActivity(i);
                finish();

            }
        });
        View gotoPrimaryView =(View)findViewById(R.id.le_gotoPrimarySetting);
        gotoPrimaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent i =new Intent(NotificationActivity.this,GotoprimaryActivity.class);
                startActivity(i);
                finish();*/
                Toast.makeText(getApplicationContext(),"work in progress",Toast.LENGTH_SHORT).show();

            }
        });



    }

}
