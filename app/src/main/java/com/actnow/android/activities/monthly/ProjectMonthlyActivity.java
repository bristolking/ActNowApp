package com.actnow.android.activities.monthly;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.MeActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.taskchart.DailyTaskChartActivity;
import com.actnow.android.activities.taskspinner.PriorityTaskActivity;
import com.actnow.android.adapter.ProjectFooterAdapter;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectMonthlyActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    RecyclerView mRecyclerViewProjectListMonthly;
    ProjectFooterAdapter mProjectFooterAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList = new ArrayList<ProjectListResponseRecords>();

    private String currentMonth;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_project_monthly);
        initializeViews();

        appHeader();
        appFooter();
    }

    private void appHeader() {
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2);
        btnLink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT).show();
            }
        });
        TextView btnLink3 = (TextView) findViewById(R.id.btn_link_3);
        btnLink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT).show();
            }
        });
        TextView btnLink4 = (TextView) findViewById(R.id.btn_link_4);

        btnLink1.setText("Projects");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));
        btnLink2.setText("Approvals");
        btnLink3.setText("Time Log");
        btnLink4.setVisibility(View.GONE);
        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendar);
        btnCalendar.setImageResource(R.drawable.ic_calendar_red);
        ImageView btnNotifications = (ImageView) findViewById(R.id.btn_notifications);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT).show();
            }
        });
        ImageView btnSettings = (ImageView) findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // session.logoutUser();
                Intent i = new Intent(ProjectMonthlyActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
    }

    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        MaterialCalendarView materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        Calendar cal = Calendar.getInstance();
        //System.out.println(new SimpleDateFormat("MM").format(cal.getTime()));
        currentMonth = new SimpleDateFormat("MM").format(cal.getTime());
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
              /*  String msg = " Selected month is" + "-" + date.getMonth();
                String seletedMonth = String.valueOf(date.getMonth() + 1);
                currentMonth = "0"+seletedMonth;
                HashMap<String, String> userId = session.getUserDetails();
                id = userId.get(UserPrefUtils.ID);
                afterselectedMonth(id);
                System.out.println("id"+id);*/
            }

        });
        mRecyclerViewProjectListMonthly = findViewById(R.id.recyclerViewMonthlyProjectList);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewProjectListMonthly.setLayoutManager(mLayoutManager);
        mRecyclerViewProjectListMonthly.setItemAnimator(new DefaultItemAnimator());
        mProjectFooterAdapter = new ProjectFooterAdapter(projectListResponseRecordsArrayList, R.layout.custom_project_footer, getApplicationContext());
        mRecyclerViewProjectListMonthly.setAdapter(mProjectFooterAdapter);
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse(id);
        call.enqueue(new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        setProjectFooterList(response.body().getProject_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<ProjectListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }

   /* private void afterselectedMonth(String id) {
        System.out.println("data"+id);
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse(id);
        call.enqueue(new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                System.out.println("s"+response.raw());
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    System.out.println("p" + response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        System.out.println("me" + response.body().getProject_records());
                        setProjectFooterList(response.body().getProject_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<ProjectListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }*/
    private void setProjectFooterList(List<ProjectListResponseRecords> project_records) {
        if (project_records.size() > 0) {
            for (int i = 0; project_records.size() > i; i++) {
                ProjectListResponseRecords projectListResponseRecords = project_records.get(i);
                ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
                projectListResponseRecords1.setName(projectListResponseRecords.getName());
                projectListResponseRecords1.setDue_date(projectListResponseRecords.getDue_date());
                projectListResponseRecordsArrayList.add(projectListResponseRecords1);
               /* String date = projectListResponseRecords.getDue_date();
                String[] dateArray = date.split(" ");
                String[] finalDateArray = dateArray[0].split("-");
                String month = finalDateArray[1];
                if (currentMonth.equals(month)) {
                    projectListResponseRecordsArrayList.add(projectListResponseRecords1);
                }*/
            }
            mRecyclerViewProjectListMonthly.setAdapter(new ProjectFooterAdapter(projectListResponseRecordsArrayList, R.layout.custom_project_footer, getApplicationContext()));
        }
    }

    private void appFooter() {
        View btnMe = findViewById(R.id.btn_me);
        btnMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMe();
            }
        });
        View btnProject = findViewById(R.id.btn_projects);
        View btnTask = findViewById(R.id.btn_task);
        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityTasks();
            }
        });
        View btnIndividuals = findViewById(R.id.btn_individuals);
        btnIndividuals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityIndividuals();
            }
        });
        View btnInsights = findViewById(R.id.btn_insights);
        btnInsights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityInsights();
            }
        });

        ImageView imgProject = (ImageView) findViewById(R.id.img_projects);
        imgProject.setImageResource(R.drawable.ic_projects_red);
        TextView txtProject = (TextView) findViewById(R.id.txt_projects);
        txtProject.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void activityMe() {
        Intent i = new Intent(getApplicationContext(), MeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);

    }

    private void activityTasks() {
        /*Intent i = new Intent(getApplicationContext(), ViewTasksActivity.class);
        startActivity(i);*/
        Intent i = new Intent(getApplicationContext(), PriorityTaskActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityIndividuals() {
        Intent i = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityInsights() {
       /* Intent i = new Intent(getApplicationContext(), PersonalActivity.class);
        startActivity(i);*/
        Intent i = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
