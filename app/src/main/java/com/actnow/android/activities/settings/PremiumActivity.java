package com.actnow.android.activities.settings;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;

public class PremiumActivity extends AppCompatActivity {
    TextView mAccountUpgrade,mRefreshSubscription;
    Button mBtContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        initializeViews();
    }

    private void initializeViews() {
        mAccountUpgrade = (TextView)findViewById(R.id.tv_acUpagrade);
        mRefreshSubscription =(TextView)findViewById(R.id.tv_refreshSubscription);
        mBtContinue =(Button)findViewById(R.id.bt_continue);
        mAccountUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Work in Progress",Toast.LENGTH_SHORT).show();
            }
        });
        mRefreshSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Work in Progress",Toast.LENGTH_SHORT).show();
            }
        });
        mBtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Work in Progress",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
