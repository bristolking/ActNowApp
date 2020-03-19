package com.actnow.android.activities;

import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.monthly.MonthlyTaskListActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.insights.InsightsChart;
import com.actnow.android.activities.taskspinner.PriorityTaskActivity;
import com.actnow.android.utils.UserPrefUtils;

public class MeActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView, mContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        session.checkLogin();
        setContentView(R.layout.activity_me);
        initializeViews();
    }

    private void initializeViews() {
        appHeader();
        appFooter();
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
    }

    private void appHeader() {
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2);
       /* TextView btnLink3 = (TextView) findViewById(R.id.btn_link_3);*/
        TextView btnLink4 = (TextView) findViewById(R.id.btn_link_4);

        btnLink1.setText("Daily");
        btnLink2.setText("Weekly");
        btnLink2.setTextColor(getResources().getColor(R.color.colorAccent));
        /*btnLink3.setText("Monthly");*/
        btnLink4.setText("Yearly");

        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MeActivity.this, MonthlyTaskListActivity.class);
                startActivity(i);
            }
        });
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
                session.logoutUser();
            }
        });
    }

    private void appFooter() {
        View btnMe = findViewById(R.id.btn_me);
        View btnProject = findViewById(R.id.btn_projects);
        btnProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityProjects();
            }
        });
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

        ImageView imgMe = (ImageView) findViewById(R.id.img_today);
        imgMe.setImageResource(R.drawable.ic_today_red);
        TextView txtMe = (TextView) findViewById(R.id.txt_today);
        txtMe.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void activityProjects() {
      /*  Intent i = new Intent(getApplicationContext(), ViewProjectsActivity.class);
        startActivity(i);*/
        Intent i = new Intent(getApplicationContext(), ProjectFooterActivity.class);
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
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);*/
        Intent i = new Intent(getApplicationContext(), InsightsChart.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
}
