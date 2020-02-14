package com.actnow.android.activities.insights;

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
import com.actnow.android.adapter.ApprovalAdapter;
import com.actnow.android.adapter.ProjectInsightsAdapter;
import com.actnow.android.sdk.responses.ProjectInsightsRecords;
import com.actnow.android.sdk.responses.ProjectInsightsReponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class ProjectInsightsActivity extends AppCompatActivity {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    ProjectInsightsAdapter mProjectInsightsAdapter;
    RecyclerView mRecyclerViewProjectInsights;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<ProjectInsightsRecords> projectInsightsRecordsArrayList = new ArrayList<ProjectInsightsRecords>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_project_insights );
        appHeaderTwo();
        initializeViews();
        appFooter();
    }

    private void appHeaderTwo() {
        ImageView imgeBack = (ImageView) findViewById( R.id.image_back_two );
        imgeBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        TextView btnLink1 = (TextView) findViewById( R.id.btn_link_1_two );
        TextView btnLink2 = (TextView) findViewById( R.id.btn_link_2_two );
        btnLink2.setVisibility( GONE );
        btnLink1.setText( "Projects Insights" );
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
                                Toast.makeText( getApplicationContext(), "Selected The Projects", Toast.LENGTH_SHORT ).show();
                                break;
                            case R.id.nav_individuals:
                                Intent iIndividuals = new Intent( getApplicationContext(), ViewIndividualsActivity.class );
                                startActivity( iIndividuals );
                                break;
                            case R.id.nav_insights:
                                Intent iInsights = new Intent( getApplicationContext(), DailyTaskChartActivity.class );
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
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_projectsInsights );
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
        mProgressView = findViewById( R.id.progress_bar );
        mContentLayout = findViewById( R.id.content_layout );

        mRecyclerViewProjectInsights = (RecyclerView) findViewById( R.id.projectInsights_recyclerView );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mRecyclerViewProjectInsights.setLayoutManager( mLayoutManager );
        mRecyclerViewProjectInsights.setItemAnimator( new DefaultItemAnimator() );
        mProjectInsightsAdapter = new ProjectInsightsAdapter( projectInsightsRecordsArrayList, R.layout.custom_project_insights, getApplicationContext() );
        mRecyclerViewProjectInsights.setAdapter( mProjectInsightsAdapter );

        apiCallProjectInsights();


    }

    private void apiCallProjectInsights() {
        HashMap<String, String> user = session.getUserDetails();
        String id = user.get( UserPrefUtils.ID );
        String orngcode = user.get( UserPrefUtils.ORGANIZATIONNAME );
        Call<ProjectInsightsReponse> callprojects = ANApplications.getANApi().projectInsightsReponse( id );
        callprojects.enqueue( new Callback<ProjectInsightsReponse>() {
            @Override
            public void onResponse(Call<ProjectInsightsReponse> call, Response<ProjectInsightsReponse> response) {
                System.out.println( "severReponse" + response.raw() );
                if (response.isSuccessful()) {
                    System.out.println( "severReponse1" + response.raw() );
                    if (response.body().getSuccess().equals( "true" )) {
                        System.out.println( "severReponse2" + response.body().getSuccess() );
                        setProjectInsights( response.body().getProjectInsightsRecords() );

                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();

                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }

            }
            @Override
            public void onFailure(Call<ProjectInsightsReponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );

            }
        } );

    }

    private void setProjectInsights(List<ProjectInsightsRecords> projectInsightsRecords) {
        if (projectInsightsRecords.size() > 0){
            for (int i=0; projectInsightsRecords.size() >i; i++){
                ProjectInsightsRecords projectInsightsRecords1 = projectInsightsRecordsArrayList.get(i);
                ProjectInsightsRecords  projectInsightsRecords2 = new ProjectInsightsRecords();
                projectInsightsRecords2.setName( projectInsightsRecords1.getName());
                projectInsightsRecords2.setApproval( projectInsightsRecords1.getApproval());
                projectInsightsRecords2.setCompleted(projectInsightsRecords1.getCompleted());
                projectInsightsRecords2.setPending( projectInsightsRecords1.getPending());
                projectInsightsRecords2.setOngoing(projectInsightsRecords1.getOngoing());
                projectInsightsRecords2.setColor(projectInsightsRecords1.getColor());
                projectInsightsRecordsArrayList.add( projectInsightsRecords2);

            }

        }
        mRecyclerViewProjectInsights.setAdapter( mProjectInsightsAdapter );
    }

    private void appFooter() {
        View btnMe = findViewById( R.id.btn_me );
        btnMe.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityToady();
            }
        } );
        View btnProject = findViewById( R.id.btn_projects );
        btnProject.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityProject();
            }
        } );
        View btnTask = findViewById( R.id.btn_task );
        btnTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityTasks();
            }
        } );
        View btnIndividuals = findViewById( R.id.btn_individuals );
        btnIndividuals.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityIndividuals();
            }
        } );
        View btnInsights = findViewById( R.id.btn_insights );
        btnInsights.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityInsights();
            }
        } );
        ImageView imgProject = (ImageView) findViewById( R.id.img_insights );
        imgProject.setImageResource( R.drawable.ic_insight_red );
        TextView txtIndividual = (TextView) findViewById( R.id.txt_insights );
        txtIndividual.setTextColor( getResources().getColor( R.color.colorAccent ) );
    }

    private void activityToady() {
        Intent i = new Intent( getApplicationContext(), TodayTaskActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    private void activityProject() {
        Intent i = new Intent( getApplicationContext(), ProjectFooterActivity.class );
        startActivity( i );

    }

    private void activityTasks() {
        Intent i = new Intent( getApplicationContext(), TaskAddListActivity.class );
        startActivity( i );

    }

    private void activityIndividuals() {
        Intent i = new Intent( getApplicationContext(), ViewIndividualsActivity.class );
        startActivity( i );
    }

    private void activityInsights() {
        Toast.makeText( getApplicationContext(), "selected Insights", Toast.LENGTH_SHORT ).show();

    }
}
