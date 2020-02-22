package com.actnow.android.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.actnow.android.R;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import static android.view.View.GONE;



public class TodayTaskActivity extends AppCompatActivity {
    final Context context = this;
    UserPrefUtils session;
    View mProgressView, mContentLayout;

    EditText mTaskQucikSearch;
    Button mButtonAdavancedSearch;
    ImageView mImageBulbTask;
    FloatingActionButton fabTodayTask;

    private String selectedType = "";
    TextView mTaskName;
    String task_code;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_today_task );
        appFooter();
        appHeaderTwo();
        initializeViews();
        checkPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE );
        checkPermission( Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE );

    }
    private void appHeaderTwo() {
        ImageView imgeBack = (ImageView) findViewById( R.id.image_back_two );
        imgeBack.setVisibility( GONE );
        TextView btnLink1 = (TextView) findViewById( R.id.btn_link_1_two );
        TextView btnLink2 = (TextView) findViewById( R.id.btn_link_2_two );
        btnLink2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iApproval = new Intent( getApplicationContext(), ApprovalsActivity.class );
                startActivity( iApproval );
            }
        } );
        btnLink1.setText( "Today" );
        btnLink1.setTextColor( getResources().getColor( R.color.colorAccent ) );
        btnLink2.setText( "Approvals" );
        ImageView btnCalendar = (ImageView) findViewById( R.id.btn_insightsrAppHeaderTwo );
        btnCalendar.setVisibility( GONE );
        ImageView btnNotifications = (ImageView) findViewById( R.id.btn_notificationsAppHeaderTwo );
        btnNotifications.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "Work in progress!", Toast.LENGTH_SHORT ).show();
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
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Toast.makeText( getApplicationContext(), "Selected The TASKS", Toast.LENGTH_SHORT ).show();
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
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_todayTask );
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

        HashMap<String, String> userId = session.getUserDetails();
        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
        if (orgn_code == null || TextUtils.isEmpty( orgn_code )) {
            Intent i = new Intent( TodayTaskActivity.this, OrngActivity.class );
            startActivity( i );
        }

        mProgressView = findViewById( R.id.progress_bar );
        mContentLayout = findViewById( R.id.content_layout );


        fabTodayTask = findViewById( R.id.fab_todayTaskadd );
        fabTodayTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get( UserPrefUtils.ID );
                String taskOwnerName = userId.get( UserPrefUtils.NAME );
                Intent i = new Intent(getApplicationContext(), ViewTasksActivity.class );
                i.putExtra( "id", id );
                i.putExtra( "taskOwnerName", taskOwnerName );
                startActivity( i );
            }
        } );
        mImageBulbTask = findViewById( R.id.image_bulbTask );
        mImageBulbTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), ViewIdeasActivity.class );
                startActivity( i );
            }
        } );

        mTaskQucikSearch = findViewById( R.id.edit_searchTask );
        mTaskQucikSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               /* if(mThisweekrecyclerView.getVisibility() != View.VISIBLE)
                    mThisweekrecyclerView.setVisibility( View.VISIBLE );*/
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //filter(editable.toString());

            }
        } );
        mButtonAdavancedSearch = findViewById( R.id.button_searchTask );
        mButtonAdavancedSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), AdvancedSearchActivity.class );
                startActivity( i );
            }
        } );
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
        ImageView imgProject = (ImageView) findViewById( R.id.img_today );
        imgProject.setImageResource( R.drawable.ic_today_red );
        TextView txtProject = (TextView) findViewById( R.id.txt_today );
        txtProject.setTextColor( getResources().getColor( R.color.colorAccent ) );
    }

    private void activityToady() {
        Toast.makeText( getApplicationContext(), "Seleted the ToadyTask", Toast.LENGTH_LONG ).show();
    }

    private void activityProject() {
        Intent i = new Intent( getApplicationContext(), ProjectFooterActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    private void activityTasks() {
        Intent i = new Intent( getApplicationContext(), TaskAddListActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    private void activityIndividuals() {
        Intent i = new Intent( getApplicationContext(), ViewIndividualsActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    private void activityInsights() {
        Intent i = new Intent( getApplicationContext(), DailyTaskChartActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }


    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission( TodayTaskActivity.this, permission ) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions( TodayTaskActivity.this,
                    new String[]{permission},
                    requestCode );
        } else {
            //Toast.makeText( TodayTaskActivity.this, "Permission already granted", Toast.LENGTH_SHORT ).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        if (requestCode == CAMERA_PERMISSION_CODE) {
            // Checking whether user granted the permission or not.
           /* if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(TodayTaskActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(TodayTaskActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }*/
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText( TodayTaskActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( TodayTaskActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT ).show();
            }
        }
    }


}
