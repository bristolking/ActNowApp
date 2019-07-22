package com.actnow.android.activities.individuals;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ApprovalsActivity;
import com.actnow.android.activities.MeActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.monthly.MonthlyTaskListActivity;
import com.actnow.android.activities.ProjectFooterActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.settings.SettingsActivity;
//import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.activities.taskchart.DailyTaskChartActivity;
import com.actnow.android.activities.taskspinner.PriorityTaskActivity;
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

public class ViewIndividualsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mIndivivalLayoutManager;
    private ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList = new ArrayList<OrgnUserRecordsCheckBox>();
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    EditText mIndividualQucikSearch;
    Button mIndividualButtonAdavancedSearch;
    ImageView mIndividualImageBulbTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_view_individuals);
        appHeader();
        initializeViews();
        appFooter();
    }
    private void appHeader() {
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2);
        btnLink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iApproval = new Intent(getApplicationContext(), ApprovalsActivity.class);
                startActivity(iApproval);
                finish();

            }
        });
        TextView btnLink3 = (TextView) findViewById(R.id.btn_link_3);
        btnLink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
                startActivity(iTimeLine);
                finish();
            }
        });
        TextView btnLink4 = (TextView) findViewById(R.id.btn_link_4);

        btnLink1.setText("Individual");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));
        btnLink2.setText("Approvals");
        btnLink3.setText("Time Log");
        btnLink4.setVisibility(View.GONE);

        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ViewIndividualsActivity.this, MonthlyTaskListActivity.class);
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
                // session.logoutUser();
                Intent i =new Intent(ViewIndividualsActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mIndividualImageBulbTask = findViewById(R.id.image_bulbIndividuals);
        mIndividualImageBulbTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewIndividualsActivity.this, ViewIdeasActivity.class);
                startActivity(i);
            }
        });
        mIndividualQucikSearch = findViewById(R.id.edit_searchIndividuals);
        mIndividualButtonAdavancedSearch = findViewById(R.id.button_searchIndividuals);

        mRecyclerView = (RecyclerView) findViewById(R.id.Individuals_recyclerView);
        mIndivivalLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mIndivivalLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(orgnUserRecordsCheckBoxList, R.layout.individual_check, getApplicationContext());
        mRecyclerView.setAdapter(checkBoxAdapter);

        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        System.out.println("id"+id);

        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse(id);
        call.enqueue(new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                System.out.println("response" + response.raw());
                if (response.isSuccessful()) {
                    System.out.println("r" + response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        System.out.println("data" + response.body().getSuccess());
                         setLoadCheckBox(response.body().getOrgn_users_records());
                    }else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<CheckBoxResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }
    private void setLoadCheckBox(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        System.out.println("output" + orgn_users_records);
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get(i);
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setName(orgnUserRecordsCheckBox.getName());
                orgnUserRecordsCheckBox1.setId(orgnUserRecordsCheckBox.getId());
                orgnUserRecordsCheckBox1.setEmail(orgnUserRecordsCheckBox.getEmail());
                orgnUserRecordsCheckBox1.setMobile_number(orgnUserRecordsCheckBox.getMobile_number());
                orgnUserRecordsCheckBox1.setProvider_id(orgnUserRecordsCheckBox.getProvider_id());
                orgnUserRecordsCheckBox1.setProvider_name(orgnUserRecordsCheckBox.getProvider_name());
                orgnUserRecordsCheckBox1.setOrgn_code(orgnUserRecordsCheckBox.getOrgn_code());
                orgnUserRecordsCheckBox1.setPassword(orgnUserRecordsCheckBox.getPassword());
                orgnUserRecordsCheckBox1.setImage_path(orgnUserRecordsCheckBox.getImage_path());
                orgnUserRecordsCheckBox1.setUser_type(orgnUserRecordsCheckBox.getUser_type());
                orgnUserRecordsCheckBox1.setOtp(orgnUserRecordsCheckBox.getOtp());
                orgnUserRecordsCheckBox1.setStatus(orgnUserRecordsCheckBox.getStatus());
                orgnUserRecordsCheckBox1.setEmail_verified_at(orgnUserRecordsCheckBox.getEmail_verified_at());
                orgnUserRecordsCheckBox1.setVerified(orgnUserRecordsCheckBox.getVerified());
                orgnUserRecordsCheckBox1.setRemember_token(orgnUserRecordsCheckBox.getRemember_token());
                orgnUserRecordsCheckBox1.setRefresh_token(orgnUserRecordsCheckBox.getRefresh_token());
                orgnUserRecordsCheckBox1.setCreated_at(orgnUserRecordsCheckBox.getCreated_at());
                orgnUserRecordsCheckBox1.setUpdated_at(orgnUserRecordsCheckBox.getUpdated_at());
                orgnUserRecordsCheckBox1.setOther_orgns(orgnUserRecordsCheckBox.getOther_orgns());
                orgnUserRecordsCheckBoxList.add(orgnUserRecordsCheckBox1);
            }
            mRecyclerView.setAdapter(new CheckBoxAdapter(orgnUserRecordsCheckBoxList, R.layout.individual_check, getApplicationContext()));
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
        btnProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityProject();
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
        ImageView imgProject = (ImageView) findViewById(R.id.img_individuals);
        imgProject.setImageResource(R.drawable.ic_individuals_red);
        TextView txtIndividual = (TextView) findViewById(R.id.txt_individuals);
        txtIndividual.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void activityMe() {
        Intent i = new Intent(getApplicationContext(), MeActivity.class);
        startActivity(i);

    }

    private void activityProject() {
        Intent i = new Intent(getApplicationContext(), ProjectFooterActivity.class);
        startActivity(i);

    }

    private void activityTasks() {
        /*Intent i = new Intent(getApplicationContext(), ViewTasksActivity.class);
        startActivity(i);*/
        Intent i = new Intent(getApplicationContext(), PriorityTaskActivity.class);
        startActivity(i);

    }

    private void activityIndividuals() {
        Intent i = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
        startActivity(i);

    }
    private void activityInsights() {
        /*Intent i = new Intent(getApplicationContext(), PersonalActivity.class);
        startActivity(i);*/
        Intent i = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
