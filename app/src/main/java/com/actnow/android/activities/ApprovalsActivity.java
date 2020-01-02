package com.actnow.android.activities;


import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.ApprovalAdapter;
import com.actnow.android.sdk.responses.TaskComplete;
import com.actnow.android.sdk.responses.TaskDelete;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.activeandroid.Cache.getContext;

public class ApprovalsActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    EditText mApprovalQucikSearch;
    Button mButtonAdavancedSearchApproval;
    ImageView mImageBulbTaskApproval;

    RecyclerView mRecyclerViewApproval;
    RecyclerView.LayoutManager mLayoutManager;
    ApprovalAdapter mApprovalAdapter;

    private String selectedType = "";
    final Context context = this;

    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();

    TextView mTaskApprovalPriority;
    TextView mTaskApprovalDate;
    TextView mTaskApprovalName;
    TextView mTaskCode;

    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_approvals );
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
        btnLink2.setVisibility( View.GONE );
        btnLink1.setText( "Approvals" );
        btnLink1.setTextColor( getResources().getColor( R.color.colorAccent ) );
        ImageView btnCalendar = (ImageView) findViewById( R.id.btn_calendarAppHeaderTwo );
        btnCalendar.setVisibility( View.GONE );
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
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_approval );
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
        mImageBulbTaskApproval = findViewById( R.id.image_approvalbuldProject );
        mImageBulbTaskApproval.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), ViewIdeasActivity.class );
                startActivity( i );
            }
        } );
        mApprovalQucikSearch = findViewById( R.id.edit_searchApproval );
        mApprovalQucikSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter( editable.toString() );

            }
        } );

        mButtonAdavancedSearchApproval = findViewById( R.id.button_searchApproval );
        mButtonAdavancedSearchApproval.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), AdvancedSearchActivity.class );
                startActivity( i );
            }
        } );

        mRecyclerViewApproval = (RecyclerView) findViewById( R.id.approval_recyclerView );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mRecyclerViewApproval.setLayoutManager( mLayoutManager );
        mRecyclerViewApproval.setItemAnimator( new DefaultItemAnimator() );
        mApprovalAdapter = new ApprovalAdapter( taskListRecordsArrayList );
        mRecyclerViewApproval.setAdapter( mApprovalAdapter );
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse( id );
        call.enqueue( new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                System.out.println( "res" + response.raw() );
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    System.out.println( "url" + response.raw() );
                    if (response.body().getSuccess().equals( "true" )) {
                        setProjectFooterList( response.body().getTask_records() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );

            }
        } );
    }

    private void filter(String toString) {
        ArrayList<TaskListRecords> taskListRecordsFilter = new ArrayList<>();
        for (TaskListRecords name : taskListRecordsArrayList) {
            if (name.getName().toLowerCase().contains( toString.toLowerCase() )) {
                taskListRecordsFilter.add( name );

            }
        }
        mApprovalAdapter.filterList( taskListRecordsFilter );
    }

    private void setProjectFooterList(List<TaskListRecords> taskListRecordsList) {
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get( i );
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName( taskListRecords.getName() );
                taskListRecords1.setDue_date( taskListRecords.getDue_date() );
                taskListRecords1.setPriority( taskListRecords.getPriority() );
                taskListRecords1.setTask_code( taskListRecords.getTask_code() );
                taskListRecords1.setProject_name( taskListRecords.getProject_name() );
                taskListRecords1.setProject_code( taskListRecords.getProject_code() );
                if (taskListRecords.getStatus().equals( "2" )) {
                    taskListRecordsArrayList.add( taskListRecords1 );
                }

            }
            mRecyclerViewApproval.setAdapter( mApprovalAdapter );
            mRecyclerViewApproval.addOnItemTouchListener( new ApprovalsActivity.RecyclerTouchListener( this, mRecyclerViewApproval, new ApprovalsActivity.ClickListener() {
                @Override
                public void onClick(final View view, int position) {
                    mTaskApprovalName = (TextView) view.findViewById( R.id.approvalTaskName );
                    mTaskApprovalDate = (TextView) view.findViewById( R.id.approvalTaskDate );
                    mTaskApprovalPriority = (TextView) view.findViewById( R.id.tv_approvalTaskPriority );
                    mTaskCode = (TextView) view.findViewById( R.id.tv_taskCodeApproval );
                    final TextView mProjectCode = (TextView) view.findViewById( R.id.tv_approvalProjectCode );
                    ImageView mImageTick = (ImageView) view.findViewById( R.id.img_checkedTaskApproval );
                    mImageTick.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String id = userId.get( UserPrefUtils.ID );
                            String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                            String task_code = mTaskCode.getText().toString();
                            Call<TaskComplete> callComplete = ANApplications.getANApi().checkTheTaskApprove( id, task_code, orgn_code );
                            callComplete.enqueue( new Callback<TaskComplete>() {
                                @Override
                                public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getSuccess().equals( "true" )) {
                                            Intent i = new Intent( getApplicationContext(), ApprovalsActivity.class );
                                            startActivity( i );
                                        } else {
                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                        }
                                    } else {
                                        AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                                    }
                                }

                                @Override
                                public void onFailure(Call<TaskComplete> call, Throwable t) {
                                    Log.d( "CallBack", " Throwable is " + t );

                                }
                            } );

                        }
                    } );
                    ImageView mImageWrong = (ImageView) view.findViewById( R.id.img_wrongTaskApproval );
                    mImageWrong.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String id = userId.get( UserPrefUtils.ID );
                            String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                            String task_code = mTaskCode.getText().toString();
                            Call<TaskComplete> callComplete = ANApplications.getANApi().checkTheDisApprove( id, task_code, orgn_code );
                            callComplete.enqueue( new Callback<TaskComplete>() {
                                @Override
                                public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getSuccess().equals( "true" )) {
                                            Intent i = new Intent( getApplicationContext(), ApprovalsActivity.class );
                                            startActivity( i );
                                        } else {
                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                        }
                                    } else {
                                        AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                                    }
                                }

                                @Override
                                public void onFailure(Call<TaskComplete> call, Throwable t) {
                                    Log.d( "CallBack", " Throwable is " + t );

                                }
                            } );

                        }
                    } );
                    ImageView mImageUserAdd = (ImageView) view.findViewById( R.id.img_approvaluseraddTaskList );
                    mImageUserAdd.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = mTaskCode.getText().toString();
                            String projectCode = mProjectCode.getText().toString();
                            Intent i = new Intent( getApplicationContext(), InvitationActivity.class );
                            i.putExtra( "TaskCode", task_code );
                            i.putExtra( "SenIvitaionprojectCode", projectCode );
                            startActivity( i );
                        }
                    } );
                    ImageView mImageComment = (ImageView) view.findViewById( R.id.img_approvalcommentTaskList );
                    mImageComment.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                            startActivity( i );
                        }
                    } );
                    ImageView mImgeDelete = (ImageView) view.findViewById( R.id.img_approvalTaskDelete );
                    mImgeDelete.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String id = userId.get( UserPrefUtils.ID );
                            String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                            String task_code = mTaskCode.getText().toString();
                            Call<TaskDelete> callTaskDelete = ANApplications.getANApi().checkTheDelete( id, task_code, orgn_code );
                            System.out.println( "deleteFields" + id + task_code + orgn_code );
                            callTaskDelete.enqueue( new Callback<TaskDelete>() {
                                @Override
                                public void onResponse(Call<TaskDelete> call, Response<TaskDelete> response) {
                                    System.out.println( "deleteResponse" + response.raw() );
                                    if (response.isSuccessful()) {
                                        System.out.println( "deleteResponse1" + response.raw() );
                                        if (response.body().getSuccess().equals( "true" )) {
                                            System.out.println( "deleteResponse2" + response.raw() );
                                            Intent i =new Intent( getApplicationContext(),ApprovalsActivity.class);
                                            startActivity(i);
                                            Snackbar.make( mContentLayout, "TaskOffline Deleted Sucessfully", Snackbar.LENGTH_SHORT ).show();
                                        } else {
                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                        }
                                    } else {
                                        AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                                    }

                                }
                                @Override
                                public void onFailure(Call<TaskDelete> call, Throwable t) {
                                    Log.d( "CallBack", " Throwable is " + t );

                                }
                            } );

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

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ApprovalsActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(ApprovalsActivity context, final RecyclerView mRecylerViewSingleSub, ApprovalsActivity.ClickListener clickListener) {
            this.clicklistener = clickListener;
            gestureDetector = new GestureDetector( getContext(), new GestureDetector.SimpleOnGestureListener() {

                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

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

    }

    private void activityToady() {
        Intent i = new Intent( getApplicationContext(), TodayTaskActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


