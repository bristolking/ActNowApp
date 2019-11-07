package com.actnow.android.activities.settings.notification;

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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;
import com.actnow.android.utils.UserPrefUtils;

import static android.view.Gravity.NO_GRAVITY;


public class NotificationActivity extends AppCompatActivity {
    final Context context = this;
    UserPrefUtils session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_notification);
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
        tv_title.setText("Notification");
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
    public void onBackPressed() {
        super.onBackPressed();
    }

}
