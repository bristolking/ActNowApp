package com.actnow.android.activities.monthly;
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
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.CommentsActivity;
import com.actnow.android.activities.ReaminderScreenActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.tasks.EditTaskActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.TaskComplete;
import com.actnow.android.sdk.responses.TaskDelete;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.activeandroid.Cache.getContext;

public class MonthlyTaskListActivity extends AppCompatActivity    {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    RecyclerView mRecyclerViewMonthly;
    TaskListAdapter mTaskListAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();
    private String currentMonth;
    final Context context = this;
    private String selectedType = "";

    TextView mTaskName;
    String task_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_monthly);
        appHeaderTwo();
        appFooter();
        initializeViews();
    }
    private void appHeaderTwo() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back_two);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1_two);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2_two);
        btnLink2.setVisibility(GONE);
        btnLink1.setText("Tasks");
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
                String email = userId.get( UserPrefUtils.EMAIL);
                ImageView mImageProfile = (ImageView) findViewById(R.id.img_profile);
                String img = userId.get( UserPrefUtils.IMAGEPATH);
                System.out.println( "img"+ img );
                Glide.with(getApplicationContext())
                        .load(img)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(mImageProfile);
                mImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),EditAccountActivity.class);
                        startActivity(i);
                    }
                });
                TextView mTextName =(TextView)findViewById(R.id.tv_nameProfile);
                mTextName.setText(taskOwnerName);
                TextView mTextEmail =(TextView)findViewById(R.id.tv_emailProfile);
                mTextEmail.setText( email );
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToday = new Intent(getApplicationContext(),TodayTaskActivity.class);
                                startActivity(iToday);
                                break;
                            case R.id.nav_idea:
                                Intent iIdea = new Intent(getApplicationContext(), ViewIdeasActivity.class);
                                startActivity(iIdea);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent(getApplicationContext(), ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                            case R.id.nav_taskfilter:
                                Intent iTaskfilter = new Intent(getApplicationContext(),TaskAddListActivity.class);
                                startActivity(iTaskfilter);
                                break;
                            case R.id.nav_project:
                                Intent iProject = new Intent(getApplicationContext(),ProjectFooterActivity.class);
                                startActivity(iProject);
                                break;
                            case R.id.nav_individuals:
                                Intent iIndividuals = new Intent(getApplicationContext(),ViewIndividualsActivity.class);
                                startActivity(iIndividuals);
                                break;
                            case R.id.nav_insights:
                                Intent iInsights = new Intent(getApplicationContext(),DailyTaskChartActivity.class);
                                startActivity(iInsights);
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent(getApplicationContext(),TimeLineActivity.class);
                                startActivity(iTimeLine);
                                break;
                            case R.id.nav_profile:
                                Intent iprofile = new Intent(getApplicationContext(), AccountSettingActivity.class);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium = new Intent(getApplicationContext(), PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_logout:
                                session.logoutUser();
                                break;

                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_monthTask);
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
        mContentLayout = findViewById(R.id.content_layout);
        mProgressView = findViewById(R.id.progress_bar);



        MaterialCalendarView materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        Calendar cal = Calendar.getInstance();
        currentMonth = new SimpleDateFormat("MM").format(cal.getTime());
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String msg = " Selected month is" + "-" + date.getMonth();
                String seletedMonth = String.valueOf(date.getMonth() + 1);
                currentMonth = "0"+seletedMonth;
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get(UserPrefUtils.ID);
                afterselectedMonth(id);
            }
        });
        mRecyclerViewMonthly =(RecyclerView)findViewById(R.id.recyclerViewMonthlyTaskList);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewMonthly.setLayoutManager(mLayoutManager);
        mRecyclerViewMonthly.setItemAnimator(new DefaultItemAnimator());
        mTaskListAdapter = new TaskListAdapter(taskListRecordsArrayList, R.layout.task_list_cutsom, getApplicationContext());
        mRecyclerViewMonthly.setAdapter(mTaskListAdapter);
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse(id);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        setTaskList(response.body().getTask_records());
                    }else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
            }
        });
    }

    private void afterselectedMonth(String id) {
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse(id);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        setTaskList(response.body().getTask_records());
                    }else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
            }
        });
    }
    private void setTaskList(List<TaskListRecords> taskListRecordsList) {
        taskListRecordsArrayList.clear();
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get(i);
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName(taskListRecords.getName());
                taskListRecords1.setDue_date(taskListRecords.getDue_date());
                taskListRecords1.setPriority(taskListRecords.getPriority());
                taskListRecords1.setProject_name( taskListRecords.getProject_name());
                String date = taskListRecords.getDue_date();
                String[] dateArray = date.split(" ");
                String[] finalDateArray = dateArray[0].split("-");
                String month = finalDateArray[1];
                if (currentMonth.equals(month)) {
                    taskListRecordsArrayList.add(taskListRecords1);
                }
            }
            mRecyclerViewMonthly.setAdapter(new TaskListAdapter(taskListRecordsArrayList,R.layout.task_list_cutsom, getApplicationContext()));
            mRecyclerViewMonthly.addOnItemTouchListener(new MonthlyTaskListActivity.RecyclerTouchListener(this, mRecyclerViewMonthly, new MonthlyTaskListActivity.ClickListener() {
                @Override
                public void onClick(final View view, int position) {
                    final View view1 = view.findViewById( R.id.taskList_liner );
                    RadioGroup groupTask = (RadioGroup) view.findViewById( R.id.taskradioGroupTask );
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById( R.id.radio_buttonAction );
                    final TextView tv_dueDate = (TextView) view.findViewById( R.id.tv_taskListDate );
                    final TextView tv_taskcode = (TextView) view.findViewById( R.id.tv_taskCode );
                    final TextView tv_priority = (TextView) view.findViewById( R.id.tv_taskListPriority );
                    final TextView tv_status = (TextView) view.findViewById( R.id.tv_taskstatus );
                    final TextView tv_projectName = (TextView) view.findViewById( R.id.tv_projectNameTaskList );
                    final TextView tv_projectCode = (TextView) view.findViewById( R.id.tv_projectCodeTaskList );
                    task_code = tv_taskcode.getText().toString();
                    groupTask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.radio_buttonAction) {
                                if (checkedId == R.id.radio_buttonAction) {
                                    selectedType = radioButtonTaskName.getText().toString();
                                    Snackbar snackbar = Snackbar.make( mContentLayout, "Completed.", Snackbar.LENGTH_LONG ).setAction( "UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            view1.setVisibility( View.VISIBLE );
                                            Intent i = new Intent( getApplicationContext(), TodayTaskActivity.class );
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
                                            String task_prioroty = tv_priority.getText().toString();
                                            String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                                            Call<TaskComplete> callComplete = ANApplications.getANApi().checkTheTaskComplete( id, task_code, orgn_code );
                                            callComplete.enqueue( new Callback<TaskComplete>() {
                                                @Override
                                                public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.body().getSuccess().equals( "true" )) {
                                                            Intent i = new Intent( getApplicationContext(), TodayTaskActivity.class );
                                                            startActivity( i );
                                                        } else {
                                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_LONG ).show();
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
                    mTaskName = (TextView) view.findViewById(R.id.tv_taskListName);
                    mTaskName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String taskOwnerName = userId.get(UserPrefUtils.NAME);
                            String s = radioButtonTaskName.getText().toString();
                            String s1 = tv_dueDate.getText().toString();
                            Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                            i.putExtra("TaskName", s);
                            i.putExtra("TaskDate", s1);
                            i.putExtra("taskOwnerName", taskOwnerName);
                            startActivity(i);
                        }
                    });
                    ImageView mImageUserAdd =(ImageView)view.findViewById(R.id.img_useraddTaskList);
                    mImageUserAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            String projectCode = tv_projectCode.getText().toString();
                            Intent i = new Intent( getApplicationContext(), InvitationActivity.class );
                            i.putExtra( "TaskCode", task_code );
                            i.putExtra( "SenIvitaionprojectCode", projectCode );
                            startActivity( i );
                        }
                    });
                    ImageView mImageComment =(ImageView)view.findViewById(R.id.img_commentTaskList);
                    mImageComment.setOnClickListener(new View.OnClickListener() {
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
                    });
                    ImageView mImageRaminder =(ImageView)view.findViewById(R.id.img_raminderTaskList);
                    mImageRaminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            Intent i = new Intent( getApplicationContext(), ReaminderScreenActivity.class );
                            i.putExtra( "TaskCode", task_code );
                            startActivity( i );
                        }

                    });
                    ImageView mImageDelete = (ImageView) view.findViewById( R.id.img_delete );
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
                                            Intent i = new Intent( getApplicationContext(),TodayTaskActivity.class );
                                            startActivity( i );
                                            Snackbar.make( mContentLayout, "Task Deleted Sucessfully", Snackbar.LENGTH_SHORT ).show();
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

            }));
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private MonthlyTaskListActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(MonthlyTaskListActivity context, final RecyclerView mRecylerViewSingleSub, MonthlyTaskListActivity.ClickListener clickListener) {
            this.clicklistener = clickListener;
            gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener() {

                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                public void onLongPress(MotionEvent e) {
                    View child = mRecylerViewSingleSub.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, mRecylerViewSingleSub.getChildAdapterPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
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
        ImageView imgProject = (ImageView) findViewById(R.id.img_projects);

    }
    private void activityToady() {
        Intent i = new Intent(getApplicationContext(), TodayTaskActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
    private void activityProject() {
        Intent i =new Intent(getApplicationContext(), ProjectFooterActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
    private void activityTasks() {
        Intent i = new Intent(getApplicationContext(), TaskAddListActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityIndividuals() {
        Intent i = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityInsights() {
        Intent i = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
    public void onBackPressed() {
        super.onBackPressed();
    }

}
