package com.actnow.android.activities.tasks;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ApprovalsActivity;
import com.actnow.android.activities.MeActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.monthly.MonthlyTaskListActivity;
import com.actnow.android.activities.ProjectFooterActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.taskchart.DailyTaskChartActivity;
import com.actnow.android.activities.taskspinner.PriorityTaskActivity;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAddListActivity extends AppCompatActivity {
    RecyclerView mTaskRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fabTask;
    TaskListAdapter mTaskListAdapter;
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    EditText mTaskQucikSearch;
    Button mButtonAdavancedSearch;
    ImageView mImageBulbTask;
    private String selectedType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_task_add_list);
        appHeader();
        initializeViews();
        appFooter();
    }

    private void appHeader() {
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2);
        btnLink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iApproval = new Intent(getApplicationContext(), ApprovalsActivity.class);
                startActivity(iApproval);
                finish();

            }
        });
        TextView btnLink3 = (TextView) findViewById(R.id.btn_link_3);
        btnLink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
                startActivity(iTimeLine);
                finish();
            }
        });
        TextView btnLink4 = (TextView) findViewById(R.id.btn_link_4);

        btnLink1.setText("Tasks");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));
        btnLink2.setText("Approvals");
        btnLink3.setText("Time Log");
        btnLink4.setVisibility(View.GONE);

        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(TaskAddListActivity.this, MonthlyTaskListActivity.class);
                startActivity(i);
                //Snackbar.make(mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT).show();
            }
        });
        ImageView btnNotifications = (ImageView) findViewById(R.id.btn_notifications);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT).show();
            }
        });
        ImageView btnSettings = (ImageView) findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String  accountEmail = userId.get(UserPrefUtils.EMAIL);
                Intent i =new Intent(TaskAddListActivity.this, SettingsActivity.class);
                i.putExtra("email", accountEmail);
                startActivity(i);
                finish();
            }
        });
    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mImageBulbTask = findViewById(R.id.image_bulbTask);
        mImageBulbTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(TaskAddListActivity.this, ViewIdeasActivity.class);
                startActivity(i);
            }
        });
        mTaskQucikSearch = findViewById(R.id.edit_searchTask);
        mButtonAdavancedSearch = findViewById(R.id.button_searchTask);
        fabTask = findViewById(R.id.fab_task);
        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get(UserPrefUtils.ID);
                String taskOwnerName = userId.get(UserPrefUtils.NAME);
                Intent i = new Intent(TaskAddListActivity.this,ViewTasksActivity.class);
                i.putExtra("id", id);
                i.putExtra("taskOwnerName", taskOwnerName);
                startActivity(i);
                System.out.println("userName" + id + taskOwnerName);
            }
        });
        mTaskRecylcerView = findViewById(R.id.task_recyclerView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mTaskRecylcerView.setLayoutManager(mLayoutManager);
        mTaskRecylcerView.setItemAnimator(new DefaultItemAnimator());
        mTaskListAdapter = new TaskListAdapter(taskListRecordsArrayList, R.layout.custom_idea, getApplicationContext());
        mTaskRecylcerView.setAdapter(mTaskListAdapter);
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse(id);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
               System.out.println("api" + response.raw());
                if (response.isSuccessful()) {
                    System.out.println("url" + response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        System.out.println("data" + response.body().getSuccess());
                        setProjectFooterList(response.body().getTask_records());
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

    private void setProjectFooterList(List<TaskListRecords> taskListRecordsList) {
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get(i);
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName(taskListRecords.getName());
                taskListRecords1.setDue_date(taskListRecords.getDue_date());
                //taskListRecords1.setRemindars_count(taskListRecords.getRemindars_count());
                taskListRecordsArrayList.add(taskListRecords1);
            }
            mTaskRecylcerView.setAdapter(new TaskListAdapter(taskListRecordsArrayList, R.layout.custom_task_list, getApplicationContext()));
            mTaskRecylcerView.addOnItemTouchListener(new TaskAddListActivity.RecyclerTouchListener(this, mTaskRecylcerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    View view1 = view.findViewById(R.id.liner_taskList);
                    RadioGroup groupTask = (RadioGroup)findViewById(R.id.radioGroupTask);
                    final RadioButton radioButtonTaskName=(RadioButton)view.findViewById(R.id.rb_taskName);
                    TextView tv_dueDate=(TextView)view.findViewById(R.id.tv_TasktDate);
                  /*  TextView tv_reminder=(TextView)view.findViewById(R.id.tv_TaskRaminder);
                    tv_remindersetVisibility(View.GONE);*/
                    groupTask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(checkedId == R.id.rb_taskName)
                                if (checkedId == R.id.rb_taskName)
                                    selectedType = radioButtonTaskName.getText().toString();
                                else if(checkedId==0)
                                    selectedType = radioButtonTaskName.getText().toString();
                        }
                    });
                    HashMap<String, String> userId = session.getUserDetails();
                    //String id = userId.get(UserPrefUtils.ID);
                    String taskOwnerName= userId.get(UserPrefUtils.NAME);
                    String s = radioButtonTaskName.getText().toString();
                    String s1 = tv_dueDate.getText().toString();
                    Intent i = new Intent(TaskAddListActivity.this, EditTaskActivity.class);
                    i.putExtra("TaskName", s);
                    i.putExtra("TaskDate", s1);
                   // i.putExtra("id",id);
                    i.putExtra("taskOwnerName",taskOwnerName);
                    startActivity(i);
                    finish();
                    System.out.println("dataPassing"+s+s1+taskOwnerName);
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
    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     * <p>
     * - creating an innerclass implementing RevyvlerView.OnItemTouchListener
     * - Pass clickListener interface as parameter
     */
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(TaskAddListActivity context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
            this.clicklistener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
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
                activityMe();
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
       // imgProject.setImageResource(R.drawable.ic_projects_red);
       // TextView txtProject = (TextView) findViewById(R.id.txt_projects);
        //txtProject.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void activityMe() {
        Intent i = new Intent(getApplicationContext(), MeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
    private void activityProject() {
        Intent i =new Intent(getApplicationContext(), ProjectFooterActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
    private void activityTasks() {
        /*Intent i = new Intent(getApplicationContext(), ViewTasksActivity.class);
        startActivity(i);*/
        Intent i = new Intent(getApplicationContext(), PriorityTaskActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityIndividuals() {
        Intent i = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityInsights() {
     /*   Intent i = new Intent(getApplicationContext(), PersonalActivity.class);
        startActivity(i);*/
        Intent i = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

     /* private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            moveTaskToBack(true);
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            Snackbar.make(mContentLayout, "Press once again to exit", Snackbar.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }*/
}
