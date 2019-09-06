package com.actnow.android.activities.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.sdk.responses.TaskAddResponse;
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

public class ViewTasksActivity extends AppCompatActivity {
    final Context context = this;
    EditText mTaskProjectName, mTaskTitle, mDueDateTask, mCommentTask;
    View mProjectNewTask, mRepeatType, mIndvalNewTask, mPriorityNewTask;
    TextView mIndividualCheckBox, mProjectCheckBox;
    TextView mRepeatTypeTextView;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();
    MultiSelectDialog mIndividuvalDialog, mProjectDialog;
    String id;
    String taskOwnerName;
    String[] reapt = {"RepeatType", "Daily", "Weekly", "Monthly", "Yearly"};

    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_view_tasks );
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
        header();
        initializeViews();
        footer();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = (String) b.get( "id" );
            taskOwnerName = (String) b.get( "taskOwnerName" );
            mTaskTitle.setText( " " + taskOwnerName );
            System.out.println( "passsed" + taskOwnerName + id );
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
        tv_title.setText( "New Task Name" );
        Spinner spinner = (Spinner) findViewById( R.id.spinner );
        spinner.setVisibility( GONE );
        ImageView imageMenu = (ImageView) findViewById( R.id.image_menu );
        imageMenu.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get( UserPrefUtils.ID );
                String taskOwnerName = userId.get( UserPrefUtils.NAME );
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
                navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToady = new Intent( getApplicationContext(), TodayTaskActivity.class );
                                startActivity( iToady );
                                finish();
                                break;
                            case R.id.nav_timeLine:
                                Toast.makeText( getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT ).show();
                                break;
                            case R.id.nav_filter:
                                Toast.makeText( getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT ).show();
                                break;
                            case R.id.nav_profile:
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get( UserPrefUtils.ID );
                                String name = userId.get( UserPrefUtils.NAME );
                                String accountEmail = userId.get( UserPrefUtils.EMAIL );
                                Intent iprofile = new Intent( ViewTasksActivity.this, EditAccountActivity.class );
                                iprofile.putExtra( "id", id );
                                iprofile.putExtra( "name", name );
                                iprofile.putExtra( "email", accountEmail );
                                startActivity( iprofile );
                                break;
                            case R.id.nav_premium:
                                Intent ipremium = new Intent( ViewTasksActivity.this, PremiumActivity.class );
                                startActivity( ipremium );
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent( ViewTasksActivity.this, ThisWeekActivity.class );
                                startActivity( ithisweek );
                                break;
                        }
                        return false;
                    }
                } );
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layoutTaskView );
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
        mTaskProjectName = findViewById( R.id.et_newTaskProjectName );
        mTaskTitle = findViewById( R.id.et_newtaskTitle );
        mDueDateTask = findViewById( R.id.et_duedateNewTaskName );

        mPriorty = findViewById( R.id.tv_taskPriorty );
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set( Calendar.YEAR, year );
                myCalendar.set( Calendar.MONTH, monthOfYear );
                myCalendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat( myFormat, Locale.UK );
                mDueDateTask.setText( sdf.format( myCalendar.getTime() ) );
            }
        };
        mDueDateTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog( ViewTasksActivity.this, datePickerListener, myCalendar
                        .get( Calendar.YEAR ), myCalendar.get( Calendar.MONTH ),
                        myCalendar.get( Calendar.DAY_OF_MONTH ) ).show();
            }
        } );
    /*    mIndvalNewTask = (View) findViewById( R.id.re_projectNewIndividual );
        mIndividualCheckBox = (TextView) findViewById( R.id.tv_individualCheckBox );*/
        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add( 0 );
       /* mIndvalNewTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndividuvalDialog.show( getSupportFragmentManager(), "mIndividuvalDialog" );
            }
        } );*/
        mProjectCheckBox = findViewById( R.id.tv_projectTask );
        mProjectNewTask = findViewById( R.id.re_projrectNewTask );
        mProjectDialog = new MultiSelectDialog();
        projectListCheckBox = new ArrayList<>();
        projectListCheckBox.add( 0 );
        mProjectNewTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProjectDialog.show( getSupportFragmentManager(), "mProjectDialog" );
            }
        } );
        mPriorityNewTask = findViewById( R.id.re_priorityNewTask );
        listItems = getResources().getStringArray( R.array.priorty );
        checkedItems = new boolean[listItems.length];
        mPriorityNewTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( ViewTasksActivity.this );
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
                        mPriorty.setText( item );

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
        //requestDynamicContent();
        requestDynamicProjectList();
        attemptCreateTask();
        spinnerData();
    }

    private void spinnerData() {
        mSpinnerReptOption = (Spinner) findViewById( R.id.spinnerReapt );
        mRepeatType = (View) findViewById( R.id.re_repeatTypeNewTask );
        reWeeklyView = findViewById( R.id.re_weekly );
        mWeekName = (TextView) findViewById( R.id.tv_weekly );
        listItemsWeek = getResources().getStringArray( R.array.weekdays );
        checkedItemsWeek = new boolean[listItemsWeek.length];

        reYearly = findViewById( R.id.re_yearly );
        mYearly = (TextView) findViewById( R.id.tv_yearly );
        listItemsMonths = getResources().getStringArray( R.array.month );
        checkedItemsMonths = new boolean[listItemsMonths.length];
        reMonthly = findViewById( R.id.re_monthly );
        mDates = (TextView) findViewById( R.id.tv_datesMonthly );
        listItemsDates = getResources().getStringArray( R.array.dates );
        checkedItemsDates = new boolean[listItemsDates.length];

        arrayAdapterReapt = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_dropdown_item_1line, reapt );
        mSpinnerReptOption.setAdapter( arrayAdapterReapt );
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( ViewTasksActivity.this );
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

        reMonthly.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( ViewTasksActivity.this );
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( ViewTasksActivity.this );
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
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse( id );
        call.enqueue( new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                //AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
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
                projectListResponseRecords1.setProject_code((projectListResponseRecords.getProject_code()));
                System.out.println( "code"+ projectListResponseRecords.getProject_code() );
                //projectcode = projectListResponseRecords1.getProject_code();
                listOfProjectNames.add( new MultiSelectModel( Integer.parseInt( projectListResponseRecords.getProject_id() ), projectListResponseRecords.getName() ) );
            }
            mProjectDialog = new MultiSelectDialog()
                    .title( "Project" ) //setting title for dialog
                    .titleSize( 25 )
                    .positiveText( "Done" )
                    .negativeText( "Cancel" )
                    .preSelectIDsList( projectListCheckBox )
                    .setMinSelectionLimit( 0 )
                    .setMaxSelectionLimit( listOfProjectNames.size() )
                    .multiSelectList( listOfProjectNames ) // the multi select model list with ids and name
                    .onSubmit( new MultiSelectDialog.SubmitCallbackListener() {
                        @Override
                        public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                            for (int i = 0; i < selectedIds.size(); i++) {
                                mProjectCheckBox.setText( dataString );
                                //mProjectCheckBox.setText(projectcode);

                            }
                            projectArray = new JSONArray( selectedIds );
                        }

                        @Override
                        public void onCancel() {
                            Log.d( "TAG", "Dialog cancelled" );
                        }
                    } );
        }

    }

    private void attemptCreateTask() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        String taskName = mTaskProjectName.getText().toString();
        String due_date = mDueDateTask.getText().toString();
        String priorty = mPriorty.getText().toString();
        String project_code = mProjectCheckBox.getText().toString();
        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
      /*  String days = mDates.getText().toString();
        String months = mYearly.getText().toString();*/
        String individuvalName = String.valueOf( individuvalArray );
        //individuvalArray.remove(0);

        String oldprojectsName = String.valueOf( projectArray );
        mDueDateTask.setError( null );
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(due_date)) {
            mDueDateTask.setError(getString( R.string.error_required));
            focusView = mDueDateTask;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            // requestCrateTask( id, taskName, duedate, String.valueOf( individuvalArray ).replace( "[", "" ).replace( "]", "" ), priorty );
            requestCreateTask(id,taskName,due_date,priorty,project_code,orgn_code,repeat_type,week_days,days,months);
            System.out.println( "data" + id + taskName + due_date+priorty + project_code +orgn_code+repeat_type+week_days+ days+months);
        }
    }

    private void requestCreateTask(String id, String taskName,String duedate, String priorty, String project_code,String orgn_code,String repeat_type,String week_days,String days,String months) {
        System.out.println("values"+ id+taskName+duedate+days+priorty+project_code+orgn_code+repeat_type+week_days+days+months);

        Call<TaskAddResponse> call = ANApplications.getANApi().checkTaskAddResponse( id, taskName, duedate, priorty,project_code,orgn_code,repeat_type,week_days,days,months);
        call.enqueue( new Callback<TaskAddResponse>() {

            @Override
            public void onResponse(Call<TaskAddResponse> call, Response<TaskAddResponse> response) {
                System.out.println("arjun"+response.raw());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        Intent i = new Intent( ViewTasksActivity.this, TaskAddListActivity.class );
                        startActivity( i );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<TaskAddResponse> call, Throwable t) {
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
        tv_create.setText( "Create" );
        tv_create.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                attemptCreateTask();
                Toast.makeText( getApplicationContext(),"selecte data",Toast.LENGTH_LONG ).show();

            }
        } );

    }


}
 /*private void requestDynamicContent() {
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
                                mIndividualCheckBox.setText( dataString );
                            }
                            individuvalArray = new JSONArray( selectedIds );
                        }

                        @Override
                        public void onCancel() {
                            Log.d( "TAG", "Dialog cancelled" );
                        }
                    } );
        }


    }*/