package com.actnow.android.activities.projects;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.AdvancedSearchActivity;
import com.actnow.android.activities.CommentsActivity;
import com.actnow.android.activities.insights.ProjectInsightsActivity;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.ProjectFooterAdapter;
import com.actnow.android.adapter.ProjectOfflineAdapter;
import com.actnow.android.databse.ProjectDBHelper;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.view.View.GONE;
public class ProjectFooterActivity extends AppCompatActivity {
    RecyclerView mRecyclerViewProjectFooter;
    ProjectFooterAdapter mProjectFooterAdapter;
    ProjectOfflineAdapter mProjectOfflineAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fabProject;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    EditText mQucikFindProject;
    ImageView mImgBulbProject;
    Button mButtonProjectAdvanced;
    String id;
    ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList;

    RadioButton mRadioButtonProjectName;
    private ProgressDialog mProgressDialog;


    int textlength = 0;
    private String selectedType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_project_footer );
        projectListResponseRecordsArrayList = new ArrayList<ProjectListResponseRecords>();
        mRecyclerViewProjectFooter = findViewById( R.id.projectfooter_recyclerView );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mRecyclerViewProjectFooter.setLayoutManager( mLayoutManager );
        mRecyclerViewProjectFooter.setItemAnimator( new DefaultItemAnimator() );
        mProjectFooterAdapter = new ProjectFooterAdapter( projectListResponseRecordsArrayList );
        mRecyclerViewProjectFooter.setAdapter( mProjectFooterAdapter );
        mProjectOfflineAdapter = new ProjectOfflineAdapter(projectListResponseRecordsArrayList);
        mRecyclerViewProjectFooter.setAdapter( mProjectOfflineAdapter );

        initializeViews();
        appHeaderTwo();
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
        btnLink1.setText( "Projects" );
        btnLink1.setTextColor( getResources().getColor( R.color.colorAccent ) );
        ImageView btnCalendar = (ImageView) findViewById( R.id.btn_insightsrAppHeaderTwo );
        btnCalendar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(), ProjectInsightsActivity.class);
               startActivity( i);

            }
        } );
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
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_projectList );
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
        if (AndroidUtils.isNetworkAvailable( getApplicationContext() )) {
            attemptProjectList();
            showProgressDialog();
        } else {
            attemptOfflineProjects();
        }

        mQucikFindProject = findViewById( R.id.edit_searchProject );
        mQucikFindProject.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mRecyclerViewProjectFooter.getVisibility() != View.VISIBLE)
                    mRecyclerViewProjectFooter.setVisibility( View.VISIBLE );
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter( editable.toString() );
            }
        } );
        mImgBulbProject = findViewById( R.id.image_buldProject );
        mImgBulbProject.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( ProjectFooterActivity.this, ViewIdeasActivity.class );
                startActivity( i );
            }
        } );
        fabProject = findViewById( R.id.fab );
        fabProject.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> userId = session.getUserDetails();
                String projectOwnerName = userId.get( UserPrefUtils.NAME );
                Intent i = new Intent( ProjectFooterActivity.this, ViewProjectsActivity.class );
                i.putExtra( "projectOwnerName", projectOwnerName );
                startActivity( i );
                finish();
            }
        } );
        mButtonProjectAdvanced = findViewById( R.id.button_searchProject );
        mButtonProjectAdvanced.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), AdvancedSearchActivity.class );
                startActivity( i );
            }
        } );


    /*    mProjectFooterAdapter = new ProjectFooterAdapter( projectListResponseRecordsArrayList, R.layout.custom_project_footer, getApplicationContext() );
        mRecyclerViewProjectFooter.setAdapter( mProjectFooterAdapter );*/
    }


    private void filter(String toString) {
        ArrayList<ProjectListResponseRecords> projectListResponseRecordsFilter = new ArrayList<>();
        for (ProjectListResponseRecords name : projectListResponseRecordsArrayList) {
            if (name.getName().toLowerCase().contains( toString.toLowerCase() )) {
                projectListResponseRecordsFilter.add( name );
            }

        }
        mProjectFooterAdapter.filterList( projectListResponseRecordsFilter );
    }

    private void attemptProjectList() {
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse( id );
        call.enqueue( new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setProjectFooterList( response.body().getProject_records() );
                        hideProgressDialog();
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<ProjectListResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );
            }
        } );

    }

    private void setProjectFooterList(List<ProjectListResponseRecords> project_records) {
        ProjectDBHelper projectDBHelper = new ProjectDBHelper( ProjectFooterActivity.this );
        if (project_records.size() > 0) {
            for (int i = 0; project_records.size() > i; i++) {
                ProjectListResponseRecords projectListResponseRecords = project_records.get( i );
                ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
                projectListResponseRecords1.setProject_id(projectListResponseRecords.getProject_id());
                projectListResponseRecords1.setName(projectListResponseRecords.getName());
                projectListResponseRecords1.setProject_code(projectListResponseRecords.getProject_code());
                projectListResponseRecords1.setColor(projectListResponseRecords.getColor());
                projectListResponseRecordsArrayList.add(projectListResponseRecords1);
                projectDBHelper.insertUserDetails(projectListResponseRecords1);
                System.out.println( "index: " + i + " database: " + id );
            }
            mRecyclerViewProjectFooter.setAdapter( mProjectFooterAdapter );
            mRecyclerViewProjectFooter.addOnItemTouchListener( new ProjectFooterActivity.RecyclerTouchListener( this, mRecyclerViewProjectFooter, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    View view1 = (View) findViewById( R.id.liner_projectList );
                    RadioGroup mRadioGroup = (RadioGroup) view.findViewById( R.id.radioGroupProject );
                    mRadioGroup.clearCheck();
                    mRadioButtonProjectName = (RadioButton)findViewById( R.id.projectNameFooter );
                    final TextView mProjectCode = (TextView) view.findViewById( R.id.tv_projectCode );
                    final TextView mProjectId = (TextView) view.findViewById( R.id.tv_projectId );
                    final TextView mProjectColor =(TextView)view.findViewById(R.id.tv_projectColor );
                    mRadioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (null != mRadioButtonProjectName && checkedId > -1) {
                                if (checkedId == R.id.projectNameFooter) {
                                    selectedType = mRadioButtonProjectName.getText().toString();
                                    String projectcode = mProjectCode.getText().toString();
                                    HashMap<String, String> userId = session.getUserDetails();
                                    String id = userId.get( UserPrefUtils.ID );
                                    String projectOwnerName = userId.get( UserPrefUtils.NAME );
                                    String s = mRadioButtonProjectName.getText().toString();
                                    Intent i = new Intent( getApplicationContext(), ProjectTaskListActivity.class );
                                    i.putExtra( "projectName", s );
                                    i.putExtra( "id", id );
                                    i.putExtra( "projectOwnerName", projectOwnerName );
                                    i.putExtra( "projectcode", projectcode );
                                    startActivity( i );
                                } else if (checkedId != 0) {
                                    selectedType = mRadioButtonProjectName.getText().toString();
                                }
                            }
                        }
                    } );
                    ImageView mImageComment = (ImageView) view.findViewById( R.id.img_commentProjectList );
                    mImageComment.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String s = mRadioButtonProjectName.getText().toString();
                            String project_code = mProjectCode.getText().toString();
                            String projectId = mProjectId.getText().toString();
                            Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                            i.putExtra( "projectName", s );
                            i.putExtra( "projectcode", project_code );
                            i.putExtra( "projectid", projectId );
                            System.out.println( "comment" + id + s + projectId + project_code );
                            startActivity( i );
                        }
                    } );
                    ImageView mImageUserAddProject = (ImageView) view.findViewById( R.id.img_useraddProjectList );
                    mImageUserAddProject.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent( getApplicationContext(), InvitationActivity.class );
                            String project_code = mProjectCode.getText().toString();
                            i.putExtra( "projectCode", project_code );
                            startActivity( i );
                        }
                    } );
                    ImageView mImgeEdit = (ImageView) view.findViewById( R.id.img_editPencilProjectList );
                    mImgeEdit.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String id = userId.get( UserPrefUtils.ID );
                            String projectOwnerName = userId.get( UserPrefUtils.NAME );
                            String s = mRadioButtonProjectName.getText().toString();
                            String projectcode = mProjectCode.getText().toString();
                            String color = mProjectColor.getText().toString();
                            Intent i = new Intent( getApplicationContext(), EditProjectActivity.class );
                            i.putExtra( "projectName", s );
                            i.putExtra( "id", id );
                            i.putExtra( "projectOwnerName", projectOwnerName );
                            i.putExtra( "projectcode", projectcode );
                            i.putExtra( "color", color );
                            System.out.println( "projecteditvalues" + s + id + projectOwnerName + projectcode + color );
                            startActivity( i );
                        }
                    } );

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            } ) );
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     * <p>
     * - creating an innerclass implementing RevyvlerView.OnItemTouchListener
     * - Pass clickListener interface as parameter
     */
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(ProjectFooterActivity context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
            this.clicklistener = clickListener;

            gestureDetector = new GestureDetector( context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = mRecylerViewSingleSub.findChildViewUnder( e.getX(), e.getY() );
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick( child, mRecylerViewSingleSub.getChildAdapterPosition( child ) );
                    }
                }
            } );
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder( e.getX(), e.getY() );
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent( e )) {
                clicklistener.onClick( child, rv.getChildAdapterPosition( child ) );
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    private void attemptOfflineProjects() {
        AndroidUtils.showProgress( false, mProgressView, mContentLayout );
        ProjectDBHelper projectDBHelper = new ProjectDBHelper(getApplicationContext());
        Cursor cursor = projectDBHelper.getProjectAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()){
                ProjectListResponseRecords projectListResponseRecords = new ProjectListResponseRecords();
                String name = cursor.getString( cursor.getColumnIndex( projectDBHelper.KEY_NAME ) );
                String date = cursor.getString( cursor.getColumnIndex( projectDBHelper.KEY_DUEDATE ) );
                String projectCode = cursor.getString( cursor.getColumnIndex( projectDBHelper.KEY_PROJECTCODE ) );
                projectListResponseRecords.setName( name );
                projectListResponseRecords.setDue_date( date );
                projectListResponseRecords.setProject_code( projectCode );
                projectListResponseRecordsArrayList.add( projectListResponseRecords );
            }
        }
        mRecyclerViewProjectFooter.setAdapter( mProjectOfflineAdapter );
        mRecyclerViewProjectFooter.addOnItemTouchListener( new ProjectFooterActivity.RecyclerTouchListener( this, mRecyclerViewProjectFooter, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                View view1 = (View) findViewById( R.id.liner_projectList );
                RadioGroup mRadioGroup = (RadioGroup) view.findViewById( R.id.radioGroupProject );
                final RadioButton mRadioButtonProjectName = (RadioButton) view.findViewById( R.id.projectNameFooter );
                final TextView mProjectCode = (TextView) view.findViewById( R.id.tv_projectCode );
                final TextView mProjectId = (TextView) view.findViewById( R.id.tv_projectId );
                final TextView mProjectColor =(TextView)view.findViewById(R.id.tv_projectColor );
                mRadioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.projectNameFooter) {
                            if (checkedId == R.id.projectNameFooter) {
                                selectedType = mRadioButtonProjectName.getText().toString();
                                String projectcode = mProjectCode.getText().toString();
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get( UserPrefUtils.ID );
                                String projectOwnerName = userId.get( UserPrefUtils.NAME );
                                String s = mRadioButtonProjectName.getText().toString();
                                Intent i = new Intent( getApplicationContext(), ProjectTaskListActivity.class );
                                i.putExtra( "projectName", s );
                                i.putExtra( "id", id );
                                i.putExtra( "projectOwnerName", projectOwnerName );
                                i.putExtra( "projectcode", projectcode );
                                System.out.println("ProjectofflineTaskValues" + s + id + projectOwnerName + projectcode );
                                startActivity( i );
                            } else if (checkedId != -1) {
                                selectedType = mRadioButtonProjectName.getText().toString();
                            }
                        }
                    }
                } );
                ImageView mImageComment = (ImageView) view.findViewById( R.id.img_commentProjectList );
                mImageComment.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = mRadioButtonProjectName.getText().toString();
                        String project_code = mProjectCode.getText().toString();
                        String projectId = mProjectId.getText().toString();
                        Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                        i.putExtra( "projectName", s );
                        i.putExtra( "projectcode", project_code );
                        i.putExtra( "projectid", projectId );
                        System.out.println( "comment" + id + s + projectId + project_code );
                        startActivity( i );
                    }
                } );
                ImageView mImgeEdit = (ImageView) view.findViewById( R.id.img_editPencilProjectList );
                mImgeEdit.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> userId = session.getUserDetails();
                        String id = userId.get( UserPrefUtils.ID );
                        String projectOwnerName = userId.get( UserPrefUtils.NAME );
                        String s = mRadioButtonProjectName.getText().toString();
                        String projectcode = mProjectCode.getText().toString();
                        Intent i = new Intent( getApplicationContext(), EditProjectActivity.class );
                        i.putExtra( "projectName", s );
                        i.putExtra( "id", id );
                        i.putExtra( "projectOwnerName", projectOwnerName );
                        i.putExtra( "projectcode", projectcode );
                        System.out.println( "i" + s + id + projectOwnerName + projectcode );
                        startActivity( i );
                    }
                } );

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ) );
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

        ImageView imgProject = (ImageView) findViewById( R.id.img_projects );
        imgProject.setImageResource( R.drawable.ic_projects_red );
        TextView txtProject = (TextView) findViewById( R.id.txt_projects );
        txtProject.setTextColor( getResources().getColor( R.color.colorAccent ) );
    }

    private void activityToady() {
        Intent i = new Intent( getApplicationContext(), TodayTaskActivity.class );
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
}
