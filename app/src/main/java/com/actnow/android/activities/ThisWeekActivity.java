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
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.tasks.EditTaskActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.ThisWeekAdapter;
import com.actnow.android.sdk.responses.TaskComplete;
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

import static android.view.View.GONE;
import static com.activeandroid.Cache.getContext;


public class ThisWeekActivity extends AppCompatActivity {
    RecyclerView mThisweekrecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ThisWeekAdapter mThisWeekAdapter;
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    TextView mTextViewWeeks;
    EditText mTaskQucikSearch;
    Button mButtonAdavancedSearch;
    ImageView mImageBulbTask;
    private String selectedType = "";
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();

    final Context context = this;

    TextView mTaskName;

    private RecyclerViewType recyclerViewType;
    protected static final String RECYCLER_VIEW_TYPE = "recycler_view_type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_this_week );
        appHeaderTwo();
        initializeViews();
        appFooter();
        recyclerViewType = (RecyclerViewType) getIntent().getSerializableExtra(RECYCLER_VIEW_TYPE);
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
        btnLink1.setText( "Thisweek" );
        btnLink1.setTextColor( getResources().getColor( R.color.colorAccent ) );
        ImageView btnCalendar = (ImageView) findViewById( R.id.btn_calendarAppHeaderTwo );
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
                                Intent iToday = new Intent( getApplicationContext(), TodayTaskActivity.class );
                                startActivity( iToday );
                                break;
                            case R.id.nav_idea:
                                Intent iIdea = new Intent( getApplicationContext(), ViewIdeasActivity.class );
                                startActivity( iIdea );
                                break;
                            case R.id.nav_thisweek:
                                Toast.makeText( getApplicationContext(), "Selected The Thisweek", Toast.LENGTH_LONG ).show();
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
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layoutWeekView );
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
        mTextViewWeeks = findViewById( R.id.tv_titleThisWeek );
        //populateRecyclerView();
        mImageBulbTask = findViewById( R.id.image_bulbTask );
        mImageBulbTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), ViewIdeasActivity.class );
                startActivity( i );
            }
        } );
        mTaskQucikSearch = findViewById( R.id.edit_searchTask );
        mTaskQucikSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "Work in Progress!", Toast.LENGTH_LONG ).show();

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


        mThisweekrecyclerView = findViewById( R.id.thisweek_recyclerView );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mThisweekrecyclerView.setLayoutManager( mLayoutManager );
        mThisweekrecyclerView.setItemAnimator( new DefaultItemAnimator() );
        mThisWeekAdapter = new ThisWeekAdapter( taskListRecordsArrayList, R.layout.custom_task_weekname_list, getApplicationContext() );
        mThisweekrecyclerView.setAdapter( mThisWeekAdapter );
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse( id );
        call.enqueue( new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                System.out.println( "raw" + response.raw() );
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    System.out.println( "url" + response.raw());
                    if (response.body().getSuccess().equals( "true")){
                        setTaskList( response.body().getTask_records());
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
            }
        } );
    }
  /*  //populate recycler view
    private void populateRecyclerView() {
        ArrayList<SectionModel> sectionModelArrayList = new ArrayList<>();
         //ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();
        //for loop for sections
        for (int i = 1; i <= 5; i++) {
            ArrayList<String> itemArrayList = new ArrayList<>();
            //for loop for items
            for (int j = 1; j <= 10; j++) {
                itemArrayList.add("Item " + j);
            }

            //add the section and items to array list
            sectionModelArrayList.add(new SectionModel(" session" + i, itemArrayList));
        }

        SectionRecyclerViewAdapter adapter = new SectionRecyclerViewAdapter(this, recyclerViewType, sectionModelArrayList);
        mThisweekrecyclerView.setAdapter(adapter);

    }*/


    private void setTaskList(List<TaskListRecords> taskListRecordsList) {
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get( i );
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName( taskListRecords.getName() );
                taskListRecords1.setDue_date( taskListRecords.getDue_date() );
                taskListRecords1.setPriority( taskListRecords.getPriority() );
                taskListRecords1.setProject_code( taskListRecords.getProject_code() );
                taskListRecords1.setTask_code( taskListRecords.getTask_code() );
                taskListRecords1.setProject_name( taskListRecords.getProject_name() );
                taskListRecords1.setRepeat_type( taskListRecords.getRepeat_type() );
                if (taskListRecords.getStatus().equals( "1" )) {
                    taskListRecordsArrayList.add( taskListRecords1 );
                }
            }
            mThisweekrecyclerView.setAdapter( new ThisWeekAdapter( taskListRecordsArrayList, R.layout.custom_task_weekname_list, getApplicationContext() ) );
            mThisweekrecyclerView.addOnItemTouchListener( new ThisWeekActivity.RecyclerTouchListener( this, mThisweekrecyclerView, new ClickListener() {
                @Override
                public void onClick(final View view, int position) {
                    final View view1 = view.findViewById( R.id.taskList_liner );
                    RadioGroup groupTask = (RadioGroup) view.findViewById( R.id.taskWeekGroupTask );
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById( R.id.Week_buttonAction );
                    final TextView tv_dueDate = (TextView) view.findViewById( R.id.tv_weektaskListDate );
                    final TextView tv_taskcode = (TextView) view.findViewById( R.id.tv_WeektaskCode );
                    final TextView tv_priority = (TextView) view.findViewById( R.id.tv_WeektaskListPriority );
                    final TextView tv_status = (TextView) view.findViewById( R.id.tv_Weektaskstatus );
                    final TextView tv_projectName = (TextView) view.findViewById( R.id.tv_WeekprojectNameTaskList );
                    final TextView tv_projectCode = (TextView) view.findViewById( R.id.tv_WeekprojectCodeTaskList );
                    groupTask.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.radio_buttonAction) {
                                if (checkedId == R.id.radio_buttonAction) {
                                    selectedType = radioButtonTaskName.getText().toString();
                                    Snackbar snackbar = Snackbar.make( mContentLayout, "Completed.", Snackbar.LENGTH_LONG ).setAction( "UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            view1.setVisibility( View.VISIBLE );
                                            Intent i = new Intent( getApplicationContext(), ThisWeekActivity.class );
                                            startActivity( i );
                                            Snackbar snackbar1 = Snackbar.make( mContentLayout, "Task is restored!", Snackbar.LENGTH_SHORT );
                                            snackbar1.show();
                                        }
                                    } );
                                    View sbView = snackbar.getView();
                                    TextView textView = (TextView) sbView.findViewById( R.id.snackbar_text );
                                    textView.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            view1.setVisibility( View.GONE );
                                            HashMap<String, String> userId = session.getUserDetails();
                                            String id = userId.get( UserPrefUtils.ID );
                                            final String taskOwnerName = userId.get( UserPrefUtils.NAME );
                                            final String name = mTaskName.getText().toString();
                                            final String date = tv_dueDate.getText().toString();
                                            String task_code = tv_taskcode.getText().toString();
                                            String task_prioroty = tv_priority.getText().toString();
                                            String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                                            Call<TaskComplete> callComplete = ANApplications.getANApi().checkTheTaskComplete( id, task_code, orgn_code );
                                            callComplete.enqueue( new Callback<TaskComplete>() {
                                                @Override
                                                public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.body().getSuccess().equals( "true" )) {
                                                            Intent i = new Intent( getApplicationContext(), ThisWeekActivity.class );
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
                                            Snackbar snackbar2 = Snackbar.make( mContentLayout, "Task is completed!", Snackbar.LENGTH_SHORT );
                                            snackbar2.show();

                                        }
                                    } );
                                    snackbar.show();
                                } else if (checkedId == 0) {
                                    selectedType = radioButtonTaskName.getText().toString();

                                }
                            }
                        }
                    } );
                    mTaskName = (TextView) view.findViewById( R.id.tv_WeektaskListName );
                    mTaskName.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String taskOwnerName = userId.get( UserPrefUtils.NAME );
                            String name = mTaskName.getText().toString();
                            String date = tv_dueDate.getText().toString();
                            String task_code = tv_taskcode.getText().toString();
                            Intent i = new Intent( getApplicationContext(), EditTaskActivity.class );
                            i.putExtra( "TaskName", name );
                            i.putExtra( "TaskDate", date );
                            i.putExtra( "TaskCode", task_code );
                            i.putExtra( "taskOwnerName", taskOwnerName );
                            startActivity( i );
                        }
                    } );
                    ImageView mImageUserAdd = (ImageView) view.findViewById( R.id.img_WeekuseraddTaskList );
                    mImageUserAdd.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            String projectCode = tv_projectCode.getText().toString();
                            Intent i = new Intent( getApplicationContext(), InvitationActivity.class );
                            i.putExtra( "TaskCode", task_code );
                            i.putExtra( "SenIvitaionprojectCode", projectCode );
                            startActivity( i );
                        }
                    } );
                    ImageView mImageComment = (ImageView) view.findViewById( R.id.img_WeekcommentTaskList );
                    mImageComment.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                            String name = mTaskName.getText().toString();
                            String date = tv_dueDate.getText().toString();
                            String task_code = tv_taskcode.getText().toString();
                            i.putExtra( "TaskName", name );
                            i.putExtra( "TaskDate", date );
                            i.putExtra( "TaskCode", task_code );
                            startActivity( i );
                        }
                    } );
                    ImageView mImageRaminder = (ImageView) view.findViewById( R.id.img_WeekraminderTaskList );
                    mImageRaminder.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent( getApplicationContext(), ReaminderScreenActivity.class );
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

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(ThisWeekActivity context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
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
