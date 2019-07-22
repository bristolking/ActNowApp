package com.actnow.android.activities.insights;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.actnow.android.R;

public class PersonalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights_personal);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
