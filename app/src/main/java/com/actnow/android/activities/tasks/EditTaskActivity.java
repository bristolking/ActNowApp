package com.actnow.android.activities.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;

import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.adapter.NewTaskProjectAdapter;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.sdk.responses.TaskEditResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;


import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class EditTaskActivity extends AppCompatActivity {

    EditText mTaskEditName, mEditTaskOwner, mDateTaskEdit, mTaskCommentEdit;
    View mEditProjectNewTask, mRepeatType, mEditPriorityNewTask;
    View mProgressView, mContentLayout;
    final Context context = this;
    UserPrefUtils session;
    String id;
    String taskOwnerName;
    String taskName;
    String taskDate;
    String task_code;
    String name;
    String date;

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

    Spinner mSpinnerReptOption;
    ArrayAdapter<String> arrayAdapterReaptEdit;

    View reWeeklyView, reYearly, reMonthly;

    TextView mYearly, mWeekName, mDates;

    String repeat_type;
    String week_days;
    String months;
    String days;
    String projectcode;
    String[] reapt = {"RepeatType", "Daily", "Weekly", "Monthly", "Yearly"};


    TextView mEditProjectCheckBox, mEditIndividuvalCheckBox;
    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<>();
    MultiSelectDialog mProjectDialog;
    ArrayList<Integer> projectListCheckBox;
    JSONArray individuvalArray, projectArray;

    TextView mPriortyEditTask;
    String projectName;

    RecyclerView mRecyclerViewDateTime;
    NewTaskProjectAdapter mNewTaskProjectAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList = new ArrayList<ProjectListResponseRecords>();

    TextView mProjectNameDailog;
    TextView mProjectCodeDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_edit_task );
        header();
        initializeViews();
        footer();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            taskOwnerName = (String) b.get( "taskOwnerName" );
            mEditTaskOwner.setText( " " + taskOwnerName );
            taskName = (String) b.get( "TaskName" );
            mTaskEditName.setText( "" + taskName );
            taskDate = (String) b.get( "TaskDate" );
            mDateTaskEdit.setText( "" + taskDate );
            task_code = (String) b.get( "TaskCode" );
            projectName = (String) b.get( "projectName" );
            // mProjectCheckBox.setText(projectName);
            projectcode = (String) b.get( "projectCode" );
            //mEditProjectCheckBox.setText( projectcode );
            System.out.println( "passsed" + taskOwnerName + id + taskName + taskDate );
        }
    }

    private void header() {
        ImageView imgeBack = (ImageView) findViewById( R.id.image_back );
        imgeBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        TextView tv_title = (TextView) findViewById( R.id.txt_title );
        tv_title.setText( "Task Edit Name" );
        Spinner spinner = (Spinner) findViewById( R.id.spinner );
        spinner.setVisibility( View.GONE );
        ImageView imageMenu = (ImageView) findViewById( R.id.image_menu );
        imageMenu.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get( UserPrefUtils.ID );
                String taskOwnerName = userId.get( UserPrefUtils.NAME );
                String email = userId.get( UserPrefUtils.EMAIL);
                ImageView mImageProfile = (ImageView) findViewById( R.id.img_profile );
                mImageProfile.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent( getApplicationContext(), EditAccountActivity.class );
                        startActivity( i );
                    }
                } );

                TextView mTextName = (TextView) findViewById( R.id.tv_nameProfile );
                mTextName.setText( taskOwnerName );
                TextView mTextEmail =(TextView)findViewById( R.id.tv_emailProfile);
                mTextEmail.setText( email );
                navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
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
                                Intent iProjects = new Intent( getApplicationContext(), ProjectFooterActivity.class);
                                startActivity( iProjects);
                                break;
                            case R.id.nav_individuals:
                                Intent iIndividuals = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
                                startActivity(iIndividuals);
                                break;
                            case R.id.nav_insights:
                                Intent iInsights = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
                                startActivity(iInsights);
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
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
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layoutTaskEditView );
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
        mTaskEditName = findViewById( R.id.et_newEditTaskName );
        mEditTaskOwner = findViewById( R.id.et_newEdittaskOwner );
        mDateTaskEdit = findViewById( R.id.et_duedateNewEditTaskName );
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set( Calendar.YEAR, year );
                myCalendar.set( Calendar.MONTH, monthOfYear );
                myCalendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat( myFormat, Locale.UK );
                mDateTaskEdit.setText( sdf.format( myCalendar.getTime() ) );
            }
        };
        mDateTaskEdit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog( EditTaskActivity.this, datePickerListener, myCalendar
                        .get( Calendar.YEAR ), myCalendar.get( Calendar.MONTH ),
                        myCalendar.get( Calendar.DAY_OF_MONTH ) ).show();
            }
        } );
        mEditPriorityNewTask = findViewById( R.id.re_EditpriorityNewTask );
        mPriortyEditTask = (TextView) findViewById( R.id.tv_editTaskPriorty );
        listItems = getResources().getStringArray( R.array.priorty );
        checkedItems = new boolean[listItems.length];
        mEditPriorityNewTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( EditTaskActivity.this );
                mBuilder.setTitle( "ADD TO PRIORITY" );
                mBuilder.setMultiChoiceItems( listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserPriorty.contains( position )) {
                                mUserPriorty.add( position );
                            } else {
                                mUserPriorty.remove( position );
                            }
                        }

                    }
                } );
                mBuilder.setCancelable( false );
                mBuilder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = " ";
                        for (int i = 0; i < mUserPriorty.size(); i++) {
                            item = item + listItems[mUserPriorty.get( i )];
                            if (i != mUserPriorty.size() - 1) {
                                item = item + " ";
                            }
                        }
                        mPriortyEditTask.setText( item );

                    }
                } );
                mBuilder.setNegativeButton( "Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                } );
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        } );
        mEditProjectNewTask = (View) findViewById( R.id.re_EditTaskProject );
        mEditProjectCheckBox = (TextView) findViewById( R.id.tv_editTaskProject );
        mEditProjectNewTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog( context, android.R.style.Theme_DeviceDefault_Dialog_Alert );
                dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                dialog.setCancelable( true );
                dialog.setContentView( R.layout.dailog_projectname_projectcode );
                mRecyclerViewDateTime = dialog.findViewById( R.id.recyleView_projectNameCode );
                mLayoutManager = new LinearLayoutManager( getApplicationContext() );
                mRecyclerViewDateTime.setLayoutManager( mLayoutManager );
                mRecyclerViewDateTime.setItemAnimator( new DefaultItemAnimator() );
                mNewTaskProjectAdapter = new NewTaskProjectAdapter( projectListResponseRecordsArrayList, R.layout.custom_project_dailog, getApplicationContext() );
                mRecyclerViewDateTime.setAdapter( mNewTaskProjectAdapter );
                requestDynamicProjectList();
                TextView tv_ok = (TextView) dialog.findViewById( R.id.tv_dailogOk );
                tv_ok.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        projectName = mProjectNameDailog.getText().toString();
                        projectcode = mProjectCodeDailog.getText().toString();
                        mEditProjectCheckBox.setText(projectName);
                        dialog.dismiss();

                       /* if (!TextUtils.isEmpty(projectName)) {
                            dialog.dismiss();
                        }*/
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
            }
        } );
        spinnerData();
    }

    private void spinnerData() {
        mSpinnerReptOption = (Spinner) findViewById( R.id.spinnerReaptEdit );
        mRepeatType = (View) findViewById( R.id.re_repeatTypeEditTask );
        reWeeklyView = findViewById( R.id.re_weeklyEdit );
        mWeekName = (TextView) findViewById( R.id.tv_weeklyEdit );
        listItemsWeek = getResources().getStringArray( R.array.weekdays );
        checkedItemsWeek = new boolean[listItemsWeek.length];

        reYearly = findViewById( R.id.re_yearlyEdit );
        mYearly = (TextView) findViewById( R.id.tv_yearlyEdit );
        listItemsMonths = getResources().getStringArray( R.array.month );
        checkedItemsMonths = new boolean[listItemsMonths.length];
        reMonthly = findViewById( R.id.re_monthlyEdit );
        mDates = (TextView) findViewById( R.id.tv_datesMonthly );
        listItemsDates = getResources().getStringArray( R.array.dates );
        checkedItemsDates = new boolean[listItemsDates.length];

        arrayAdapterReaptEdit = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_dropdown_item_1line, reapt );
        mSpinnerReptOption.setAdapter( arrayAdapterReaptEdit );
        mSpinnerReptOption.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repeat_type = parent.getSelectedItem().toString();

                if (repeat_type.contentEquals( "Weekly" )) {
                    reWeeklyView.setVisibility( View.VISIBLE );
                    reYearly.setVisibility( GONE );
                    reMonthly.setVisibility( GONE );
                }
                if (repeat_type.equals( "Monthly" )) {
                    reMonthly.setVisibility( View.VISIBLE );
                    reWeeklyView.setVisibility( GONE );
                    reYearly.setVisibility( GONE );

                }
                if (repeat_type.equals( "Yearly" )) {
                    reYearly.setVisibility( View.VISIBLE );
                    reMonthly.setVisibility( View.VISIBLE );
                    reWeeklyView.setVisibility( GONE );
                }
                if (repeat_type.equals( "Daily" )) {
                    reYearly.setVisibility( GONE );
                    reMonthly.setVisibility( GONE );
                    reWeeklyView.setVisibility( GONE );
                }

                if (repeat_type.equals( "RepeatType" )) {
                    reYearly.setVisibility( GONE );
                    reMonthly.setVisibility( GONE );
                    reWeeklyView.setVisibility( GONE );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        } );
        reWeeklyView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( EditTaskActivity.this );
                mBuilder.setTitle( "ADD TO DATES" );
                mBuilder.setMultiChoiceItems( listItemsWeek, checkedItemsWeek, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mWeek.contains( position )) {
                                mWeek.add( position );
                            } else {
                                mWeek.remove( position );
                            }
                        }

                    }
                } );
                mBuilder.setCancelable( false );
                mBuilder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = " ";
                        for (int i = 0; i < mWeek.size(); i++) {
                            item = item + listItemsWeek[mWeek.get( i )];
                            if (i != mWeek.size() - 1) {
                                item = item + " ";
                            }
                        }

                        mWeekName.setText( item );
                        week_days = mWeekName.getText().toString();


                    }
                } );
                mBuilder.setNegativeButton( "Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                } );
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        } );

        reMonthly.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( EditTaskActivity.this );
                mBuilder.setTitle( "ADD TO DATES" );
                mBuilder.setMultiChoiceItems( listItemsDates, checkedItemsDates, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserDates.contains( position )) {
                                mUserDates.add( position );
                            } else {
                                mUserDates.remove( position );
                            }
                        }

                    }
                } );
                mBuilder.setCancelable( false );
                mBuilder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = " ";
                        for (int i = 0; i < mUserDates.size(); i++) {
                            item = item + listItemsDates[mUserDates.get( i )];
                            if (i != mUserDates.size() - 1) {
                                item = item + " ";
                            }
                        }
                        mDates.setText( item );
                        days = mDates.getText().toString();

                        //mRepeatTypeTextView.setText(item);

                    }
                } );
                mBuilder.setNegativeButton( "Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                } );
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        } );
        reYearly.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( EditTaskActivity.this );
                mBuilder.setTitle( "ADD TO MONTHS" );
                mBuilder.setMultiChoiceItems( listItemsMonths, checkedItemsMonths, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!uMonths.contains( position )) {
                                uMonths.add( position );
                            } else {
                                uMonths.remove( position );
                            }
                        }

                    }
                } );
                mBuilder.setCancelable( false );
                mBuilder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = " ";
                        for (int i = 0; i < uMonths.size(); i++) {
                            item = item + listItemsMonths[uMonths.get( i )];
                            if (i != uMonths.size() - 1) {
                                item = item + " ";
                            }
                        }
                        mYearly.setText( item );
                        months = mYearly.getText().toString();


                    }
                } );
                mBuilder.setNegativeButton( "Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                } );
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        } );
    }


    private void requestDynamicProjectList() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse( id );
        call.enqueue( new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                System.out.println( "api" + response.raw() );
                if (response.isSuccessful()) {
                    System.out.println( "data" + response.raw() );
                    if (response.body().getSuccess().equals( "true" )) {
                        setProjectFooterList( response.body().getProject_records() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<ProjectListResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );
            }
        } );

    }

    private void setProjectFooterList(List<ProjectListResponseRecords> project_records) {
        if (project_records.size() > 0) {
            for (int i = 0; project_records.size() > i; i++) {
                ProjectListResponseRecords projectListResponseRecords = project_records.get( i );
                ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
                projectListResponseRecords1.setName( projectListResponseRecords.getName());
                projectListResponseRecords1.setProject_code( (projectListResponseRecords.getProject_code()));
                projectListResponseRecords1.setCreated_date(projectListResponseRecords.getCreated_date());
                projectListResponseRecordsArrayList.add( projectListResponseRecords1 );
            }
            mRecyclerViewDateTime.setAdapter( new NewTaskProjectAdapter( projectListResponseRecordsArrayList, R.layout.custom_project_dailog, getApplicationContext() ) );
            mRecyclerViewDateTime.addOnItemTouchListener( new EditTaskActivity.RecyclerTouchListener( this, mRecyclerViewDateTime, new EditTaskActivity.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    mProjectNameDailog = (TextView) view.findViewById( R.id.tv_projectNameDailog );
                    projectName = mProjectNameDailog.getText().toString();
                    mProjectCodeDailog = (TextView) view.findViewById( R.id.tv_projectCodeDailog );
                    System.out.println( "projectName" + projectName + " " + mProjectCodeDailog.getText().toString() );

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

    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     * <p>
     * - creating an innerclass implementing RevyvlerView.OnItemTouchListener
     * - Pass clickListener interface as parameter
     */
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private EditTaskActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(EditTaskActivity context, final RecyclerView mRecylerViewSingleSub, EditTaskActivity.ClickListener clickListener) {
            this.clicklistener = clickListener;

            gestureDetector = new GestureDetector( context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
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

    private void attemptUpdateTask() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        String taskName = mTaskEditName.getText().toString();
        String due_date = mDateTaskEdit.getText().toString();
        String priorty = mPriortyEditTask.getText().toString();
        String project_code = mEditProjectCheckBox.getText().toString();
        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
        String individuvalName = String.valueOf( individuvalArray );
        //individuvalArray.remove(0);

        String oldprojectsName = String.valueOf( projectArray );
        mDateTaskEdit.setError( null );
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty( due_date )) {
            mDateTaskEdit.setError( getString( R.string.error_required ) );
            focusView = mDateTaskEdit;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            if (week_days != null) {
                String[] weekNumber = null;
                ArrayList<String> list = new ArrayList<>();
                if (week_days.contains( "Monday" )) {
                    list.add( "1" );
                }
                if (week_days.contains( "Tuesday" )) {
                    list.add( "2" );
                }
                if (week_days.contains( "Wednesday" )) {
                    list.add( "3" );
                }
                if (week_days.contains( "Thursday" )) {
                    list.add( "4" );
                }
                if (week_days.contains( "Friday" )) {
                    list.add( "5" );
                }
                if (week_days.contains( "Saturday" )) {
                    list.add( "6" );
                }
                if (week_days.contains( "Sunday" )) {
                    list.add( "7" );
                }
            requestUpdateTasks( id, task_code, taskName, due_date, priorty, project_code, orgn_code, repeat_type, String.valueOf( list ), days, months );
        }else {
                requestUpdateTasks( id, task_code, taskName, due_date, priorty, project_code, orgn_code, repeat_type, String.valueOf( week_days ), days, months );
            }
        }
    }

    private void requestUpdateTasks(String id, String task_code, String taskName, String duedate, String priorty, String project_code, String orgn_code, String repeat_type, String list, String days, String months) {
        System.out.println( "values" + id + taskName + duedate + days + priorty + project_code + orgn_code + repeat_type + week_days + days + months );

        Call<TaskEditResponse> call = ANApplications.getANApi().checkTheTaskEditReponse( id, task_code, taskName, duedate, priorty, project_code, orgn_code, repeat_type, list, days, months );
        call.enqueue( new Callback<TaskEditResponse>() {

            @Override
            public void onResponse(Call<TaskEditResponse> call, Response<TaskEditResponse> response) {
                System.out.println( "arjun" + response.raw() );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        Intent i = new Intent( EditTaskActivity.this, TaskAddListActivity.class );
                        startActivity( i );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<TaskEditResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );
            }
        } );
    }


    private void footer() {
        ImageView imageGallery = (ImageView) findViewById( R.id.image_gallery );
        imageGallery.setVisibility( GONE );
        ImageView imageAttachament = (ImageView) findViewById( R.id.image_attachament );
        imageAttachament.setVisibility( GONE );

        ImageView imageCamera = (ImageView) findViewById( R.id.image_camera );
        imageCamera.setVisibility( GONE );
        ImageView imageProfile = (ImageView) findViewById( R.id.image_profile );
        imageProfile.setVisibility( GONE );

        TextView tv_create = (TextView) findViewById( R.id.tv_create );
        tv_create.setText( "Update" );
        tv_create.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptUpdateTask();
            }
        } );
    }


}
