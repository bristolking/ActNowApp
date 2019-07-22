package com.actnow.android.activities;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
;
import android.util.Log;
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
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.monthly.ProjectMonthlyActivity;
import com.actnow.android.activities.projects.EditProjectActivity;
import com.actnow.android.activities.projects.ViewProjectsActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.taskchart.DailyTaskChartActivity;
import com.actnow.android.activities.taskspinner.PriorityTaskActivity;
import com.actnow.android.adapter.ProjectFooterAdapter;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ProjectFooterActivity extends AppCompatActivity {
    RecyclerView mRecyclerViewProjectFooter;
    ProjectFooterAdapter mProjectFooterAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fabProject;
    private ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList = new ArrayList<ProjectListResponseRecords>();
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    EditText mQucikFindProject;
    ImageView mImgBulbProject;
    Button mButtonProjectAdvanced;
    String id;
    String name;
    int textlength = 0;
    private String selectedType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_project_footer);
        initializeViews();
        appHeader();
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

        btnLink1.setText("Projects");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));
        btnLink2.setText("Approvals");
        btnLink3.setText("Time Log");
        btnLink4.setVisibility(View.GONE);
        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ProjectFooterActivity.this, ProjectMonthlyActivity.class);
                startActivity(i);
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
                Intent i =new Intent(ProjectFooterActivity.this,SettingsActivity.class);
                startActivity(i);
            }
        });
    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mQucikFindProject = findViewById(R.id.edit_searchProject);
        mImgBulbProject = findViewById(R.id.image_buldProject);
        mImgBulbProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ProjectFooterActivity.this, ViewIdeasActivity.class);
                startActivity(i);
            }
        });
        fabProject= findViewById(R.id.fab);
        fabProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> userId = session.getUserDetails();
                String projectOwnerName = userId.get(UserPrefUtils.NAME);
                Intent i = new Intent(ProjectFooterActivity.this, ViewProjectsActivity.class);
                i.putExtra("projectOwnerName", projectOwnerName);
                startActivity(i);
                finish();
            }
        });
        mButtonProjectAdvanced = findViewById(R.id.button_searchProject);
        mRecyclerViewProjectFooter = findViewById(R.id.projectfooter_recyclerView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewProjectFooter.setLayoutManager(mLayoutManager);
        mRecyclerViewProjectFooter.setItemAnimator(new DefaultItemAnimator());
        mProjectFooterAdapter = new ProjectFooterAdapter(projectListResponseRecordsArrayList, R.layout.custom_project_footer, getApplicationContext());
        mRecyclerViewProjectFooter.setAdapter(mProjectFooterAdapter);
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get(UserPrefUtils.ID);
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse(id);
        call.enqueue(new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    System.out.println("name"+response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        setProjectFooterList(response.body().getProject_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<ProjectListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }
    private void setProjectFooterList(List<ProjectListResponseRecords> project_records) {
        if (project_records.size() > 0) {
            for (int i = 0; project_records.size() > i; i++) {
                ProjectListResponseRecords projectListResponseRecords = project_records.get(i);
                ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
                projectListResponseRecords1.setName(projectListResponseRecords.getName());
                projectListResponseRecords1.setDue_date(projectListResponseRecords.getDue_date());
                projectListResponseRecordsArrayList.add(projectListResponseRecords1);
            }
            mRecyclerViewProjectFooter.setAdapter(new ProjectFooterAdapter(projectListResponseRecordsArrayList, R.layout.custom_project_footer, getApplicationContext()));

            mRecyclerViewProjectFooter.addOnItemTouchListener(new ProjectFooterActivity.RecyclerTouchListener(this, mRecyclerViewProjectFooter, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    View view1 = view.findViewById(R.id.liner_projectList);
                     RadioGroup group = (RadioGroup) findViewById(R.id.radioGroupProject);
                     final RadioButton radioButton = (RadioButton)view.findViewById(R.id.projectNameFooter);
                        TextView tvDate=(TextView)view.findViewById(R.id.tv_projectDateFooter);
                        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(checkedId == R.id.projectNameFooter)
                                if (checkedId == R.id.projectNameFooter)
                                    selectedType = radioButton.getText().toString();
                                else if(checkedId==0)
                                    selectedType = radioButton.getText().toString();
                        }
                    });
                    HashMap<String, String> userId = session.getUserDetails();
                    String id = userId.get(UserPrefUtils.ID);
                    String projectOwnerName= userId.get(UserPrefUtils.NAME);
                    String s = radioButton.getText().toString();
                    Intent i = new Intent(ProjectFooterActivity.this, EditProjectActivity.class);
                    i.putExtra("projectName", s);
                    i.putExtra("id",id);
                    i.putExtra("projectOwnerName",projectOwnerName);
                    startActivity(i);
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

        public RecyclerTouchListener(ProjectFooterActivity context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
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
        imgProject.setImageResource(R.drawable.ic_projects_red);
        TextView txtProject = (TextView) findViewById(R.id.txt_projects);
        txtProject.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void activityMe() {
        Intent i = new Intent(getApplicationContext(), MeActivity.class);
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
        Intent i = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
}
