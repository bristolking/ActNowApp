package com.actnow.android.activities.settings.notification;


import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;

import static android.view.View.TEXT_ALIGNMENT_CENTER;


public class DailyReviewActivity extends AppCompatActivity {
   Switch mSwitch;
    final Context context = this;

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
    public void onBackPressed() {
        super.onBackPressed();
    }


}
