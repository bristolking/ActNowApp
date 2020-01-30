package com.actnow.android.activities.projects;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.view.MenuItem;

import android.view.View;


import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.databse.ProjectDBHelper;
import com.actnow.android.sdk.responses.ProjectAddResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;


public class ViewProjectsActivity extends AppCompatActivity {
    EditText mProjectName, mProjectOwnerName;
    View mProjectColor;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    final Context context = this;
    String id;
    String projectName;
    String projectOwnerName;
    ImageView mImgeCircleColor;
    TextView mColorCodePoject;
    private int currentBackgroundColor = 0xffffffff;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_view_projects );
        initializeViews();
        header();
        footer();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = (String) b.get( "id" );
            projectOwnerName = (String) b.get( "projectOwnerName" );
            mProjectOwnerName.setText( " " + projectOwnerName );
            System.out.println( "passsed" + projectOwnerName + id );
        }
    }

    private void header() {
        ImageView imgeBack = (ImageView) findViewById( R.id.image_back );
        imgeBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        TextView tv_title = (TextView) findViewById( R.id.txt_title );
        tv_title.setText( "New Project Name" );
        Spinner spinner = (Spinner) findViewById( R.id.spinner );
        spinner.setVisibility( GONE );
        ImageView imageMenu = (ImageView) findViewById( R.id.image_menu );
        imageMenu.setOnClickListener( new View.OnClickListener() {
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
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layoutProjectView );
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
        mProjectName = findViewById( R.id.et_newProjectName );
        mProjectOwnerName = findViewById( R.id.et_newProjectOwnerName );

        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
        mColorCodePoject = (TextView) findViewById( R.id.tv_newProjectColorCode );
        mProjectColor = findViewById( R.id.liner_newPojectColor );
        mImgeCircleColor = findViewById( R.id.img_newProjectcolor );
        mProjectColor.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with( context )
                        .setTitle( "Choose colorWhite" )
                        .initialColor( currentBackgroundColor )
                        .wheelType( ColorPickerView.WHEEL_TYPE.FLOWER )
                        .density( 12 )
                        .setOnColorSelectedListener( new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                toast( "onColorSelected: 0x" + Integer.toHexString( selectedColor ) );
                                String color = Integer.toHexString( selectedColor );
                                mColorCodePoject.setText( color );
                                System.out.println( "color"+color );
                            }
                        } )
                        .setPositiveButton( "ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                changeBackgroundColor( selectedColor );
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder( "Color List:" );
                                        sb.append( "#" + Integer.toHexString( color ).toUpperCase() );
                                    }

                                    if (sb != null)
                                        Toast.makeText( getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT ).show();
                                }
                            }
                        } )
                        .setNegativeButton( "cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        } )
                        .showColorEdit( true )
                        .setColorEditTextColor( ContextCompat.getColor( ViewProjectsActivity.this, android.R.color.black ) )
                        .build()
                        .show();
            }
        } );
    }

    private void changeBackgroundColor(int selectedColor) {
        currentBackgroundColor = selectedColor;
        mImgeCircleColor.setBackgroundColor( selectedColor );
    }

    private void toast(String text) {
        Toast.makeText( this, text, Toast.LENGTH_SHORT ).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void attemptCreateProject() {
        mProjectName.setError( null );
        projectName = mProjectName.getText().toString();
        String color = mColorCodePoject.getText().toString();
        String mColor= color.substring(2);
        System.out.println( "mColor"+mColor);
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty( projectName )) {
            mProjectName.setError( getString( R.string.error_required ) );
            focusView = mProjectName;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            HashMap<String, String> userId = session.getUserDetails();
            String id = userId.get( UserPrefUtils.ID );
            //requestCrateTask(id, projectName, String.valueOf(individuvalArray).replace("[", "").replace("]", ""));
            requestCrateTask( id, projectName, mColor );
            System.out.println( "project" + id + projectName + color );
        }
    }

    private void requestCrateTask(String a, String b, String c) {
        System.out.println( "values" + a + b + c );
        Call<ProjectAddResponse> call = ANApplications.getANApi().checkProjectAddReponse( a, b, c );
        call.enqueue( new Callback<ProjectAddResponse>() {
            @Override
            public void onResponse(Call<ProjectAddResponse> call, Response<ProjectAddResponse> response) {
                System.out.println( "arjun" + response.raw() );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        Intent i = new Intent( ViewProjectsActivity.this, ProjectFooterActivity.class );
                        startActivity( i );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<ProjectAddResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );
            }
        } );
    }

    private void footer() {
        ImageView imageGallery = (ImageView) findViewById( R.id.image_gallery );
        imageGallery.setVisibility( GONE );
        ImageView imageAttachament = (ImageView) findViewById( R.id.image_attachament );
        imageAttachament.setVisibility( GONE );

        ImageView imageCamera = (ImageView) findViewById( R.id.image_camera );
        imageCamera.setVisibility( GONE );
        ImageView imageProfile = (ImageView) findViewById( R.id.image_profile );
        imageProfile.setVisibility( GONE );
        TextView tv_create = (TextView) findViewById( R.id.tv_create );
        tv_create.setText( "Create" );
        tv_create.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (AndroidUtils.isNetworkAvailable( getApplicationContext() )) {
                        attemptCreateProject();
                    } else {
                        attemptCreateOfflineProjects();
                    }
                }
            }
        } );

    }

    private void attemptCreateOfflineProjects() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        mProjectName.setError( null );
        projectName = mProjectName.getText().toString();
        String color = mColorCodePoject.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty( projectName )) {
            mProjectName.setError( getString( R.string.error_required ) );
            focusView = mProjectName;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
            projectListResponseRecords1.setProject_id( id );
            projectListResponseRecords1.setName(projectName);
            projectListResponseRecords1.setColor(color);
            ProjectDBHelper projectDBHelper = new ProjectDBHelper(ViewProjectsActivity.this);
            projectDBHelper.insertUserDetails(projectListResponseRecords1);
            Intent i = new Intent( ViewProjectsActivity.this, ProjectFooterActivity.class );
            Toast.makeText( getApplicationContext(), "Project Offline Created Sucessfully", Toast.LENGTH_SHORT ).show();
            startActivity(i);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
