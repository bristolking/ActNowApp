package com.actnow.android.activities.tasks;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.insights.InsightsChart;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.monthly.MonthlyTaskListActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.TaskComplete;
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

import static com.activeandroid.Cache.getContext;

public class MonthlyTaskListSpinnerActivity extends AppCompatActivity {
    final Context context = this;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();
    RecyclerView mRecyclerViewMonthly;
    RecyclerView.LayoutManager mLayoutManager;
    TaskListAdapter mTaskListAdapter;
    Spinner mSpinnerMonthlyTaskList;
    ArrayAdapter<String> arrayAdapterWeekly;
    String[] repetitive = {"MONTHLY", "YEARLY", "DAILY", "WEEKLY",};
    TextView mDailyOverdue, mDailyPriority, mDailyAssigned;
    private String selectedType = "";

    ArrayList<com.abdeveloper.library.MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<com.abdeveloper.library.MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();

    MultiSelectDialog mIndividuvalDialog, mProjectDialog;
    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;
    TextView mTaskName;
    EditText mMonthlyTasklistQucikSearch;
    Button mMonthlyTaskListButtonAdavancedSearch;
    ImageView mMonthlyTaskListImageBulbTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_monthly_task_list_spinner);
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
        btnLink2.setVisibility(View.GONE);
        btnLink1.setText("Tasks");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));
        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_insightsrAppHeaderTwo);
        //btnCalendar.setVisibility(View.GONE);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MonthlyTaskListActivity.class);
                startActivity(i);
            }
        });
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
                String  email = userId.get( UserPrefUtils.EMAIL);
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
                TextView mTextName = (TextView) findViewById(R.id.tv_nameProfile);
                mTextName.setText(taskOwnerName);
                TextView mTextEmail =(TextView)findViewById(R.id.tv_emailProfile);
                mTextEmail.setText( email );
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToday = new Intent(getApplicationContext(),TodayTaskActivity.class);
                                startActivity(iToday);
                                break;
                            case R.id.nav_idea:
                                Intent iIdea = new Intent(getApplicationContext(),ViewIdeasActivity.class);
                                startActivity(iIdea);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent(getApplicationContext(), ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                            case R.id.nav_taskfilter:
                                Toast.makeText(getApplicationContext(), "Selected The TaskFilter", Toast.LENGTH_SHORT).show();
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

                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_monthlytaskList);
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
        requestDynamicContent();
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        View view = (View) findViewById(R.id.liner_taskMonthlyList);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TaskAddListActivity.class);
                startActivity(i);
                finish();
            }
        });

        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add( 0 );

        mMonthlyTaskListImageBulbTask = findViewById(R.id.image_bulbMonthlyTaskList);
        mMonthlyTaskListImageBulbTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ViewIdeasActivity.class);
                startActivity(i);
            }
        });
        mMonthlyTasklistQucikSearch = findViewById(R.id.edit_searchMonthlyTaskList);
        mMonthlyTasklistQucikSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Work in Progress!", Toast.LENGTH_LONG).show();

            }
        });
        mMonthlyTaskListButtonAdavancedSearch = findViewById(R.id.button_searchMonthlyTaskList);
        mMonthlyTaskListButtonAdavancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent( getApplicationContext(), AdvancedSearchActivity.class);
                startActivity(i);
            }
        });

        mSpinnerMonthlyTaskList = (Spinner) findViewById(R.id.spinnerMonthlyTaskList);
        arrayAdapterWeekly = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, repetitive);
        mSpinnerMonthlyTaskList.setAdapter(arrayAdapterWeekly);
        mSpinnerMonthlyTaskList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) mSpinnerMonthlyTaskList.getSelectedView()).setTextColor(getResources().getColor(R.color.colorAccent));
                switch (position) {
                    case 0:
                       /* Intent iMonthly =new Intent(getApplicationContext(),MonthlyTaskListSpinnerActivity.class);
                        startActivity(iMonthly);
                        break;*/
                        break;
                    case 1:
                        Intent iYearly = new Intent(getApplicationContext(), YearlyTaskListActivity.class);
                        startActivity(iYearly);
                        break;
                    case 2:
                        Intent iDaily = new Intent(getApplicationContext(), DailyTaskListActivity.class);
                        startActivity(iDaily);
                        break;
                    case 3:
                        Intent iWeekly = new Intent(getApplicationContext(), WeeklyTaskListActivity.class);
                        startActivity(iWeekly);
                        break;
                   /* case 4:
                        Intent iRepetitive = new Intent(getApplicationContext(),TaskAddListActivity.class);
                        startActivity(iRepetitive);
                        break;*/
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRecyclerViewMonthly = findViewById(R.id.task_recyclerViewMonthly);
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
                System.out.println("res"+response.raw());
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    System.out.println("url" + response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        setMonthlyTaskList(response.body().getTask_records());
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

    private void setMonthlyTaskList(List<TaskListRecords> taskListRecordsList) {
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get( i );
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName( taskListRecords.getName() );
                taskListRecords1.setDue_date( taskListRecords.getDue_date() );
                taskListRecords1.setPriority( taskListRecords.getPriority() );
                taskListRecords1.setProject_code( taskListRecords.getProject_code() );
                taskListRecords1.setTask_code( taskListRecords.getTask_code() );
                taskListRecords1.setRemindars_count( taskListRecords.getRemindars_count() );
                taskListRecords1.setStatus( taskListRecords.getStatus());
                taskListRecords1.setProject_name(taskListRecords.getProject_name());
                taskListRecords1.setRepeat_type(taskListRecords.getRepeat_type());
                if (taskListRecords.getStatus().equals( "1" ) && taskListRecords.getRepeat_type().equals("Monthly")) {
                    taskListRecordsArrayList.add( taskListRecords1 );
                }
            }
            mRecyclerViewMonthly.setAdapter( new TaskListAdapter( taskListRecordsArrayList, R.layout.task_list_cutsom, getApplicationContext() ) );
            mRecyclerViewMonthly.addOnItemTouchListener( new MonthlyTaskListSpinnerActivity.RecyclerTouchListener( this, mRecyclerViewMonthly, new MonthlyTaskListSpinnerActivity.ClickListener() {
                @Override
                public void onClick(final View view, int position) {
                    final View view1 = view.findViewById( R.id.taskList_liner );
                    RadioGroup groupTask = (RadioGroup) view.findViewById( R.id.taskradioGroupTask );
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById( R.id.radio_buttonAction );
                    final TextView tv_dueDate = (TextView) view.findViewById( R.id.tv_taskListDate );
                    final TextView tv_taskcode = (TextView) view.findViewById( R.id.tv_taskCode );
                    final TextView tv_priority = (TextView) view.findViewById( R.id.tv_taskListPriority );
                    final TextView tv_status = (TextView) view.findViewById( R.id.tv_taskstatus );
                    final TextView tv_projectName =(TextView)view.findViewById(R.id.tv_projectNameTaskList);
                    final TextView tv_projectCode =(TextView)view.findViewById(R.id.tv_projectCodeTaskList);
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
                                            Intent i = new Intent( getApplicationContext(), MonthlyTaskListSpinnerActivity.class );
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
                                            Call<TaskComplete> taskCompleteCall = ANApplications.getANApi().checkTheTaskComplete( id, task_code, orgn_code );
                                            taskCompleteCall.enqueue( new Callback<TaskComplete>() {
                                                @Override
                                                public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.body().getSuccess().equals( "true" )) {
                                                            Intent i = new Intent( getApplicationContext(), MonthlyTaskListSpinnerActivity.class );
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
                    mTaskName = (TextView) view.findViewById( R.id.tv_taskListName );
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
                    ImageView mImageUserAdd = (ImageView) view.findViewById( R.id.img_useraddTaskList );
                    mImageUserAdd.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i= new Intent(getApplicationContext(), InvitationActivity.class);
                            startActivity(i);
                            //mIndividuvalDialog.show( getSupportFragmentManager(), "mIndividuvalDialog" );
                        }
                    } );
                    ImageView mImageComment = (ImageView) view.findViewById( R.id.img_commentTaskList );
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
                    ImageView mImageRaminder = (ImageView) view.findViewById( R.id.img_raminderTaskList );
                    mImageRaminder.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            Intent i=new Intent( getApplicationContext(), ReaminderScreenActivity.class);
                            i.putExtra( "TaskCode", task_code );
                            startActivity(i);
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

        private MonthlyTaskListSpinnerActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(MonthlyTaskListSpinnerActivity context, final RecyclerView mRecylerViewSingleSub, MonthlyTaskListSpinnerActivity.ClickListener clickListener) {
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
    private void requestDynamicContent() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse( id );
        call.enqueue( new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setLoadCheckBox( response.body().getOrgn_users_records() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<CheckBoxResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );
            }
        } );

    }

    private void setLoadCheckBox(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get( i );
                listOfIndividuval.add( new MultiSelectModel( Integer.parseInt( orgnUserRecordsCheckBox.getId() ), orgnUserRecordsCheckBox.getName() ) );
            }
            mIndividuvalDialog = new MultiSelectDialog()
                    .title( "Individuval" ) //setting title for dialog
                    .titleSize( 25 )
                    .positiveText( "Done" )
                    .negativeText( "Cancel" )
                    .preSelectIDsList( individualCheckBox )
                    .setMinSelectionLimit( 0 )
                    .setMaxSelectionLimit( listOfIndividuval.size() )
                    .multiSelectList( listOfIndividuval ) // the multi select model list with ids and name
                    .onSubmit( new MultiSelectDialog.SubmitCallbackListener() {
                        @Override
                        public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                            for (int i = 0; i < selectedIds.size(); i++) {
                                // mIndividualCheckBox.setText(dataString);
                            }
                            individuvalArray = new JSONArray( selectedIds );
                        }

                        @Override
                        public void onCancel() {
                            Log.d( "TAG", "Dialog cancelled" );
                        }
                    } );
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
        ImageView imgProject = (ImageView) findViewById(R.id.img_task);
        imgProject.setImageResource(R.drawable.ic_tasklistred);
        TextView txtProject = (TextView) findViewById(R.id.txt_task);
        txtProject.setTextColor(getResources().getColor(R.color.colorAccent));
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
        Toast.makeText(getApplicationContext(), "Selected the Tasks", Toast.LENGTH_LONG).show();
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


}
