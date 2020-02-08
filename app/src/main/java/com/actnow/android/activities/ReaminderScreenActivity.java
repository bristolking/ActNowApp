package com.actnow.android.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.ReminderListAdapter;
import com.actnow.android.databse.ReminderDBHelper;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.ReminderAdd;
import com.actnow.android.sdk.responses.ReminderResponse;
import com.actnow.android.sdk.responses.ReminderTaskReinders;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.Gravity.NO_GRAVITY;
import static android.view.View.GONE;
import static com.activeandroid.Cache.getContext;

public class ReaminderScreenActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    final Context context = this;
    RecyclerView mReminderTaskList;
    RecyclerView.LayoutManager mLayoutManager;
    ReminderListAdapter mReminderListAdapter;
    private ArrayList<ReminderTaskReinders> reminderTaskReindersArrayList = new ArrayList<ReminderTaskReinders>();
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    String task_code;

    int mDay, mMonth, mYear, mHour, mMinute, mSeconds;

    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal, secondsFinal;


    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();

    MultiSelectDialog mIndividuvalDialog;
    ArrayList<Integer> individualCheckBox;
    JSONArray individuvalArray;


    TextView mUserReminder;
    TextView mReminderUserTaskId;
    TextView mDateandTime;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_reaminder_screen );

        mReminderTaskList = findViewById( R.id.reminder_Tasklist );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mReminderTaskList.setLayoutManager( mLayoutManager );
        mReminderTaskList.setItemAnimator( new DefaultItemAnimator() );
        mReminderListAdapter = new ReminderListAdapter( reminderTaskReindersArrayList, R.layout.reminder_time_date, getApplicationContext() );
        mReminderTaskList.setAdapter( mReminderListAdapter );
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            task_code = (String) b.get( "TaskCode" );
            System.out.println( "taskCode" + task_code );

        }
        appHeaderTwo();
        initializeViews();
        footer();
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
        btnLink1.setText( "Reminder" );
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
                System.out.println( "img" + img );
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
                                Intent ithisweek = new Intent( getApplicationContext(), ThisWeekActivity.class );
                                startActivity( ithisweek );
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
                                Intent iInsights = new Intent( getApplicationContext(), DailyTaskChartActivity.class );
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
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_reaminderList );
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
        if (AndroidUtils.isNetworkAvailable( getApplicationContext() )) {
            attemptCreteReminderList();
        } else {
            attemptOfflineReminder();
        }
        requestDynamicContent();
        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add( 0 );
        mUserReminder = (TextView) findViewById( R.id.tv_reminderUsers );
        mDateandTime = (TextView) findViewById( R.id.tv_dateandTime );
    }


    private void attemptCreteReminderList() {
        HashMap<String, String> usierId = session.getUserDetails();
        String id = usierId.get( UserPrefUtils.ID );
        String orgn_code = usierId.get( UserPrefUtils.ORGANIZATIONNAME );
        Call<ReminderResponse> call = ANApplications.getANApi().checkTheReminderList( id, task_code, orgn_code );
        System.out.println( "content" + id + task_code + orgn_code );
        call.enqueue( new Callback<ReminderResponse>() {
            @Override
            public void onResponse(Call<ReminderResponse> call, Response<ReminderResponse> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setRemiderTaskList( response.body().getTask_reminders() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );

                }
            }

            @Override
            public void onFailure(Call<ReminderResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );

            }
        } );

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu( this, view, Gravity.RIGHT | NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0 );
        popupMenu.inflate( R.menu.delete );
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        HashMap<String, String> userId = session.getUserDetails();
                        String id = userId.get( UserPrefUtils.ID );
                        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                        String reminderTaskId = mReminderUserTaskId.getText().toString();
                        Call<ReminderAdd> reminderDeleteCall = ANApplications.getANApi().checkTheReminderDelete( id, reminderTaskId, orgn_code );
                        System.out.println( "deleteValues" + id + orgn_code + reminderTaskId );
                        reminderDeleteCall.enqueue( new Callback<ReminderAdd>() {
                            @Override
                            public void onResponse(Call<ReminderAdd> call, Response<ReminderAdd> response) {
                                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                                if (response.isSuccessful()) {
                                    System.out.println( "deletCall" + response.raw() );
                                    if (response.body().getSuccess().equals( "true" )) {
                                        Intent i = new Intent( getApplicationContext(), ReaminderScreenActivity.class );
                                        i.putExtra( "TaskCode", task_code );
                                        startActivity( i );
                                        Toast.makeText( getApplicationContext(), "Reminder deleted successfully", Toast.LENGTH_SHORT ).show();
                                    } else {
                                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                    }
                                } else {
                                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );

                                }
                            }

                            @Override
                            public void onFailure(Call<ReminderAdd> call, Throwable t) {

                            }
                        } );

                        return true;
                    default:
                        return false;
                }
            }
        } );
    }

    private void requestDynamicContent() {

        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse( id );
        call.enqueue( new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
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
                    .setMaxSelectionLimit( 5 )
                    .multiSelectList( listOfIndividuval ) // the multi select model list with ids and name
                    .onSubmit( new MultiSelectDialog.SubmitCallbackListener() {
                        @Override
                        public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                            for (int i = 0; i < selectedIds.size(); i++) {
                                mUserReminder.setText( dataString );
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

    private void setRemiderTaskList(List<ReminderTaskReinders> reminderTaskReindersList) {
        ReminderDBHelper reminderDBHelper = new ReminderDBHelper( this);
        if (reminderTaskReindersList.size() > 0) {
            for (int i = 0; reminderTaskReindersList.size() > i; i++) {
                ReminderTaskReinders reminderTaskReinders = reminderTaskReindersList.get( i );
                ReminderTaskReinders reminderTaskReinders1 = new ReminderTaskReinders();
                reminderTaskReinders1.setReminder_date( reminderTaskReinders.getReminder_date() );
                reminderTaskReinders1.setReminder_task_id( reminderTaskReinders.getReminder_task_id() );
                reminderTaskReinders1.setTask_code( reminderTaskReinders.getTask_code());
                reminderTaskReinders1.setStatus( reminderTaskReinders.getStatus());
                reminderTaskReindersArrayList.add( reminderTaskReinders );
                reminderDBHelper.insertUserReminder(reminderTaskReinders1);
                System.out.println( "index: " + i + " database: " + id );
            }
        }
        mReminderTaskList.setAdapter( new ReminderListAdapter( reminderTaskReindersArrayList, R.layout.reminder_time_date, getApplicationContext() ) );
        mReminderTaskList.addOnItemTouchListener( new ReaminderScreenActivity.RecyclerTouchListener( this, mReminderTaskList, new ReaminderScreenActivity.ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                mReminderUserTaskId = (TextView) view.findViewById( R.id.tv_riminderTaslkId );
                ImageView mReminderMenu = (ImageView) view.findViewById( R.id.img_menuComment );
                mReminderMenu.setOnClickListener( new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        showPopup( view );
                    }
                } );
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        } ) );
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ReaminderScreenActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(ReaminderScreenActivity context, final RecyclerView mRecylerViewSingleSub, ReaminderScreenActivity.ClickListener clickListener) {
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
    private void footer() {
        ImageView mUserIntivationReminder = (ImageView) findViewById( R.id.img_individualReminder );
        mUserIntivationReminder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndividuvalDialog.show( getSupportFragmentManager(), "mIndividuvalDialog" );

            }
        } );
        ImageView mImageSend = (ImageView) findViewById( R.id.sendImg_reminder );
        mImageSend.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                addTheReminder();
            }
        } );
        ImageView mCalenderImageView = (ImageView) findViewById( R.id.calender_img );
        mCalenderImageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get( Calendar.YEAR );
                mMonth = calendar.get( Calendar.MONTH );
                mDay = calendar.get( Calendar.DAY_OF_MONTH );
                DatePickerDialog datePickerDialog = new DatePickerDialog( ReaminderScreenActivity.this, ReaminderScreenActivity.
                        this, mYear, mMonth, mDay );
                datePickerDialog.show();

            }
        } );


    }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1;
        dayFinal = i2;

        Calendar calendarTime = Calendar.getInstance();
        mHour = calendarTime.get( Calendar.HOUR_OF_DAY );
        mMinute = calendarTime.get( Calendar.MINUTE );
        mSeconds = calendarTime.get( Calendar.SECOND );
        TimePickerDialog timePickerDialog = new TimePickerDialog( ReaminderScreenActivity.
                this, ReaminderScreenActivity.this, mHour, mMinute, DateFormat.is24HourFormat( this ) );
        timePickerDialog.show();
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinal = i;
        minuteFinal = i1;
        mDateandTime.setText( yearFinal + "-" + monthFinal + "-" + dayFinal + " " + hourFinal + ":" + minuteFinal );
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addTheReminder() {
        String datetime = mDateandTime.getText().toString();
        mDateandTime.setError( null );
        individuvalArray.remove( 0 );
        String individuvalName = String.valueOf( individuvalArray );
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty( datetime )) {
            mDateandTime.setError(getString( R.string.error_required ) );
            focusView = mDateandTime;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            HashMap<String, String> userId = session.getUserDetails();
            String id = userId.get( UserPrefUtils.ID );
            String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
            addtheremdinerList( id, task_code, datetime, String.valueOf( individuvalArray ).replace( "[", "" ).replace( "]", "" ), orgn_code );
        }
    }

    private void addtheremdinerList(String a, String b, String c, String d, String e) {
        Call<ReminderAdd> call = ANApplications.getANApi().checTheReminderAdd( a, b, c, d, e );
        System.out.println( "reminderFelids" + a + b + c + String.valueOf( individuvalArray ).replace( "[", "" ).replace( "]", "" ) + e );
        call.enqueue( new Callback<ReminderAdd>() {
            @Override
            public void onResponse(Call<ReminderAdd> call, Response<ReminderAdd> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    System.out.println( "reminderReponse" + response.raw() );
                    if (response.body().getSuccess().equals( "true" )) {
                        Intent i = new Intent( getApplicationContext(), ReaminderScreenActivity.class );
                        i.putExtra( "TaskCode", task_code );
                        startActivity( i );
                        Snackbar.make( mContentLayout, "Reminder added successfully", Snackbar.LENGTH_SHORT ).show();
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<ReminderAdd> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );

            }
        } );
    }
    /// offline Data
    private void attemptOfflineReminder() {
        AndroidUtils.showProgress( false, mProgressView, mContentLayout);
        ReminderDBHelper reminderDBHelper = new ReminderDBHelper( this);
        Cursor cursor = reminderDBHelper.getReminderAllData();
        if (cursor.getCount()!= 0){
            while (cursor.moveToNext()){
                ReminderTaskReinders reminderTaskReinders = new ReminderTaskReinders();
                String reminderTaskCode = cursor.getString(cursor.getColumnIndex( reminderDBHelper.KEY_REMINDER_TASK_CODE ));
                String reminderDate = cursor.getString( cursor.getColumnIndex( reminderDBHelper.KEY_REMINDER_DATE ));
                String reminderUserId = cursor.getString( cursor.getColumnIndex( reminderDBHelper.KEY_REMINDER_STATUS ));
                String reminderCreateDate =cursor.getString( cursor.getColumnIndex( reminderDBHelper.KEY_REMINDER_CREATED_DATE ));
                String reminderTo = cursor.getString( cursor.getColumnIndex( reminderDBHelper.KEY_REMINDER_REMID_TO ));
                String reminderUserName = cursor.getString(cursor.getColumnIndex( reminderDBHelper.KEY_REMINDER_USER_NAME ));
                reminderTaskReinders.setTask_code( reminderTaskCode );
                reminderTaskReinders.setReminder_date( reminderDate );
                reminderTaskReinders.setUser_id( reminderUserId );
                reminderTaskReinders.setCreated_date( reminderCreateDate );
                reminderTaskReinders.setRemind_to( reminderTo );
                reminderTaskReinders.setUser_name( reminderUserName );
                reminderTaskReindersArrayList.add( reminderTaskReinders );
            }


        }
        mReminderTaskList.setAdapter( new ReminderListAdapter( reminderTaskReindersArrayList, R.layout.reminder_time_date, getApplicationContext() ) );
        mReminderTaskList.addOnItemTouchListener( new ReaminderScreenActivity.RecyclerTouchListener( this, mReminderTaskList, new ReaminderScreenActivity.ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                mReminderUserTaskId = (TextView) view.findViewById( R.id.tv_riminderTaslkId );
                ImageView mReminderMenu = (ImageView) view.findViewById( R.id.img_menuComment );
                mReminderMenu.setOnClickListener( new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        showPopupOffline( view );
                    }
                } );
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        } ) );



    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void showPopupOffline(View view) {
        PopupMenu popupMenu = new PopupMenu( this, view, Gravity.RIGHT | NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0 );
        popupMenu.inflate( R.menu.delete );
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        Toast.makeText(getApplicationContext(),"WORK IN PROGRESS!",Toast.LENGTH_LONG ).show();
                        return true;
                    default:
                        return false;
                }

            }
        } );

    }


    public void onBackPressed() {
        super.onBackPressed();
    }
}
