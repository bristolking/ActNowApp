package com.actnow.android.activities.individuals;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
//import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.CheckBoxAdapter;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class ViewIndividualsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mIndivivalLayoutManager;
    private ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList = new ArrayList<OrgnUserRecordsCheckBox>();
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    EditText mIndividualQucikSearch;
    Button mIndividualButtonAdavancedSearch;
    ImageView mIndividualImageBulbTask;

    String id;
    String taskOwnerName;

    final Context context = this;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_view_individuals);
        appHeaderTwo();
        initializeViews();
        appFooter();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = (String) b.get("id");
            taskOwnerName = (String) b.get("taskOwnerName");
            System.out.println("passsed" + taskOwnerName + id);
        }
    }

    private void  appHeaderTwo(){
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back_two);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1_two);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2_two);
        btnLink2.setVisibility(View.GONE);
        btnLink1.setText("Individuals");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));
        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendarAppHeaderTwo);
        btnCalendar.setVisibility(GONE);
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
                HashMap<String, String> userId = session.getUserDetails();
                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                i.putExtra("email", accountEmail);
                startActivity(i);
                finish();
            }
        });
        ImageView btnMenu = (ImageView) findViewById(R.id.img_menuTopTwo);
        btnMenu.setOnClickListener(new View.OnClickListener() {
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
                TextView mTextName = (TextView) findViewById(R.id.tv_nameProfile);
                mTextName.setText(taskOwnerName);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToady = new Intent(getApplicationContext(), TodayTaskActivity.class);
                                startActivity(iToady);
                                finish();
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
                                startActivity(iTimeLine);
                                break;
                            case R.id.nav_filter:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_profile:
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get(UserPrefUtils.ID);
                                String name = userId.get(UserPrefUtils.NAME);
                                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                                Intent iprofile = new Intent(getApplicationContext(), EditAccountActivity.class);
                                iprofile.putExtra("id", id);
                                iprofile.putExtra("name", name);
                                iprofile.putExtra("email", accountEmail);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium = new Intent(getApplicationContext(), PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent(getApplicationContext(), ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_individual);
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


    }

    private void initializeViews() {
     /*   AssetManager am = context.getApplicationContext().getAssets();
        typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "abc.ttf"));
        mIndividualQucikSearch.setTypeface(typeface);
        mIndividualButtonAdavancedSearch.setTypeface(typeface);
*/
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
        mIndividualQucikSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Work in Progress!",Toast.LENGTH_LONG).show();

            }
        });
        mIndividualButtonAdavancedSearch = findViewById(R.id.button_searchIndividuals);
        mIndividualButtonAdavancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Work in Progress!",Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.Individuals_recyclerView);
        mIndivivalLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mIndivivalLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(orgnUserRecordsCheckBoxList, R.layout.individual_check, getApplicationContext());
        mRecyclerView.setAdapter(checkBoxAdapter);

        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        System.out.println("id" + id);

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
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
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
                activityToady();
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
    private void activityToady() {
        Intent i = new Intent(getApplicationContext(), TodayTaskActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
    private void activityProject() {
        Intent i = new Intent(getApplicationContext(), ProjectFooterActivity.class);
        startActivity(i);
    }

    private void activityTasks() {
        Intent i = new Intent(getApplicationContext(), TaskAddListActivity.class);
        startActivity(i);
    }
    private void activityIndividuals() {
        Intent i = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
        startActivity(i);
    }

    private void activityInsights() {
        Intent i = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
