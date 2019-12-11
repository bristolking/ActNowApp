package com.actnow.android.activities.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;
import static android.view.View.GONE;

public class HelpFeedBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_feed_back);
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
        tv_title.setText("HELP&FEEDBACK");
        TextView tv_settingDone = (TextView) findViewById(R.id.tv_settingDone);
        tv_settingDone.setVisibility(GONE);
    }
    private void initializeViews() {
        View viewGettingGuide = (View)findViewById(R.id.re_gettingGuide);
        viewGettingGuide.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getApplicationContext(),"Work in progress!" ,Toast.LENGTH_LONG ).show();
            }
        } );
        View viewHelpCenter = (View)findViewById(R.id.re_helpcenter);
        viewHelpCenter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getApplicationContext(),"Work in progress!" ,Toast.LENGTH_LONG ).show();
            }
        } );
        View viewcontactSupport = (View)findViewById(R.id.re_contactSupport);
        viewcontactSupport.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getApplicationContext(),"Work in progress!" ,Toast.LENGTH_LONG ).show();
            }
        } );
        View viewShare = (View)findViewById(R.id.re_shreApp);
        viewShare.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=com.actnow.android");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        } );

        View viewRateAPP = (View)findViewById(R.id.rateApp_appstore);
        viewRateAPP.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), " unable to find market app", Toast.LENGTH_LONG).show();
                }

            }
        } );


    }
    public void onBackPressed() {
        super.onBackPressed();
    }

}
