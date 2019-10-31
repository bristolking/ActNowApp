package com.actnow.android.activities.invitation;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.adapter.SendInvitationAdapter;

import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendInvitationActivity extends AppCompatActivity {

    UserPrefUtils session;
    private RecyclerView mRecyclerView;
    private SendInvitationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private ArrayList<ExampleItem> mExampleList;

    //private  ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxArrayList;

    private ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList = new ArrayList<OrgnUserRecordsCheckBox>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_send_invitation );
        session = new UserPrefUtils( getApplicationContext() );
        //createExampleList();
        buildRecyclerView();
        appheaderSend();
        initializeViews();

    }

    private void appheaderSend() {
        ImageView imgBackSend = (ImageView) findViewById( R.id.image_back_send );
        imgBackSend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        } );

        TextView mTextTileSend = (TextView) findViewById( R.id.txt_title_send );
        mTextTileSend.setText( "Invitations" );
        ImageView imageMenuSend = (ImageView) findViewById( R.id.image_menu_send );
        imageMenuSend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        } );
    }

    public void showPopup(View view){
        final PopupMenu popupMenu = new PopupMenu( this,view );
        popupMenu.inflate( R.menu.daily_review);
        popupMenu.setGravity( Gravity.RIGHT|Gravity.CENTER);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_settings :
                        Intent i = new Intent( getApplicationContext(), SettingsActivity.class);
                        startActivity(i);
                        return  true;
                    default:
                        return  false;
                }
            }
        } );
    }

    private void initializeViews() {
        EditText editText = findViewById(R.id.editTextRecylerView);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }
    private void filter(String text) {
        ArrayList<OrgnUserRecordsCheckBox> exampleItemfilteredList = new ArrayList<>();

        for (OrgnUserRecordsCheckBox item : orgnUserRecordsCheckBoxList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                exampleItemfilteredList.add(item);
            }
        }

        mAdapter.filterList(exampleItemfilteredList);
    }
    /*private void createExampleList() {
        mExampleList = new ArrayList<>();
        mExampleList.add(new ExampleItem("Nagarjuna", "chintha.nagarjuna@cancrisoft.com"));
        mExampleList.add(new ExampleItem("Kalimullah Khan", "kalimullah.khan@cancri.in"));
        mExampleList.add(new ExampleItem("siva", "siva.siripuram@cancri.in"));
        mExampleList.add(new ExampleItem("Pravin", "pravin.rathi@cancrisoft.com"));
        mExampleList.add(new ExampleItem("Prashanth Reddy .P", "prashanth.reddy@cancri.in"));
        mExampleList.add(new ExampleItem("Susheel Rathi", "susheel.rathi@cancrisoft.com"));
        mExampleList.add(new ExampleItem("mallesh", "mallesh.tekumatla@cancri.in"));
        mExampleList.add(new ExampleItem("Srinivas Gadde", "srinivas.gadde@cancrisoft.com"));
        *//*if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get( i );
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setId( orgnUserRecordsCheckBox.getId() );
                orgnUserRecordsCheckBox1.setName( orgnUserRecordsCheckBox.getName());
                orgnUserRecordsCheckBox1.setEmail(orgnUserRecordsCheckBox.getEmail());
                orgnUserRecordsCheckBoxList.add( orgnUserRecordsCheckBox1 );
            }*//*

    }
*/    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView_share);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new SendInvitationAdapter(orgnUserRecordsCheckBoxList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        HashMap<String,String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        Call<CheckBoxResponse> call  = ANApplications.getANApi().checktheSpinnerResponse( id );
        call.enqueue( new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                System.out.println( "severRepomser1" + response.raw());
                if (response.isSuccessful()){
                    System.out.println( "severRepomser2" + response.raw());

                    if (response.body().getSuccess().equals( "true" )){
                        System.out.println( "severRepomser3" + response.body().getOrgn_users_records());
                        setTasskList(response.body().getOrgn_users_records());

                    }else {

                    }
                }else {

                }

            }

            @Override
            public void onFailure(Call<CheckBoxResponse> call, Throwable t) {

            }
        } );

    }

    private void setTasskList(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get( i );
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setId( orgnUserRecordsCheckBox.getId() );
                orgnUserRecordsCheckBox1.setName( orgnUserRecordsCheckBox.getName() );
                orgnUserRecordsCheckBox1.setEmail( orgnUserRecordsCheckBox.getEmail() );
                orgnUserRecordsCheckBoxList.add( orgnUserRecordsCheckBox1 );
            }
        }
        mRecyclerView.setAdapter(mAdapter);

    }

}

