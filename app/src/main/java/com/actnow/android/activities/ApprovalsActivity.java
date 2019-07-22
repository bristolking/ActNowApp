package com.actnow.android.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.monthly.ProjectMonthlyActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.taskchart.DailyTaskChartActivity;
import com.actnow.android.activities.taskspinner.PriorityTaskActivity;
import com.actnow.android.utils.UserPrefUtils;

public class ApprovalsActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView, mContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_approvals);
        appHeader();
        appFooter();
    }
    private void appHeader() {
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2);
        btnLink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mContentLayout, "Selected the Approval!", Snackbar.LENGTH_SHORT).show();


            }
        });
        TextView btnLink3 = (TextView) findViewById(R.id.btn_link_3);
        btnLink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent iTimeLog = new Intent(getApplicationContext(),TimeLineActivity.class);
                startActivity(iTimeLog);
                finish();
            }
        });
        TextView btnLink4 = (TextView) findViewById(R.id.btn_link_4);
        btnLink1.setText("Tasks");
        btnLink2.setText("Approvals");
        btnLink2.setTextColor(getResources().getColor(R.color.colorAccent));
        btnLink3.setText("Time Log");
        btnLink4.setVisibility(View.GONE);
        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), ProjectMonthlyActivity.class);
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
                Intent i =new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
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

       /* ImageView imgProject = (ImageView) findViewById(R.id.img_projects);
        imgProject.setImageResource(R.drawable.ic_projects_red);
        TextView txtProject = (TextView) findViewById(R.id.txt_projects);
        txtProject.setTextColor(getResources().getColor(R.color.colorAccent));*/
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
        Intent i = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
}


