package com.actnow.android.activities.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.insights.InsightsChart;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.adapter.NewTaskProjectAdapter;
import com.actnow.android.adapter.NewTaskProjectAdapterOffline;
import com.actnow.android.databse.ProjectDBHelper;
import com.actnow.android.databse.TaskDBHelper;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.sdk.responses.TaskAddResponse;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static java.util.Calendar.MARCH;

public class ViewTasksActivity extends AppCompatActivity {
    final Context context = this;
    EditText mTaskProjectName, mTaskTitle, mDueDateTask, mCommentTask;
    View mProjectNewTask, mRepeatType, mPriorityNewTask;
    TextView mProjectCheckBox;

    UserPrefUtils session;
    String id;
    String taskOwnerName;
    String[] reapt = {"RepeatType", "Daily", "Weekly", "Monthly", "Yearly"};


    JSONArray projectArray;
    TextView mPriorty;
    // priority
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserPriorty = new ArrayList<>();
    // dates
    String[] listItemsDates;
    boolean[] checkedItemsDates;
    ArrayList<Integer> mUserDates = new ArrayList<>();

    // yearly muliple select
    String[] listItemsMonths;
    boolean[] checkedItemsMonths;
    ArrayList<Integer> uMonths = new ArrayList<>();
    // weekly multiple select
    String[] listItemsWeek;
    boolean[] checkedItemsWeek;
    ArrayList<Integer> mWeek = new ArrayList<>();

    String[] listItemDates1;
    boolean[] checkedItemsDates1;
    ArrayList<Integer> mUserDates1 = new ArrayList<>();


    String[] listItemDates2;
    boolean[] checkedItemsDates2;
    ArrayList<Integer> mUserDates2 = new ArrayList<>();

    // repeatTupe
    String[] listItemsRepeat;
    boolean[] checkedItemsRepeat;
    ArrayList<Integer> mRepeatTypeDaily = new ArrayList<>();

    Spinner mSpinnerReptOption;
    ArrayAdapter<String> arrayAdapterReapt;

    View reWeeklyView, reYearly, reMonthly;

    TextView mYearly, mWeekName, mDates;

    String repeat_type;
    String week_days;
    String months;
    String days;
    String projectcode;

    String projectName;
    ImageView imageView;

    RecyclerView mRecyclerViewDateTime;
    NewTaskProjectAdapter mNewTaskProjectAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList;

    TextView mProjectNameDailog;
    TextView mProjectCodeDailog;

    private ProgressDialog mProgressDialog;

