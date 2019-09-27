package com.actnow.android.activities.invitation;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.adapter.CheckBoxAdapter;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareActivity extends AppCompatActivity {
    RecyclerView mRecyclerViewShare;
    RecyclerView.LayoutManager mShareLayoutManager;
    private ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList = new ArrayList<OrgnUserRecordsCheckBox>();
    UserPrefUtils session;
    View mProgressView, mContentLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils(getApplicationContext());
        setContentView( R.layout.activity_share );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( null );

        initializeViews();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.search_menu, menu );

        return true;
    }

    private void initializeViews() {
        mRecyclerViewShare = (RecyclerView) findViewById( R.id.recyclerView_share );
        mShareLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mRecyclerViewShare.setLayoutManager( mShareLayoutManager );
        mRecyclerViewShare.setItemAnimator( new DefaultItemAnimator() );
        CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter( orgnUserRecordsCheckBoxList, R.layout.individual_check, getApplicationContext() );
        mRecyclerViewShare.setAdapter( checkBoxAdapter );

        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        System.out.println( "id" + id );

        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse( id );
        call.enqueue( new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                System.out.println( "response" + response.raw() );
                if (response.isSuccessful()) {
                    System.out.println( "r" + response.raw() );
                    if (response.body().getSuccess().equals( "true" )) {
                        System.out.println( "data" + response.body().getSuccess() );
                        setShareUser( response.body().getOrgn_users_records() );
                    } else {
                       // Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<CheckBoxResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );
            }
        } );
    }

    private void setShareUser(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get( i );
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setName( orgnUserRecordsCheckBox.getName() );
                orgnUserRecordsCheckBox1.setId( orgnUserRecordsCheckBox.getId() );
                orgnUserRecordsCheckBox1.setEmail( orgnUserRecordsCheckBox.getEmail() );
                orgnUserRecordsCheckBox1.setMobile_number( orgnUserRecordsCheckBox.getMobile_number() );
                orgnUserRecordsCheckBox1.setProvider_id( orgnUserRecordsCheckBox.getProvider_id() );
                orgnUserRecordsCheckBox1.setProvider_name( orgnUserRecordsCheckBox.getProvider_name() );
                orgnUserRecordsCheckBox1.setOrgn_code( orgnUserRecordsCheckBox.getOrgn_code() );
                orgnUserRecordsCheckBox1.setPassword( orgnUserRecordsCheckBox.getPassword() );
                orgnUserRecordsCheckBox1.setImage_path( orgnUserRecordsCheckBox.getImage_path() );
                orgnUserRecordsCheckBox1.setUser_type( orgnUserRecordsCheckBox.getUser_type() );
                orgnUserRecordsCheckBox1.setOtp( orgnUserRecordsCheckBox.getOtp() );
                orgnUserRecordsCheckBox1.setStatus( orgnUserRecordsCheckBox.getStatus() );
                orgnUserRecordsCheckBox1.setEmail_verified_at( orgnUserRecordsCheckBox.getEmail_verified_at() );
                orgnUserRecordsCheckBox1.setVerified( orgnUserRecordsCheckBox.getVerified() );
                orgnUserRecordsCheckBox1.setRemember_token( orgnUserRecordsCheckBox.getRemember_token() );
                orgnUserRecordsCheckBox1.setRefresh_token( orgnUserRecordsCheckBox.getRefresh_token() );
                orgnUserRecordsCheckBox1.setCreated_at( orgnUserRecordsCheckBox.getCreated_at() );
                orgnUserRecordsCheckBox1.setUpdated_at( orgnUserRecordsCheckBox.getUpdated_at() );
                orgnUserRecordsCheckBox1.setOther_orgns( orgnUserRecordsCheckBox.getOther_orgns() );
                orgnUserRecordsCheckBoxList.add( orgnUserRecordsCheckBox1 );
            }
            mRecyclerViewShare.setAdapter( new CheckBoxAdapter( orgnUserRecordsCheckBoxList, R.layout.individual_check, getApplicationContext() ) );
        }
    }


}
