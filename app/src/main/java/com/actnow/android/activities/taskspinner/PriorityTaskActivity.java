package com.actnow.android.activities.taskspinner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.adapter.PriorityTaskAdapter;
import com.actnow.android.sdk.responses.PriortyTaskListResponse;
import com.actnow.android.sdk.responses.PriorityTaskListRecords;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriorityTaskActivity extends AppCompatActivity  {
    RecyclerView mOverDueTaskRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    PriorityTaskAdapter mPriorityTaskAdapter;
    String[] arrayItems = {"Priorty TaskOffline","Over Due TaskOffline"};
    private List<TaskListRecords> priorityTaskListRecords =new ArrayList<TaskListRecords>();
    UserPrefUtils session;
    View mProgressView,mContentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_priority_task);
        appHeader();
        initializeViews();
    }
    private void appHeader() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.txt_title);
        tv_title.setVisibility(View.GONE);
        ImageView imageMenu = (ImageView) findViewById(R.id.image_menu);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get(UserPrefUtils.ID);
                String taskOwnerName = userId.get(UserPrefUtils.NAME);
                ImageView mImageProfile = (ImageView) findViewById(R.id.img_profile);
                mImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),EditAccountActivity.class);
                        startActivity(i);
                    }
                });
                TextView mTextName =(TextView)findViewById(R.id.tv_nameProfile);
                mTextName.setText(taskOwnerName);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.nav_today:
                              /*  Toast.makeText(getApplicationContext(),"Work in Progress",Toast.LENGTH_SHORT).show();*/
                                Intent iToady = new Intent(getApplicationContext(), TodayTaskActivity.class);
                                startActivity(iToady);
                                finish();
                                break;
                            case R.id.nav_timeLine:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;
                           /* case R.id.nav_filter:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;*/
                            case R.id.nav_profile:
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get(UserPrefUtils.ID);
                                String name = userId.get(UserPrefUtils.NAME);
                                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                                Intent iprofile=new Intent(PriorityTaskActivity.this, EditAccountActivity.class);
                                iprofile.putExtra("id", id);
                                iprofile.putExtra("name", name);
                                iprofile.putExtra("email", accountEmail);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium=new Intent(PriorityTaskActivity.this, PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek= new Intent(PriorityTaskActivity.this, ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                ImageView imgeClose =(ImageView)findViewById(R.id.nav_close);
                imgeClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    }
                });
            }
        });
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayItems);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text_color);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorAccent));
               /* Object item = spinner.getItemAtPosition(position);
                projects = item.toString();*/
                switch ( position ) {
                    case 0:
                        Toast.makeText( getApplicationContext(), "PriorityTask", Toast.LENGTH_SHORT ).show();
                        break;
                    case 1:
                        Intent i=new Intent(PriorityTaskActivity.this,OverDueTaskActivity.class);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mOverDueTaskRecyclerView =(RecyclerView)findViewById(R.id.re_overDueTask);
        mLayoutManager  = new LinearLayoutManager(getApplicationContext());
        mOverDueTaskRecyclerView.setLayoutManager(mLayoutManager);
        mOverDueTaskRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mOverDueTaskRecyclerView.setAdapter(mPriorityTaskAdapter);

        HashMap<String,String> userId = session.getUserDetails();
        String id= userId.get(UserPrefUtils.ID);
        System.out.println("userId"+ id);
        Call<PriortyTaskListResponse> call= ANApplications.getANApi().checkPriorityTaskList(id);
        call.enqueue(new Callback<PriortyTaskListResponse>() {
            @Override
            public void onResponse(Call<PriortyTaskListResponse> call, Response<PriortyTaskListResponse> response) {
                AndroidUtils.showProgress(false,mProgressView,mContentLayout);
                if (response.isSuccessful()){
                   // System.out.println("response1"+response.raw());
                    if (response.body().getSuccess().equals("true")){
                        //System.out.println("response2"+response.body().getTask_records());
                       // setPriortyList(response.body().getTask_records());
                    }else{
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");

                }
            }
            @Override
            public void onFailure(Call<PriortyTaskListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });

    }
   /* private void setPriortyList(List<PriorityTaskListRecords> task_recordsList) {
        if(task_recordsList.size() > 0){
            for (int i=0;task_recordsList.size() > i; i++){
                Ta priorityTaskListRecordsList = task_recordsList.get(i);
                System.out.println("priorityTaskListRecordsList"+ priorityTaskListRecordsList);
                PriorityTaskListRecords priorityTaskListRecords1 = new PriorityTaskListRecords();
                priorityTaskListRecords1.setName(priorityTaskListRecordsList.getName());
                priorityTaskListRecords1.setDue_date(priorityTaskListRecordsList.getDue_date());
                priorityTaskListRecords1.setPriority(priorityTaskListRecordsList.getPriority());
                priorityTaskListRecords.add(priorityTaskListRecords1);
            }
            mOverDueTaskRecyclerView.setAdapter(new PriorityTaskAdapter(priorityTaskListRecords, R.layout.custom_priority_task, getApplicationContext()));
        }
    }*/
}
