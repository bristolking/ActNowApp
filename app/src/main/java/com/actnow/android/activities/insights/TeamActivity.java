package com.actnow.android.activities.insights;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.actnow.android.R;

public class TeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights_team);
    }
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }

}
