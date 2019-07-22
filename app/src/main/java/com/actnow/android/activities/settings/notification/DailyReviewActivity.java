package com.actnow.android.activities.settings.notification;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;


public class DailyReviewActivity extends AppCompatActivity {
   Switch mSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_review);
        header();
        initializeViews();

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
        tv_title.setText("Daily review");
        final ImageView imageMenu = (ImageView) findViewById(R.id.img_menuNotification);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Work in progress!",Toast.LENGTH_SHORT).show();

                }
        });
    }
    private void initializeViews() {
        /*LinearLayout linearLayout =(LinearLayout) findViewById(R.id.le_switch);
        Switch sb = new Switch(this);
        sb.setTextOff("OFF");
        sb.setTextOn("ON");
        sb.setChecked(true);
        linearLayout.addView(sb);*/
         mSwitch = (Switch)findViewById(R.id.switch1);
         mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked) {
                     // The toggle is enabled
                     mSwitch.setTextOn("ON");
                     mSwitch.setText("ON");
                     Toast.makeText(getApplicationContext(),"selected ON",Toast.LENGTH_SHORT).show();
                 } else {
                     // The toggle is disabled
                     mSwitch.setTextOff("Off");
                     mSwitch.setText("OFF");
                     Toast.makeText(getApplicationContext(),"selected Off",Toast.LENGTH_SHORT).show();
                 }
             }
         });


    }


}
