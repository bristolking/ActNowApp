package com.actnow.android.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.actnow.android.R;

public class NoNetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_no_network );
        Button btntryAgain = (Button) findViewById( R.id.btn_try );
        btntryAgain.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent( NoNetworkActivity.this, TodayTaskActivity.class );
                startActivity( home );
                finish();
            }
        } );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}