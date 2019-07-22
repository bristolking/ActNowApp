package com.actnow.android.activities;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actnow.android.R;

import static android.view.View.GONE;

public class TimeLineActivity extends AppCompatActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        header();
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
        tv_title.setText("Time Line");
        ImageView mImgeProjectFilter =(ImageView)findViewById(R.id.img_filterbyProject);
        mImgeProjectFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        final Dialog dialog = new Dialog(context,android.R.style.Theme_DeviceDefault_Dialog_Alert);
                        //  Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.filterbyproject);
                        dialog.show();
            }
        });
        ImageView imgeMenuEvent =(ImageView)findViewById(R.id.img_filterByEvent);

    }
}