    View mProgressView, mContentLayout;
    View mProgressViewNEWTask, mContentLayoutNewTask;
    TextView  mProjectNameDailogOffline;
    TextView mProjectCodeDailogOffline;
    String projectNameOffline;
    String projectCodeOffline;



    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    MultiSelectDialog mIndividuvalDialog, mProjectDialog;
    ArrayList<Integer> individualCheckBox;
    TextView mTextShareIndividual;
    JSONArray individuvalArray;
    TextView mIndividualsCheckBox;
    View mIndividuals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_view_tasks);
        projectListResponseRecordsArrayList = new ArrayList<ProjectListResponseRecords>();

        header();
        initializeViews();
        footer();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = (String) b.get("id");
            taskOwnerName = (String) b.get("taskOwnerName");
            mTaskTitle.setText(" " + taskOwnerName);

        }

    }

    private void header() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.txt_title);
        tv_title.setText("New Task Name");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(GONE);
        ImageView imageMenu = (ImageView) findViewById(R.id.image_menu);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get(UserPrefUtils.ID);
                String taskOwnerName = userId.get(UserPrefUtils.NAME);
                String email = userId.get(UserPrefUtils.EMAIL);
                ImageView mImageProfile1 = (ImageView) findViewById(R.id.img_profile);
                String img = userId.get(UserPrefUtils.IMAGEPATH);
                System.out.println("img" + img);
                Glide.with(getApplicationContext())
                        .load(img)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(mImageProfile1);

                mImageProfile1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), EditAccountActivity.class);
                        startActivity(i);
                    }
                });
                TextView mTextName = (TextView) findViewById(R.id.tv_nameProfile);
                mTextName.setText(taskOwnerName);
                TextView mTextEmail = (TextView) findViewById(R.id.tv_emailProfile);
                mTextEmail.setText(email);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToday = new Intent(getApplicationContext(), TodayTaskActivity.class);
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
                                Intent iTaskfilter = new Intent(getApplicationContext(), TaskAddListActivity.class);
                                startActivity(iTaskfilter);
                                break;
                            case R.id.nav_project:
                                Intent iProjects = new Intent(getApplicationContext(), ProjectFooterActivity.class);
                                startActivity(iProjects);
                                break;
                            case R.id.nav_individuals:
                                Intent iIndividuals = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
                                startActivity(iIndividuals);
                                break;
                            case R.id.nav_insights:
                                Intent iInsights = new Intent(getApplicationContext(), InsightsChart.class);
                                startActivity(iInsights);
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
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
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutTaskView);
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
        mProgressViewNEWTask =findViewById( R.id.progress_bar );
        mContentLayoutNewTask = findViewById( R.id.content_layout );
        mTaskProjectName = findViewById(R.id.et_newTaskProjectName);
        mTaskTitle = findViewById(R.id.et_newtaskTitle);
        imageView = (ImageView)findViewById(R.id.dateClearImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDueDateTask.setText(" ");
                imageView.setVisibility(GONE);
            }
        });
        mDueDateTask = findViewById(R.id.et_duedateNewTaskName);
        String due_date = mDueDateTask.getText().toString();
        if (TextUtils.isEmpty(due_date)){
            imageView.setVisibility(GONE);
        }
        mPriorty = findViewById(R.id.tv_taskPriorty);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                mDueDateTask.setText(sdf.format(myCalendar.getTime()));
            }
        };
        mDueDateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ViewTasksActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                imageView.setVisibility(View.VISIBLE);

            }
        });
        mPriorityNewTask = findViewById(R.id.re_priorityNewTask);
        listItems = getResources().getStringArray(R.array.priorty);
        checkedItems = new boolean[listItems.length];
        mPriorityNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewTasksActivity.this);
                mBuilder.setTitle("PRIORITY");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserPriorty.contains(position)) {
                                mUserPriorty.add(position);
                            } else {
                                mUserPriorty.remove(position);
                            }
                        }

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = " ";
                        for (int i = 0; i < mUserPriorty.size(); i++) {
                            item = item + listItems[mUserPriorty.get(i)];
                            if (i != mUserPriorty.size() - 1) {
                                item = item + " ";
                            }
                        }
                        mPriorty.setText(item);

                    }
                });
                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        });
        mProjectCheckBox = findViewById(R.id.tv_projectTask);
        mProjectNewTask = findViewById(R.id.re_projrectNewTask);
        mProjectNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AndroidUtils.isNetworkAvailable(getApplicationContext())) {
                    requestDynamicProjectList();
                    final Dialog dialog = new Dialog( context, android.R.style.Theme_DeviceDefault_Dialog_Alert );
                    dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                    dialog.setCancelable( true );
                    dialog.setContentView( R.layout.dailog_projectname_projectcode );
                    mProgressView = dialog.findViewById( R.id.progress_bar );
                    mContentLayout = dialog.findViewById( R.id.content_layout );
                    mRecyclerViewDateTime = dialog.findViewById( R.id.recyleView_projectNameCode );
                    mLayoutManager = new LinearLayoutManager( getApplicationContext() );
                    mRecyclerViewDateTime.setLayoutManager( mLayoutManager );
                    mRecyclerViewDateTime.setItemAnimator( new DefaultItemAnimator() );
                    mNewTaskProjectAdapter = new NewTaskProjectAdapter( projectListResponseRecordsArrayList, R.layout.custom_project_dailog, getApplicationContext() );
                    mRecyclerViewDateTime.setAdapter( mNewTaskProjectAdapter );
                    TextView tv_ok = (TextView) dialog.findViewById( R.id.tv_dailogOk );
                    tv_ok.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            projectName = mProjectNameDailog.getText().toString();
                            projectcode = mProjectCodeDailog.getText().toString();
                            mProjectCheckBox.setText( projectcode );
                            System.out.println( "projectNames" + projectcode + projectName );
                            dialog.dismiss();
                        }
                    } );
                    TextView tv_cancel = (TextView) dialog.findViewById( R.id.tv_dailogCancel );
                    tv_cancel.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    } );
                    dialog.show();
                } else {
                    attemptOfflineProjects();
                }

            }
        });
        requestIndividualDynamicContent();
        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add( 0 );
        mIndividuals = (View)findViewById(R.id.re_individuals);
        mIndividualsCheckBox =findViewById(R.id.tv_individuals);
        mIndividuals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndividuvalDialog.show( getSupportFragmentManager(), "mIndividuvalDialog" );

            }
        });
        spinnerData();

    }

    private void spinnerData() {
        mSpinnerReptOption = (Spinner) findViewById(R.id.spinnerReapt);
        mRepeatType = (View) findViewById(R.id.re_repeatTypeNewTask);
        reWeeklyView = findViewById(R.id.re_weekly);
        mWeekName = (TextView) findViewById(R.id.tv_weekly);
        listItemsWeek = getResources().getStringArray(R.array.weekdays);
        checkedItemsWeek = new boolean[listItemsWeek.length];

        reYearly = findViewById(R.id.re_yearly);
        mYearly = (TextView) findViewById(R.id.tv_yearly);
        listItemsMonths = getResources().getStringArray(R.array.month);
        checkedItemsMonths = new boolean[listItemsMonths.length];
        reMonthly = findViewById(R.id.re_monthly);
        mDates = (TextView) findViewById(R.id.tv_datesMonthly);
        listItemsDates = getResources().getStringArray(R.array.dates);
        checkedItemsDates = new boolean[listItemsDates.length];

        listItemDates1 = getResources().getStringArray(R.array.dates1);
        checkedItemsDates1 = new boolean[listItemDates1.length];


        listItemDates2 = getResources().getStringArray(R.array.dates2);
        checkedItemsDates2 = new boolean[listItemDates2.length];

        arrayAdapterReapt = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, reapt);
        mSpinnerReptOption.setAdapter(arrayAdapterReapt);
        mSpinnerReptOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repeat_type = parent.getSelectedItem().toString();
                if (repeat_type.contentEquals("Weekly")) {
                    reWeeklyView.setVisibility(View.VISIBLE);
                    reYearly.setVisibility(GONE);
                    reMonthly.setVisibility(GONE);
                }
                if (repeat_type.equals("Monthly")) {
                    reMonthly.setVisibility(View.VISIBLE);
                    reWeeklyView.setVisibility(GONE);
                    reYearly.setVisibility(GONE);

                }
                if (repeat_type.equals("Yearly")) {
                    reYearly.setVisibility(View.VISIBLE);
                    reMonthly.setVisibility(View.VISIBLE);
                    reWeeklyView.setVisibility(GONE);
                }
                if (repeat_type.equals("Daily")) {
                    reYearly.setVisibility(GONE);
                    reMonthly.setVisibility(GONE);
                    reWeeklyView.setVisibility(GONE);
                }

                if (repeat_type.equals("RepeatType")) {
                    reYearly.setVisibility(GONE);
                    reMonthly.setVisibility(GONE);
                    reWeeklyView.setVisibility(GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        reWeeklyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewTasksActivity.this);
                mBuilder.setTitle("WEEKS");
                mBuilder.setMultiChoiceItems(listItemsWeek, checkedItemsWeek, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mWeek.contains(position)) {
                                mWeek.add(position);
                            } else {
                                mWeek.remove(position);
                            }
                        }

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = " ";
                        for (int i = 0; i < mWeek.size(); i++) {
                            item = item + listItemsWeek[mWeek.get(i)];
                            if (i != mWeek.size() - 1) {
                                item = item + " ";
                            }
                        }
                        mWeekName.setText(item);
                        week_days = mWeekName.getText().toString();


                    }
                });
                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        });

        reMonthly.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewTasksActivity.this);
                LocalDate currentdate = LocalDate.now();
                Month currentMonth = currentdate.getMonth();
                System.out.println("Current month: "+currentMonth);
                if (currentMonth.equals(MARCH)) {
                    mBuilder.setTitle("SELECT THE DAY OF THE MONTH TO REPEAT");
                    mBuilder.setMultiChoiceItems(listItemDates2, checkedItemsDates2, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                            if (isChecked) {
                                if (!mUserDates2.contains(position)) {
                                    mUserDates2.add(position);
                                } else {
                                    mUserDates2.remove(position);
                                }
                            }

                        }
                    });
                }
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = " ";
                        for (int i = 0; i < mUserDates2.size(); i++) {
                            item = item + listItemDates2[mUserDates2.get(i)];
                            if (i != mUserDates2.size() - 1) {
                                item = item + " ";

                            }
                        }
                        System.out.println("item" + item);
                        mDates.setText(item);
                        days = mDates.getText().toString();

                        //mRepeatTypeTextView.setText(item);

                    }
                });
                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        reYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewTasksActivity.this);
                mBuilder.setTitle("ADD TO MONTHS");
                mBuilder.setMultiChoiceItems(listItemsMonths, checkedItemsMonths, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!uMonths.contains(position)) {
                                uMonths.add(position);
                            } else {
                                uMonths.remove(position);
                            }
                        }

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = " ";
                        for (int i = 0; i < uMonths.size(); i++) {
                            item = item + listItemsMonths[uMonths.get(i)];
                            if (i != uMonths.size() - 1) {
                                item = item + " ";
                            }
                        }
                        mYearly.setText(item);
                        months = mYearly.getText().toString();

                    }
                });
                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    private void requestDynamicProjectList() {
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse(id);
        call.enqueue(new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        setProjectFooterList(response.body().getProject_records());
                    } else {
                        Toast.makeText(ViewTasksActivity.this, "", Toast.LENGTH_SHORT).show();
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
        ProjectDBHelper projectDBHelper = new ProjectDBHelper(getApplicationContext());
        if (project_records.size() > 0) {
            for (int i = 0; project_records.size() > i; i++) {
                ProjectListResponseRecords projectListResponseRecords = project_records.get(i);
                ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
                projectListResponseRecords1.setName(projectListResponseRecords.getName());
                projectListResponseRecords1.setProject_code((projectListResponseRecords.getProject_code()));
                projectDBHelper.insertUserDetails(projectListResponseRecords1);
                projectListResponseRecordsArrayList.add(projectListResponseRecords1);
            }
            mRecyclerViewDateTime.setAdapter(new NewTaskProjectAdapter(projectListResponseRecordsArrayList, R.layout.custom_project_footer, getApplicationContext()));
            mRecyclerViewDateTime.addOnItemTouchListener(new ViewTasksActivity.RecyclerTouchListener(this, mRecyclerViewDateTime, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    mProjectNameDailog = (TextView) view.findViewById(R.id.tv_projectNameDailog);
                    projectName = mProjectNameDailog.getText().toString();
                    mProjectCodeDailog = (TextView) view.findViewById(R.id.tv_projectCodeDailog);
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

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(ViewTasksActivity context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
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

    private void requestIndividualDynamicContent() {
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
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get(i);
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setEmail(orgnUserRecordsCheckBox.getEmail());
                listOfIndividuval.add(new MultiSelectModel(Integer.parseInt(orgnUserRecordsCheckBox.getId()), orgnUserRecordsCheckBox.getEmail()));
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
                                mIndividualsCheckBox.setText(dataString);
                            }
                            individuvalArray = new JSONArray( selectedIds );
                        }

                        @Override
                        public void onCancel() {
                            Log.d("TAG", "Dialog cancelled");
                        }
                    });
        }
    }
    private void attemptOfflineProjects() {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_offline_dailog_projects);
        mProgressView = dialog.findViewById(R.id.progress_bar);
        mContentLayout = dialog.findViewById(R.id.content_layout);
        RecyclerView mRecyclerViewDateTime = dialog.findViewById(R.id.projectOfflinerecyleView_projectNameCode);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewDateTime.setLayoutManager(mLayoutManager);
        mRecyclerViewDateTime.setItemAnimator(new DefaultItemAnimator());
       NewTaskProjectAdapterOffline mNewTaskProjectAdapterOffline = new NewTaskProjectAdapterOffline(projectListResponseRecordsArrayList, R.layout.projectoffline_custom, getApplicationContext());
        mRecyclerViewDateTime.setAdapter(mNewTaskProjectAdapterOffline);
        TextView tv_okOffline = (TextView) dialog.findViewById(R.id.tv_projectOfflinedailogOk);
        tv_okOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectNameOffline = mProjectNameDailogOffline.getText().toString();
                projectCodeOffline = mProjectCodeDailogOffline.getText().toString();
                mProjectCheckBox.setText(projectCodeOffline);
                System.out.println("projectNames" + projectcode + projectName);
                dialog.dismiss();
            }
        });
        TextView tv_cancelOffline = (TextView) dialog.findViewById(R.id.tv_ProjectOfflinedailogCancel);
        tv_cancelOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        //AndroidUtils.showProgress( false, mProgressView, mContentLayout );
        ProjectDBHelper projectDBHelper = new ProjectDBHelper(getApplicationContext());
        Cursor cursor = projectDBHelper.getProjectAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
                String name = cursor.getString(cursor.getColumnIndex(projectDBHelper.KEY_NAME));
                String date = cursor.getString(cursor.getColumnIndex(projectDBHelper.KEY_DUEDATE));
                String projectCode = cursor.getString(cursor.getColumnIndex(projectDBHelper.KEY_PROJECTCODE));
                projectListResponseRecords1.setName(name);
                projectListResponseRecords1.setDue_date(date);
                projectListResponseRecords1.setProject_code(projectCode);
                projectListResponseRecordsArrayList.add(projectListResponseRecords1);
                System.out.println("project" + projectListResponseRecords1);
            }
        }
        mRecyclerViewDateTime.setAdapter(new NewTaskProjectAdapterOffline(projectListResponseRecordsArrayList, R.layout.projectoffline_custom, getApplicationContext()));
        mRecyclerViewDateTime.addOnItemTouchListener(new ViewTasksActivity.RecyclerTouchListener(this, mRecyclerViewDateTime, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mProjectNameDailogOffline = (TextView) view.findViewById(R.id.tv_projectNameDailogOFFline);
                projectNameOffline = mProjectNameDailogOffline.getText().toString();
                mProjectCodeDailogOffline = (TextView) view.findViewById(R.id.tv_projectCodeDailogOffline);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void footer() {
        ImageView imageGallery = (ImageView) findViewById(R.id.image_gallery);
        imageGallery.setVisibility(GONE);
        ImageView imageAttachament = (ImageView) findViewById(R.id.image_attachament);
        imageAttachament.setVisibility(GONE);
        ImageView imageCamera = (ImageView) findViewById(R.id.image_camera);
        imageCamera.setVisibility(GONE);
        ImageView imageProfile = (ImageView) findViewById(R.id.image_profile);
        imageProfile.setVisibility(GONE);
        TextView tv_create = (TextView) findViewById(R.id.tv_create);
        tv_create.setText("Create");
        tv_create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                if (AndroidUtils.isNetworkAvailable(getApplicationContext())) {
                    attemptCreateTask();
                } else {
                    attemptCreateOfflineTask();
                }

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void attemptCreateTask() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        String taskName = mTaskProjectName.getText().toString();
        String due_date = mDueDateTask.getText().toString();
        String priorty = mPriorty.getText().toString();
        String project_code = mProjectCheckBox.getText().toString();
        String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
        String individuvalName = String.valueOf(individuvalArray);
        individuvalArray.remove(0);
      /*  if (repeat_type.equals("RepeatType")){
            repeat_type= " ";
        }*/
        //String oldprojectsName = String.valueOf(projectArray);
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(taskName)) {
            mTaskProjectName.setError(getString(R.string.error_required));
            focusView = mTaskProjectName;
            cancel = true;


             /* if (repeat_type.equals( "" )) {
                  cancel = false;
              }else {
                  cancel = true;
              }*/
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            if (week_days != null) {
                String[] weekNumber = null;
                ArrayList<String> list = new ArrayList<>();
                if (week_days.contains("Monday")) {
                    list.add("1");
                }
                if (week_days.contains("Tuesday")) {
                    list.add("2");
                }
                if (week_days.contains("Wednesday")) {
                    list.add("3");
                }
                if (week_days.contains("Thursday")) {
                    list.add("4");
                }
                if (week_days.contains("Friday")) {
                    list.add("5");
                }
                if (week_days.contains("Saturday")) {
                    list.add("6");
                }
                if (week_days.contains("Sunday")) {
                    list.add("7");
                }
                showProgressDialog();
                //requestCrateTask( id, taskName, duedate, String.valueOf( individuvalArray ).replace( "[", "" ).replace( "]", "" ), priorty );
                requestCreateTask(id, taskName, due_date, priorty, project_code, String.valueOf( individuvalArray ).replace( "[", "" ).replace( "]", "" ),orgn_code, repeat_type, String.valueOf(list), days, months);
                System.out.println("taskcreateVlaues" + id + taskName + due_date + priorty + String.valueOf( individuvalArray ).replace( "[", "" ).replace( "]", "" )+ project_code + orgn_code + repeat_type + list + days + months);
            } else {

                requestCreateTask(id, taskName, due_date, priorty, project_code,String.valueOf( individuvalArray ).replace( "[", "" ).replace( "]", "" ), orgn_code, repeat_type, String.valueOf(week_days), days, months);

            }

        }
    }
    private void requestCreateTask(String id, String taskName, String duedate, String priorty, String project_code, String b, String orgn_code, String repeat_type, String list, String days, String months) {
        Call<TaskAddResponse> call = ANApplications.getANApi().checkTaskAddResponse(id, taskName, duedate, priorty, project_code ,orgn_code,String.valueOf( individuvalArray ).replace( "[", "" ).replace( "]", "" ), repeat_type, list, days, months);
        System.out.println("taskfelids" + id + taskName + duedate + priorty + project_code + b +orgn_code + repeat_type + week_days + days + months);
        call.enqueue(new Callback<TaskAddResponse>() {
            @Override
            public void onResponse(Call<TaskAddResponse> call, Response<TaskAddResponse> response) {
                hideProgressDialog();
                if (response.isSuccessful()) {
                    System.out.println( "severReponse:" + response.raw() );
                    if (response.body().getSuccess().equals("true")) {
                        System.out.println( "severReponse1:" + response.body().getSuccess());

                        Intent i = new Intent(getApplicationContext(), TaskAddListActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<TaskAddResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }


    /*/ OFFLINE TASK CREATE*/

    private void attemptCreateOfflineTask() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
        TaskListRecords taskListRecords = new TaskListRecords();
        String taskName = mTaskProjectName.getText().toString();
        String due_date = mDueDateTask.getText().toString();
        String priorty = mPriorty.getText().toString();
        String project_code = mProjectCheckBox.getText().toString();
        String status = "1";
        taskListRecords.setTask_id(id);
        taskListRecords.setName(taskName);
        taskListRecords.setDue_date(due_date);
        taskListRecords.setPriority(priorty);
        taskListRecords.setProject_code(project_code);
        taskListRecords.setStatus(status);
        taskListRecords.setRepeat_type(repeat_type);
        taskListRecords.setRepeat_months(months);
        taskListRecords.setRepeat_days(days);
        taskListRecords.setRepeat_weeks(week_days);
        System.out.println("TaskOfflineValues" + id + taskName + due_date + priorty + project_code + status);
        ArrayList<String> list = new ArrayList<>();
        TaskDBHelper taskDBHelper = new TaskDBHelper(ViewTasksActivity.this);
        taskDBHelper.insertTaskDetails(taskListRecords);
        Intent i = new Intent(getApplicationContext(), TaskAddListActivity.class);
        Toast.makeText(ViewTasksActivity.this, "Task Offline Created Sucessfully", Toast.LENGTH_SHORT).show();
        startActivity(i);
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getApplicationContext());
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
}
