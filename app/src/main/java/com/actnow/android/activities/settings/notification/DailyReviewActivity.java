package com.actnow.android.activities.settings.notification;


import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;

import static android.view.Gravity.NO_GRAVITY;

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
