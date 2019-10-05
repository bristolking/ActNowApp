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

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
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
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.activeandroid.Cache.getContext;


public class TodayTaskActivity extends AppCompatActivity {
    final Context context = this;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();

    RecyclerView mToadyOverDueTask;
    RecyclerView.LayoutManager mLayoutManagerOverDue;
    TaskListAdapter mTaskListOverDueAdapter;

    RecyclerView mTodayRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    TaskListAdapter mTaskListAdapter;

    EditText mTaskQucikSearch;
    Button mButtonAdavancedSearch;
    ImageView mImageBulbTask;

    private String selectedType = "";
    ArrayList<com.abdeveloper.library.MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<com.abdeveloper.library.MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();

    MultiSelectDialog mIndividuvalDialog, mProjectDialog;
    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;
    TextView mTaskName;
    String task_code;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_today_task);
        appFooter();
        appHeaderTwo();
        initializeViews();
    }

    private void appHeaderTwo() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back_two);
        imgeBack.setVisibility(GONE);
       /* imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1_two);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2_two);
        btnLink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iApproval = new Intent(getApplicationContext(), ApprovalsActivity.class);
                //iApproval.putExtra( "TaskCode", task_code );
                startActivity(iApproval);
                //System.out.println( "TaskCode"+ task_code);
            }
        });
        btnLink1.setText("Today");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));
        btnLink2.setText("Approvals");
        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendarAppHeaderTwo);
        btnCalendar.setVisibility(GONE);
        ImageView btnNotifications = (ImageView) findViewById(R.id.btn_notificationsAppHeaderTwo);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Work in progress!", Toast.LENGTH_SHORT).show();
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
                ImageView mImageProfile = (ImageView) findViewById(R.id.img_profile);
                mImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),EditAccountActivity.class);
                        startActivity(i);
                    }
                });                TextView mTextName = (TextView) findViewById(R.id.tv_nameProfile);
                mTextName.setText(taskOwnerName);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Toast.makeText(getApplicationContext(), "Selected TodayTaskChart", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
                                startActivity(iTimeLine);
                                break;
                            case R.id.nav_filter:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_profile:
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get(UserPrefUtils.ID);
                                String name = userId.get(UserPrefUtils.NAME);
                                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                                Intent iprofile = new Intent(getApplicationContext(), EditAccountActivity.class);
                                iprofile.putExtra("id", id);
                                iprofile.putExtra("name", name);
                                iprofile.putExtra("email", accountEmail);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium = new Intent(getApplicationContext(), PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent(getApplicationContext(), ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_todayTask);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                ImageView imgeClose = (ImageView) findViewById(R.id.nav_close);
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
        HashMap<String, String> userId = session.getUserDetails();
        String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
        System.out.println("orgn_code"+orgn_code);

        if (orgn_code == null){
            Intent i = new Intent(TodayTaskActivity.this,OrngActivity.class);
            startActivity(i);
        }else {

        }

        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        requestDynamicContent();

        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add(0);

        mImageBulbTask = findViewById(R.id.image_bulbTask);
        mImageBulbTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ViewIdeasActivity.class);
                startActivity(i);
            }
        });
        mTaskQucikSearch = findViewById(R.id.edit_searchTask);
        mTaskQucikSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Work in Progress!", Toast.LENGTH_LONG).show();

            }
        });
        mButtonAdavancedSearch = findViewById(R.id.button_searchTask);
        mButtonAdavancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent( getApplicationContext(), AdvancedSearchActivity.class);
                startActivity(i);
            }
        });
        //overDueRecyclerView
      /*  mToadyOverDueTask = findViewById(R.id.toadyTaskOverDue_recyclerView);
        mLayoutManagerOverDue = new LinearLayoutManager(getApplicationContext());
        mToadyOverDueTask.setLayoutManager(mLayoutManagerOverDue);
        mToadyOverDueTask.setItemAnimator(new DefaultItemAnimator());
        mTaskListOverDueAdapter = new TaskListAdapter(taskListRecordsArrayList, R.layout.task_list_cutsom, getApplicationContext());
        mToadyOverDueTask.setAdapter(mTaskListOverDueAdapter);
        id = userId.get(UserPrefUtils.ID);
        Call<TaskListResponse> callOverDue= ANApplications.getANApi().checkTheTaskListResponse(id);
        callOverDue.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        setOverDueTaskList( response.body().getTask_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);

            }
        });*/
        //TodayRecyclerView
        mTodayRecyclerView = findViewById(R.id.thisweek_recyclerView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mTodayRecyclerView.setLayoutManager(mLayoutManager);
        mTodayRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTaskListAdapter = new TaskListAdapter(taskListRecordsArrayList, R.layout.task_list_cutsom, getApplicationContext());
        mTodayRecyclerView.setAdapter(mTaskListAdapter);
         id = userId.get(UserPrefUtils.ID);
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse(id);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        setTaskList(response.body().getTask_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);

            }
        });
    }

    //OverDue TaskList
    /*private void setOverDueTaskList(List<TaskListRecords> taskListRecords2) {
        if (taskListRecords2.size() > 0) {
            for (int i = 0; taskListRecords2.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecords2.get( i );
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName( taskListRecords.getName() );
                taskListRecords1.setDue_date( taskListRecords.getDue_date() );
                taskListRecords1.setPriority( taskListRecords.getPriority() );
                taskListRecords1.setProject_code( taskListRecords.getProject_code() );
                taskListRecords1.setTask_code( taskListRecords.getTask_code() );
                if (taskListRecords.getStatus().equals("1")) {
                    Date date1 = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(date1);
                    String date2[] = taskListRecords.getDue_date().split( " " );
                    String date3 = date2[0];
                    try {
                        Date date4 = new SimpleDateFormat("yyyy-MM-dd" ).parse( date3);
                        System.out.println( "date3"+ date4 );
                        if(date4.before(date1)){
                            taskListRecordsArrayList.add(taskListRecords1);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }*/

    // Today Task list
    private void setTaskList(List<TaskListRecords> taskListRecordsList) {
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get(i);
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName(taskListRecords.getName());
                taskListRecords1.setDue_date(taskListRecords.getDue_date());
                taskListRecords1.setPriority(taskListRecords.getPriority());
                taskListRecords1.setProject_code( taskListRecords.getProject_code());
                taskListRecords1.setTask_code( taskListRecords.getTask_code());
                if (taskListRecords.getStatus().equals("1")) {
                    Date date1 = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(date1);
                    String date2[] = taskListRecords.getDue_date().split( " " );
                    String date3 = date2[0];
                    if (date3.equals(formattedDate)){
                        taskListRecordsArrayList.add(taskListRecords1);
                    }
                }
            }
            mTodayRecyclerView.setAdapter(new TaskListAdapter(taskListRecordsArrayList, R.layout.task_list_cutsom, getApplicationContext()));
            mTodayRecyclerView.addOnItemTouchListener(new TodayTaskActivity.RecyclerTouchListener(this, mTodayRecyclerView, new ClickListener() {
                @Override
                public void onClick(final View view, int position) {
                    final View view1 = view.findViewById(R.id.taskList_liner);
                    RadioGroup groupTask = (RadioGroup) view.findViewById(R.id.taskradioGroupTask);
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById(R.id.radio_buttonAction);
                    final TextView tv_dueDate = (TextView) view.findViewById( R.id.tv_taskListDate );
                    final TextView tv_taskcode = (TextView) view.findViewById( R.id.tv_taskCode );
                    final TextView tv_priority = (TextView) view.findViewById( R.id.tv_taskListPriority );
                    final TextView tv_status = (TextView) view.findViewById( R.id.tv_taskstatus );
                    task_code = tv_taskcode.getText().toString();
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
                                            Intent i =new Intent(getApplicationContext(),TodayTaskActivity.class);
                                            startActivity(i);
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
                                                            Intent i =new Intent(getApplicationContext(),TodayTaskActivity.class);
                                                            startActivity(i);
                                                        } else {
                                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_LONG ).show();
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
                                            Snackbar snackbar2 = Snackbar.make(mContentLayout, "Task is completed!", Snackbar.LENGTH_SHORT);
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
                    ImageView mImageUserAdd = (ImageView) view.findViewById(R.id.img_useraddTaskList);
                    mImageUserAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //mIndividuvalDialog.show(getSupportFragmentManager(), "mIndividuvalDialog");
                            Intent i= new Intent(getApplicationContext(),InvitationActivity.class);
                            startActivity(i);
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

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(TodayTaskActivity context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
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
    private void requestDynamicContent() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse(id);
        call.enqueue(new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        setLoadCheckBox(response.body().getOrgn_users_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<CheckBoxResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });

    }

    private void setLoadCheckBox(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get(i);
                listOfIndividuval.add(new MultiSelectModel(Integer.parseInt(orgnUserRecordsCheckBox.getId()), orgnUserRecordsCheckBox.getName()));
            }
            mIndividuvalDialog = new MultiSelectDialog()
                    .title("Individuval") //setting title for dialog
                    .titleSize(25)
                    .positiveText("Done")
                    .negativeText("Cancel")
                    .preSelectIDsList(individualCheckBox)
                    .setMinSelectionLimit(0)
                    .setMaxSelectionLimit(listOfIndividuval.size())
                    .multiSelectList(listOfIndividuval) // the multi select model list with ids and name
                    .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                        @Override
                        public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                            for (int i = 0; i < selectedIds.size(); i++) {
                               // mIndividualCheckBox.setText(dataString);
                            }
                            individuvalArray = new JSONArray(selectedIds);
                        }

                        @Override
                        public void onCancel() {
                            Log.d("TAG", "Dialog cancelled");
                        }
                    });
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
        ImageView imgProject = (ImageView) findViewById(R.id.img_today);
        imgProject.setImageResource(R.drawable.ic_today_red);
        TextView txtProject = (TextView) findViewById(R.id.txt_today);
        txtProject.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void activityToady() {
        Toast.makeText(getApplicationContext(), "Seleted the ToadyTask", Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
}
