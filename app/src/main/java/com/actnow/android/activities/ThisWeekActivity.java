package com.actnow.android.activities;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
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
import com.actnow.android.activities.insights.InsightsChart;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.adapter.ThisWeekAdapter;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;


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
    FloatingActionButton  fabThisTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_this_week );
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
        btnLink1.setText( "Thisweek" );
        btnLink1.setTextColor( getResources().getColor( R.color.colorAccent ) );
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
        fabThisTask = findViewById( R.id.fab_thisTaskadd );
        fabThisTask.setOnClickListener( new View.OnClickListener() {
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

        mButtonAdavancedSearch = findViewById( R.id.button_searchTask );
        mButtonAdavancedSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), AdvancedSearchActivity.class );
                startActivity( i );
            }
        } );
       /* mThisweekrecyclerView = findViewById( R.id.thisweek_recyclerView );
        mThisweekrecyclerView.setHasFixedSize( true );
        mLayoutManager = new LinearLayoutManager( this );
        mThisWeekAdapter = new ThisWeekAdapter(taskListRecordsArrayList);
        mThisweekrecyclerView.setLayoutManager( mLayoutManager );
        mThisweekrecyclerView.setAdapter( mThisWeekAdapter );
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse( id );
        call.enqueue( new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
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
        } );*/
    }
   /* public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }*/

   /* private void filter(String toString) {
        ArrayList<TaskListRecords> taskListRecordsFilter = new ArrayList<>(  );
        for (TaskListRecords name :taskListRecordsArrayList){
            if (name.getName().toLowerCase().contains( toString.toLowerCase())){
                taskListRecordsFilter.add(name);
            }

        }
        mThisWeekAdapter.filterList(taskListRecordsFilter);
    }
*/

  /*  private void setTaskList(List<TaskListRecords> taskListRecordsList) {
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
            mThisweekrecyclerView.setAdapter( mThisWeekAdapter );
            //mThisweekrecyclerView.setAdapter( new ThisWeekAdapter( taskListRecordsArrayList, R.layout.custom_task_weekname_list, getApplicationContext() ) );
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
                                            Snackbar snackbar1 = Snackbar.make( mContentLayout, "TaskOffline is restored!", Snackbar.LENGTH_SHORT );
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
                                            Snackbar snackbar2 = Snackbar.make( mContentLayout, "TaskOffline is completed!", Snackbar.LENGTH_SHORT );
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
                    ImageView mImageDelete = (ImageView) view.findViewById( R.id.img_WeekuseraddDelete );
                    mImageDelete.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String id = userId.get( UserPrefUtils.ID );
                            String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                            String task_code = tv_taskcode.getText().toString();
                            Call<TaskDelete> taskDeleteCall = ANApplications.getANApi().checkTheDelete( id, task_code, orgn_code );
                            taskDeleteCall.enqueue( new Callback<TaskDelete>() {
                                @Override
                                public void onResponse(Call<TaskDelete> call, Response<TaskDelete> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getSuccess().equals( "true" )) {
                                            Intent i = new Intent( getApplicationContext(),ThisWeekActivity.class );
                                            startActivity( i );
                                            Snackbar.make( mContentLayout, "TaskOffline Deleted Sucessfully", Snackbar.LENGTH_SHORT ).show();
                                        } else {
                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                        }
                                    } else {
                                        AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!" );
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
    }*/

    private void appFooter() {
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
        Intent i = new Intent( getApplicationContext(), InsightsChart.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }



}
