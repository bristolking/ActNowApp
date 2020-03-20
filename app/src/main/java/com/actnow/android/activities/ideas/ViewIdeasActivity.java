package com.actnow.android.activities.ideas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.AdvancedSearchActivity;
import com.actnow.android.activities.CommentsActivity;
import com.actnow.android.activities.ReaminderScreenActivity;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.insights.InsightsChart;
import com.actnow.android.activities.tasks.EditTaskActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.databse.TaskDBHelper;
import com.actnow.android.sdk.responses.TaskComplete;
import com.actnow.android.sdk.responses.TaskDelete;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.activeandroid.Cache.getContext;

public class ViewIdeasActivity extends AppCompatActivity {
    RecyclerView mTaskRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    TaskListAdapter mTaskListAdapter;
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    FloatingActionButton fabIdea;

    EditText mTaskQucikSearch;
    Button mButtonAdavancedSearch;
    ImageView mImageBulbTask;

    final Context context = this;
    private String selectedType = "";
    ArrayList<com.abdeveloper.library.MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<com.abdeveloper.library.MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();
    MultiSelectDialog mIndividuvalDialog, mProjectDialog;

    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;

    TextView mTaskName;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_view_ideas);
        appHeaderTwo();
        initializeViews();
        appFooter();
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
        btnLink1.setText("Idea");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));

        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_insightsrAppHeaderTwo);
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
                ImageView mImageProfile= (ImageView)findViewById(R.id.img_profile);
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
                                Toast.makeText(getApplicationContext(), "Selected The Idea", Toast.LENGTH_SHORT).show();
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
                                Intent iInsights = new Intent(getApplicationContext(), InsightsChart.class);
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
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_idea);
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
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mImageBulbTask = findViewById(R.id.image_bulbTask);
        mImageBulbTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(getApplicationContext(),"selected the Idea",Toast.LENGTH_LONG).show();
            }
        });
        mTaskQucikSearch = findViewById( R.id.edit_searchTask );
        mTaskQucikSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mTaskRecylcerView.getVisibility() != View.VISIBLE)
                    mTaskRecylcerView.setVisibility( View.VISIBLE );
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        } );
        mButtonAdavancedSearch = findViewById(R.id.button_searchTask);
        mButtonAdavancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent( getApplicationContext(), AdvancedSearchActivity.class);
                startActivity(i);
            }
        });
        fabIdea = findViewById(R.id.fab_idea);
        fabIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get( UserPrefUtils.ID );
                String taskOwnerName = userId.get( UserPrefUtils.NAME );
                Intent i =new Intent( getApplicationContext(), ViewTasksActivity.class );
                i.putExtra( "id", id );
                i.putExtra( "taskOwnerName", taskOwnerName );
                startActivity(i);

            }
        });
        mTaskRecylcerView = findViewById(R.id.idea_recyclerView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mTaskRecylcerView.setLayoutManager(mLayoutManager);
        mTaskRecylcerView.setItemAnimator(new DefaultItemAnimator());
        mTaskListAdapter = new TaskListAdapter(taskListRecordsArrayList);
        mTaskRecylcerView.setAdapter(mTaskListAdapter);
        if (AndroidUtils.isNetworkAvailable(getApplicationContext())) {
            attemptTaskList();
        } else {
            ideaNoConnection();
        }
    }
    private void attemptTaskList() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse(id);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
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

    private void filter(String toString) {
        ArrayList<TaskListRecords> taskListRecordsFilter = new ArrayList<>(  );
        for (TaskListRecords name :taskListRecordsArrayList){
            if (name.getName().toLowerCase().contains( toString.toLowerCase())){
                taskListRecordsFilter.add(name);
            }
        }
        mTaskListAdapter.filterList(taskListRecordsFilter);
    }
    private void setProjectFooterList(List<TaskListRecords> taskListRecordsList) {
        TaskDBHelper dbHelper = new TaskDBHelper(getApplicationContext());
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get(i);
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName(taskListRecords.getName());
                taskListRecords1.setRemindars_count(taskListRecords.getRemindars_count());
                taskListRecords1.setPriority(taskListRecords.getPriority());
                taskListRecords1.setProject_code( taskListRecords.getProject_code());
                taskListRecords1.setTask_code( taskListRecords.getTask_code());
                taskListRecords1.setDue_date(taskListRecords.getDue_date());
                taskListRecords1.setRepeat_type( taskListRecords.getRepeat_type());
                taskListRecords1.setProject_name(taskListRecords.getProject_name());
                dbHelper.insertTaskDetails(taskListRecords1);
                if (taskListRecords.getStatus().equals("1") && taskListRecords.getDue_date() == null) {
                    taskListRecordsArrayList.add(taskListRecords1);
                }
            }
            mTaskRecylcerView.setAdapter(mTaskListAdapter);
            mTaskRecylcerView.addOnItemTouchListener(new ViewIdeasActivity.RecyclerTouchListener(this, mTaskRecylcerView, new ViewIdeasActivity.ClickListener() {
                @Override
                public void onClick(final View view, final int position) {
                    final View view1 = view.findViewById(R.id.taskList_liner);
                    RadioGroup groupTask = (RadioGroup) view.findViewById(R.id.taskradioGroupTask);
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById(R.id.radio_buttonAction);
                    final TextView tv_dueDate = (TextView) view.findViewById( R.id.tv_taskListDate );
                    final TextView tv_taskcode = (TextView) view.findViewById( R.id.tv_taskCode );
                    final TextView tv_priority = (TextView) view.findViewById( R.id.tv_taskListPriority );
                    final TextView tv_status = (TextView) view.findViewById( R.id.tv_taskstatus );
                    final TextView tv_projectName =(TextView)view.findViewById(R.id.tv_projectNameTaskList);
                    final TextView tv_projectCode =(TextView)view.findViewById(R.id.tv_projectCodeTaskList);
                    groupTask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.radio_buttonAction) {
                                if (checkedId == R.id.radio_buttonAction) {
                                    selectedType = radioButtonTaskName.getText().toString();
                                    Snackbar snackbar = Snackbar.make(mContentLayout, "Confirm...", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            view1.setVisibility(View.VISIBLE);
                                         /*   Intent i= new Intent( getApplicationContext(),ViewIdeasActivity.class);
                                            startActivity(i);*/
                                            Snackbar snackbar1 = Snackbar.make(mContentLayout, "Task is restored!", Snackbar.LENGTH_SHORT);
                                            snackbar1.show();
                                        }
                                    });
                                    View sbView = snackbar.getView();
                                    TextView textView =(TextView)sbView.findViewById(R.id.snackbar_text);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            view1.setVisibility(View.GONE);
                                            mTaskListAdapter.removeItem(position);
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
                                                         /*   Intent i= new Intent( getApplicationContext(),ViewIdeasActivity.class);
                                                            startActivity(i);*/
                                                        } else {
                                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                                        }
                                                    } else {
                                                        AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!" );
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<TaskComplete> call, Throwable t) {
                                                    Log.d( "CallBack", " Throwable is " + t );
                                                }
                                            } );
                                            Snackbar snackbar2 = Snackbar.make(mContentLayout, "TaskOffline is completed!", Snackbar.LENGTH_SHORT);
                                            snackbar2.show();

                                        }
                                    });
                                    snackbar.show();
                                } else if (checkedId == 0) {
                                    selectedType = radioButtonTaskName.getText().toString();

                                }
                            }
                        }
                    });
                    final TextView mTaskName = (TextView) view.findViewById(R.id.tv_taskListName);
                    mTaskName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String taskOwnerName = userId.get(UserPrefUtils.NAME);
                            String name = mTaskName.getText().toString();
                            String date = tv_dueDate.getText().toString();
                            String task_code = tv_taskcode.getText().toString();
                            String task_prioroty = tv_priority.getText().toString();
                            String projectcode = tv_projectCode.getText().toString();
                            Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                            i.putExtra("TaskName", name);
                            i.putExtra("TaskDate", date);
                            i.putExtra("TaskCode", task_code);
                            i.putExtra("taskOwnerName", taskOwnerName);
                            i.putExtra("projectCode",projectcode);
                            i.putExtra("priority",task_prioroty);
                            startActivity(i);
                            System.out.println("user" + task_code);
                        }
                    });
                    ImageView mImageUserAdd = (ImageView) view.findViewById(R.id.img_useraddTaskList);
                    mImageUserAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            String projectCode = tv_projectCode.getText().toString();
                            Intent i = new Intent( getApplicationContext(), InvitationActivity.class );
                            i.putExtra( "TaskCode", task_code );
                            i.putExtra( "SenIvitaionprojectCode",projectCode);
                            startActivity( i );
                        }
                    });
                    ImageView mImageComment = (ImageView) view.findViewById(R.id.img_commentTaskList);
                    mImageComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getApplicationContext(), CommentsActivity.class);
                            String name = mTaskName.getText().toString();
                            String date = tv_dueDate.getText().toString();
                            String task_code = tv_taskcode.getText().toString();
                            i.putExtra( "TaskName", name );
                            i.putExtra( "TaskDate", date );
                            i.putExtra( "TaskCode", task_code );
                            startActivity(i);
                        }
                    });
                    ImageView mImageRaminder = (ImageView) view.findViewById(R.id.img_raminderTaskList);
                    mImageRaminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            Intent i=new Intent( getApplicationContext(), ReaminderScreenActivity.class);
                            i.putExtra( "TaskCode", task_code );
                            startActivity(i);
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
                            mTaskListAdapter.removeItem(position);
                            Call<TaskDelete> taskDeleteCall = ANApplications.getANApi().checkTheDelete( id, task_code, orgn_code );
                            taskDeleteCall.enqueue( new Callback<TaskDelete>() {
                                @Override
                                public void onResponse(Call<TaskDelete> call, Response<TaskDelete> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getSuccess().equals( "true" )) {
                                        /*    Intent i = new Intent( getApplicationContext(),TodayTaskActivity.class );
                                            startActivity( i );*/
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

        private ViewIdeasActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(ViewIdeasActivity context, final RecyclerView mRecylerViewSingleSub, ViewIdeasActivity.ClickListener clickListener) {
            this.clicklistener = clickListener;
            gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

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

// OFFLINEDATA
    private void  ideaNoConnection(){
        AndroidUtils.showProgress( false, mProgressView, mContentLayout );
        TaskDBHelper taskDBHelper = new TaskDBHelper( getContext() );
        Cursor cursor = taskDBHelper.getAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                TaskListRecords taskListRecords = new TaskListRecords();
                String name = cursor.getString( cursor.getColumnIndex(taskDBHelper.KEY_NAME));
                String date = cursor.getString( cursor.getColumnIndex(taskDBHelper.KEY_DUEDATE));
                String priority = cursor.getString( cursor.getColumnIndex(taskDBHelper.KEY_PRIORITY));
                String projectcode = cursor.getString( cursor.getColumnIndex(taskDBHelper.KEY_PROJECT_CODE));
                String taskcode = cursor.getString( cursor.getColumnIndex(taskDBHelper.KEY_TASK_CODE));
                String remindarscount = cursor.getString( cursor.getColumnIndex(taskDBHelper.KEY_REMINDARS_COUNT));
                String status = cursor.getString( cursor.getColumnIndex(taskDBHelper.KEY_STATUS));
                String projectName = cursor.getString( cursor.getColumnIndex(taskDBHelper.KEY_PROJECT_NAME));
                String type = cursor.getString( cursor.getColumnIndex(taskDBHelper.KEY_REPEAT_TYPE));
                taskListRecords.setName(name);
                taskListRecords.setDue_date(date);
                taskListRecords.setPriority(priority);
                taskListRecords.setProject_code(projectcode);
                taskListRecords.setTask_code(taskcode);
                taskListRecords.setRemindars_count(remindarscount);
                taskListRecords.setStatus(status);
                taskListRecords.setProject_name(projectName);
                taskListRecords.setRepeat_type(type);
                if (taskListRecords.getStatus().equals("1") && taskListRecords.getDue_date() == null) {
                    taskListRecordsArrayList.add(taskListRecords);
                }
            }
            mTaskRecylcerView.setAdapter(mTaskListAdapter);
            mTaskRecylcerView.addOnItemTouchListener(new ViewIdeasActivity.RecyclerTouchListener(this, mTaskRecylcerView, new ViewIdeasActivity.ClickListener() {
                @Override
                public void onClick(final View view, final int position) {
                    final View view1 = view.findViewById(R.id.taskList_liner);
                    RadioGroup groupTask = (RadioGroup) view.findViewById(R.id.taskradioGroupTask);
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById(R.id.radio_buttonAction);
                    final TextView tv_dueDate = (TextView) view.findViewById( R.id.tv_taskListDate );
                    final TextView tv_taskcode = (TextView) view.findViewById( R.id.tv_taskCode );
                    final TextView tv_priority = (TextView) view.findViewById( R.id.tv_taskListPriority );
                    final TextView tv_status = (TextView) view.findViewById( R.id.tv_taskstatus );
                    final TextView tv_projectName =(TextView)view.findViewById(R.id.tv_projectNameTaskList);
                    final TextView tv_projectCode =(TextView)view.findViewById(R.id.tv_projectCodeTaskList);
                    groupTask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.radio_buttonAction) {
                                if (checkedId == R.id.radio_buttonAction) {
                                    selectedType = radioButtonTaskName.getText().toString();
                                    Snackbar snackbar = Snackbar.make(mContentLayout, "Completed.", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            view1.setVisibility(View.VISIBLE);
                                         /*   Intent i= new Intent( getApplicationContext(),ViewIdeasActivity.class);
                                            startActivity(i);*/
                                            Snackbar snackbar1 = Snackbar.make(mContentLayout, "Task is restored!", Snackbar.LENGTH_SHORT);
                                            snackbar1.show();
                                        }
                                    });
                                    View sbView = snackbar.getView();
                                    TextView textView =(TextView)sbView.findViewById(R.id.snackbar_text);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            view1.setVisibility(View.GONE);
                                            mTaskListAdapter.removeItem(position);
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
                                                         /*   Intent i= new Intent( getApplicationContext(),ViewIdeasActivity.class);
                                                            startActivity(i);*/
                                                        } else {
                                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                                        }
                                                    } else {
                                                        AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!" );
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<TaskComplete> call, Throwable t) {
                                                    Log.d( "CallBack", " Throwable is " + t );
                                                }
                                            } );
                                            Snackbar snackbar2 = Snackbar.make(mContentLayout, "TaskOffline is completed!", Snackbar.LENGTH_SHORT);
                                            snackbar2.show();

                                        }
                                    });
                                    snackbar.show();
                                } else if (checkedId == 0) {
                                    selectedType = radioButtonTaskName.getText().toString();

                                }
                            }
                        }
                    });
                    final TextView mTaskName = (TextView) view.findViewById(R.id.tv_taskListName);
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
                    ImageView mImageUserAdd = (ImageView) view.findViewById(R.id.img_useraddTaskList);
                    mImageUserAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            String projectCode = tv_projectCode.getText().toString();
                            Intent i = new Intent( getApplicationContext(), InvitationActivity.class );
                            i.putExtra( "TaskCode", task_code );
                            i.putExtra( "SenIvitaionprojectCode",projectCode);
                            startActivity( i );
                        }
                    });
                    ImageView mImageComment = (ImageView) view.findViewById(R.id.img_commentTaskList);
                    mImageComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getApplicationContext(), CommentsActivity.class);
                            String name = mTaskName.getText().toString();
                            String date = tv_dueDate.getText().toString();
                            String task_code = tv_taskcode.getText().toString();
                            i.putExtra( "TaskName", name );
                            i.putExtra( "TaskDate", date );
                            i.putExtra( "TaskCode", task_code );
                            startActivity(i);
                        }
                    });
                    ImageView mImageRaminder = (ImageView) view.findViewById(R.id.img_raminderTaskList);
                    mImageRaminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            Intent i=new Intent( getApplicationContext(), ReaminderScreenActivity.class);
                            i.putExtra( "TaskCode", task_code );
                            startActivity(i);
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
                            mTaskListAdapter.removeItem(position);
                            Call<TaskDelete> taskDeleteCall = ANApplications.getANApi().checkTheDelete( id, task_code, orgn_code );
                            taskDeleteCall.enqueue( new Callback<TaskDelete>() {
                                @Override
                                public void onResponse(Call<TaskDelete> call, Response<TaskDelete> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getSuccess().equals( "true" )) {
                                        /*    Intent i = new Intent( getApplicationContext(),TodayTaskActivity.class );
                                            startActivity( i );*/
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




    private void appFooter() {
        /*View btnMe = findViewById(R.id.btn_me);
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
    }*/
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
        Intent i = new Intent(getApplicationContext(), TodayTaskActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
    private void activityProject() {
        Intent i = new Intent(getApplicationContext(), ProjectFooterActivity.class);
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
        Intent i = new Intent(getApplicationContext(), InsightsChart.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
