package com.actnow.android.activities.insights;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.sdk.responses.IndividualMembersReponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.activeandroid.Cache.getContext;

public class IndividualInsighsGrapghActivity extends AppCompatActivity {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    BarChart barChart;

    TextView btnLink1;
    String nameOne;
    String color ;

    ArrayList<IndividualMembersReponse> individualMembersReponseArrayList = new ArrayList<>();
    ArrayList<BarEntry> x;
    ArrayList<String> y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils( getApplicationContext() );
        setContentView(R.layout.activity_individual_insighs_grapgh);
        appHeaderTwo();
        initializeViews();
        appFooter();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            nameOne =(String)b.get("nameInsights");
            btnLink1.setText(nameOne);
            color =(String)b.get("colorInsights");
        }

    }
    private void appHeaderTwo() {
        ImageView imgeBack = (ImageView) findViewById( R.id.image_back_two );
        imgeBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        btnLink1 = (TextView) findViewById( R.id.btn_link_1_two );
        TextView btnLink2 = (TextView) findViewById( R.id.btn_link_2_two );
        btnLink2.setVisibility( GONE );
        //btnLink1.setText(name );
        btnLink1.setTextColor( getResources().getColor( R.color.colorAccent ) );
        ImageView btnCalendar = (ImageView) findViewById( R.id.btn_insightsrAppHeaderTwo );
        btnCalendar.setVisibility( GONE );
        ImageView btnNotifications = (ImageView) findViewById( R.id.btn_notificationsAppHeaderTwo );
        btnNotifications.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make( mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT ).show();
            }
        } );
        ImageView btnSettings = (ImageView) findViewById( R.id.btn_settingsAppHeaderTwo );
        btnSettings.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String accountEmail = userId.get( UserPrefUtils.EMAIL );
                Intent i = new Intent( getApplicationContext(), SettingsActivity.class );
                i.putExtra( "email", accountEmail );
                startActivity( i );
                finish();
            }
        } );
        ImageView btnMenu = (ImageView) findViewById( R.id.img_menuTopTwo );
        btnMenu.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get( UserPrefUtils.ID );
                String taskOwnerName = userId.get( UserPrefUtils.NAME );
                String email = userId.get( UserPrefUtils.EMAIL );
                ImageView mImageProfile = (ImageView) findViewById( R.id.img_profile );
                String img = userId.get( UserPrefUtils.IMAGEPATH );
                System.out.println( "img" + img );
                Glide.with( getApplicationContext() )
                        .load( img )
                        .centerCrop()
                        .placeholder( R.drawable.placeholder )
                        .error( R.drawable.placeholder )
                        .into( mImageProfile );
                mImageProfile.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent( getApplicationContext(), EditAccountActivity.class );
                        startActivity( i );
                    }
                } );
                TextView mTextName = (TextView) findViewById( R.id.tv_nameProfile );
                mTextName.setText( taskOwnerName );
                TextView mTextEmail = (TextView) findViewById( R.id.tv_emailProfile );
                mTextEmail.setText( email );
                navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToday = new Intent( getApplicationContext(), TodayTaskActivity.class );
                                startActivity( iToday );
                                break;
                            case R.id.nav_idea:
                                Intent iIdea = new Intent( getApplicationContext(), ViewIdeasActivity.class );
                                startActivity( iIdea );
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent( getApplicationContext(), ThisWeekActivity.class );
                                startActivity( ithisweek );
                                break;
                            case R.id.nav_taskfilter:
                                Intent iTaskfilter = new Intent( getApplicationContext(), TaskAddListActivity.class );
                                startActivity( iTaskfilter );
                                break;
                            case R.id.nav_project:
                                Intent iProjects = new Intent( getApplicationContext(), ProjectFooterActivity.class );
                                startActivity( iProjects );
                                break;
                            case R.id.nav_individuals:
                                Intent iIndividuals = new Intent( getApplicationContext(), ViewIndividualsActivity.class );
                                startActivity( iIndividuals );
                                break;
                            case R.id.nav_insights:
                                Intent iInsights = new Intent( getApplicationContext(), InsightsChart.class );
                                startActivity( iInsights );
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent( getApplicationContext(), TimeLineActivity.class );
                                startActivity( iTimeLine );
                                break;
                            case R.id.nav_profile:
                                Intent iprofile = new Intent( getApplicationContext(), AccountSettingActivity.class );
                                startActivity( iprofile );
                                break;
                            case R.id.nav_premium:
                                Intent ipremium = new Intent( getApplicationContext(), PremiumActivity.class );
                                startActivity( ipremium );
                                break;
                            case R.id.nav_logout:
                                session.logoutUser();
                                break;

                        }
                        return false;
                    }
                } );
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_IndividualInsightsBarGraph );
                if (drawer.isDrawerOpen( GravityCompat.START )) {
                } else {
                    drawer.openDrawer( GravityCompat.START );
                }
                ImageView imgeClose = (ImageView) findViewById( R.id.nav_close );
                imgeClose.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (drawer.isDrawerOpen( GravityCompat.START )) {
                            drawer.closeDrawer( GravityCompat.START );
                        } else {
                            drawer.openDrawer( GravityCompat.START );
                        }
                    }
                } );
            }
        } );
    }

    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        barChart = (BarChart) findViewById(R.id.individualinsightsbarchart);

        barChart.setDrawBarShadow( false );
        barChart.setDrawValueAboveBar( true );
        barChart.setMaxVisibleValueCount( 50 );
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground( true );
        apiCallIndividual();
    }
    private void apiCallIndividual() {
        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(UserPrefUtils.ID);
        Call<ResponseBody> responseBodyCall = ANApplications.getANApi().individualInsights(id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                System.out.println("SeverReponse" + response.raw());
                if (response.isSuccessful()) {
                    System.out.println("SeverReponse1" + response.raw());
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getString("success").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("members");
                                System.out.println("SeverReponse2" + jsonArray);
                                setIndividualInsightsList(jsonArray);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void setIndividualInsightsList(JSONArray jsonArray) {
        for (int i = 0; jsonArray.length() > i; i++) {
            IndividualMembersReponse individualMembersReponse = new IndividualMembersReponse();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String email = jsonObject.getString("email");
                String mobile_number = jsonObject.getString("mobile_number");
                String provider_id = jsonObject.getString("provider_id");
                String provider_name = jsonObject.getString("provider_name");
                String orgn_code = jsonObject.getString("orgn_code");
                String password = jsonObject.getString("password");
                String image_path = jsonObject.getString("image_path");
                String user_type = jsonObject.getString("user_type");
                String otp = jsonObject.getString("otp");
                String status = jsonObject.getString("status");
                String email_verified_at = jsonObject.getString("email_verified_at");
                String verified = jsonObject.getString("verified");
                String remember_token = jsonObject.getString("remember_token");
                String refresh_token = jsonObject.getString("refresh_token");
                String created_at = jsonObject.getString("created_at");
                String updated_at = jsonObject.getString("updated_at");
                String other_orgns = jsonObject.getString("other_orgns");
                String timezone = jsonObject.getString("timezone");
                String completed = jsonObject.getString("completed");
                String approval = jsonObject.getString("approval");
                String ongoing = jsonObject.getString("ongoing");
                String pending = jsonObject.getString("pending");

                individualMembersReponse.setId(id);
                individualMembersReponse.setName(name);
                individualMembersReponse.setEmail(email);
                individualMembersReponse.setMobile_number(mobile_number);
                individualMembersReponse.setProvider_id(provider_id);
                individualMembersReponse.setProvider_name(provider_name);
                individualMembersReponse.setOrgn_code(orgn_code);
                individualMembersReponse.setPassword(password);
                individualMembersReponse.setImage_path(image_path);
                individualMembersReponse.setUser_type(user_type);
                individualMembersReponse.setOtp(otp);
                individualMembersReponse.setStatus(status);
                individualMembersReponse.setEmail_verified_at(email_verified_at);
                individualMembersReponse.setVerified(verified);
                individualMembersReponse.setRemember_token(remember_token);
                individualMembersReponse.setRefresh_token(refresh_token);
                individualMembersReponse.setCreated_at(created_at);
                individualMembersReponse.setUpdated_at(updated_at);
                individualMembersReponse.setOther_orgns(other_orgns);
                individualMembersReponse.setTimezone(timezone);
                individualMembersReponse.setCompleted(completed);
                individualMembersReponse.setApproval(approval);
                individualMembersReponse.setOngoing(ongoing);
                individualMembersReponse.setPending(pending);
                if (name.equals(nameOne)) {
                    x = new ArrayList<BarEntry>();
                    x.add(new BarEntry(Integer.parseInt(completed), 0));
                    x.add(new BarEntry(Integer.parseInt(approval), 1));
                    x.add(new BarEntry(Integer.parseInt(ongoing), 2));
                    x.add(new BarEntry(Integer.parseInt(pending), 3));
                    y = new ArrayList<String>();
                    y.add("completed");
                    y.add("approval");
                    y.add("ongoing");
                    y.add("pending");
                    BarDataSet bardataset = new BarDataSet(x, "Data Set");
                    bardataset.setColors(new int[]{R.color.greenmilk, R.color.orngeccolr, R.color.buleMilk, R.color.colorAccent}, getContext());
                    BarData data = new BarData(y, bardataset);
                    barChart.setData(data);
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.animateY(5000);
                    individualMembersReponseArrayList.add(individualMembersReponse);
                    System.out.println("SeverGraphValues" +  name + completed + pending + ongoing + approval);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void appFooter() {
        /*View btnMe = findViewById(R.id.btn_me);
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
        ImageView imgProject = (ImageView) findViewById(R.id.img_insights);
        imgProject.setImageResource(R.drawable.ic_insight_red);
        TextView txtIndividual = (TextView) findViewById(R.id.txt_insights);
        txtIndividual.setTextColor(getResources().getColor(R.color.colorAccent));*/
        FloatingActionButton floatingActionButton =(FloatingActionButton)findViewById(R.id.fab_marignBottom);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityInsights();
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottomNavigationToday:
                        activityToady();
                        return true;
                    case R.id.bottomNavigationProjects:
                        activityProject();
                        return true;
                    case R.id.bottomNavigationTask:
                        activityTasks();
                        return true;
                    case R.id.bottomNavigationIndividuals:
                        activityIndividuals();
                        return true;

                }
                return false;
            }
        });
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
        Toast.makeText(getApplicationContext(), "selected Insights", Toast.LENGTH_SHORT).show();

    }

}
