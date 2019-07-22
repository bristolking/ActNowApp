package com.actnow.android.activities.ideas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.monthly.MonthlyTaskListActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewIdeasActivity extends AppCompatActivity {
    RecyclerView mTaskRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    TaskListAdapter mTaskListAdapter;
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    FloatingActionButton fabIdea;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_view_ideas);
        appHeader();
        initializeViews();

    }
    private void appHeader() {
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1);
        btnLink1.setText("Ideas");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));

        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendarAppHeaderTwo);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ViewIdeasActivity.this, MonthlyTaskListActivity.class);
                startActivity(i);
            }
        });
        ImageView btnNotifications = (ImageView) findViewById(R.id.btn_notificationsAppHeaderTwo);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT).show();
            }
        });
        ImageView btnSettings = (ImageView) findViewById(R.id.btn_settingsAppHeaderTwo);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // session.logoutUser();
                Intent i =new Intent(ViewIdeasActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        fabIdea = findViewById(R.id.fab_idea);
        fabIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.remainder);
                CalendarView calendarView = (CalendarView) dialog.findViewById(R.id.calendarView);
                final TextView tv_raminderDate = (TextView)dialog.findViewById(R.id.tv_popreminderDate);
                TextView tv_timedate = (TextView)dialog.findViewById(R.id.tv_popreminderTime);
                tv_timedate.setVisibility(View.GONE);
                if (calendarView != null) {
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
                            //String msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year;
                            String msg="Selected date is" + (month +1)+ "/"+ dayOfMonth+ "/"+year;
                            Toast.makeText(ViewIdeasActivity.this, msg, Toast.LENGTH_SHORT).show();
                            tv_raminderDate.setText(dayOfMonth + "/" + month+ "/" + year );
                        }
                    });
                }
                dialog.show();
            }
        });
        mTaskRecylcerView = findViewById(R.id.idea_recyclerView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mTaskRecylcerView.setLayoutManager(mLayoutManager);
        mTaskRecylcerView.setItemAnimator(new DefaultItemAnimator());
        mTaskListAdapter = new TaskListAdapter(taskListRecordsArrayList, R.layout.custom_task_list, getApplicationContext());
        mTaskRecylcerView.setAdapter(mTaskListAdapter);
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse(id);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                //System.out.println("api" + response.raw());
                if (response.isSuccessful()) {
                    //System.out.println("url" + response.raw());
                    if (response.body().getSuccess().equals("true")) {
                      //  System.out.println("data" + response.body().getSuccess());
                        setProjectFooterList(response.body().getTask_records());
                    }else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
            }
        });
    }

    private void setProjectFooterList(List<TaskListRecords> taskListRecordsList) {
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get(i);
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName(taskListRecords.getName());
               // taskListRecords1.setCreated_date(taskListRecords.getCreated_date());
                taskListRecords1.setRemindars_count(taskListRecords.getRemindars_count());
                taskListRecordsArrayList.add(taskListRecords1);
            }
            mTaskRecylcerView.setAdapter(new TaskListAdapter(taskListRecordsArrayList, R.layout.custom_task_list, getApplicationContext()));

        }
    }
    public void onBackPressed() {
        super.onBackPressed();
    }

}
