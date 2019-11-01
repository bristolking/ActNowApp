package com.actnow.android.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.ReminderListAdapter;
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

import static android.view.View.GONE;
import static com.activeandroid.Cache.getContext;

public class ReaminderScreenActivity extends AppCompatActivity {
    final Context context = this;
    RecyclerView mReminderTaskList;
    RecyclerView.LayoutManager mLayoutManager;
    ReminderListAdapter mReminderListAdapter;
    private ArrayList<ReminderTaskReinders> reminderTaskReindersArrayList = new ArrayList<ReminderTaskReinders>();
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    String task_code;

    private int mYear, mMonth, mDay, mHour, mMinute;


    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();

    MultiSelectDialog mIndividuvalDialog, mProjectDialog;
    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;

     TextView mDateReminderTextView;
     TextView mTimeReminderScreen;
     TextView mUserReminder;

     TextView mReminderUserTaskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_reaminder_screen );

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
        ImageView btnCalendar = (ImageView) findViewById( R.id.btn_calendarAppHeaderTwo );
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
                String email = userId.get( UserPrefUtils.EMAIL);
                ImageView mImageProfile = (ImageView) findViewById( R.id.img_profile );
                String img = userId.get( UserPrefUtils.IMAGEPATH);
                System.out.println( "img"+ img );
                Glide.with(getApplicationContext())
                        .load(img)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(mImageProfile);
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
                                Intent iTaskfilter = new Intent(getApplicationContext(), TaskAddListActivity.class);
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
                            case R.id.nav_logout:
                                session.logoutUser();
                                break;

                        }
                        return false;
                    }
                });
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
        requestDynamicContent();
        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add(0);
        mReminderTaskList = findViewById( R.id.reminder_Tasklist );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mReminderTaskList.setLayoutManager( mLayoutManager );
        mReminderTaskList.setItemAnimator( new DefaultItemAnimator() );
        mReminderListAdapter = new ReminderListAdapter( reminderTaskReindersArrayList, R.layout.reminder_time_date, getApplicationContext() );
        mReminderTaskList.setAdapter( mReminderListAdapter );
        HashMap<String, String> usierId = session.getUserDetails();
        String id = usierId.get( UserPrefUtils.ID );
        String orgn_code = usierId.get( UserPrefUtils.ORGANIZATIONNAME );
        Call<ReminderResponse> call = ANApplications.getANApi().checkTheReminderList( id, task_code, orgn_code );
        System.out.println( "content" + id + task_code + orgn_code );
        call.enqueue( new Callback<ReminderResponse>() {
            @Override
            public void onResponse(Call<ReminderResponse> call, Response<ReminderResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setRemiderTaskList( response.body().getTask_reminders() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                      AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");

                }
            }

            @Override
            public void onFailure(Call<ReminderResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );

            }
        } );
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addTheReminder() {
        String datetime = mDateReminderTextView.getText().toString();
        mDateReminderTextView.setError( null );
        String time = mTimeReminderScreen.getText().toString();
        //String individuvalName = String.valueOf( individuvalArray );
        String individuvalName = String.valueOf( String.valueOf(individuvalArray).replace("[", "").replace("]", "") );
        individuvalArray.remove(0);
        mTimeReminderScreen.setError( null );
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(datetime)) {
            mDateReminderTextView.setError(getString( R.string.error_required));
            focusView = mDateReminderTextView;
            cancel = true;
        }
        if (TextUtils.isEmpty(time)) {
            mTimeReminderScreen.setError(getString( R.string.error_required));
            focusView = mTimeReminderScreen;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            HashMap<String,String> userId = session.getUserDetails();
            String id = userId.get( UserPrefUtils.ID);
            String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME);
            addtheremdinerList(id,datetime,individuvalName,orgn_code);
        }
    }
    private void addtheremdinerList(String a, String b, String c, String d) {
        Call<ReminderAdd> call = ANApplications.getANApi().checTheReminderAdd( a,task_code,b,c,d);
        System.out.println( "reminderFelids"+a+task_code+b+c+d);
        call.enqueue( new Callback<ReminderAdd>() {
            @Override
            public void onResponse(Call<ReminderAdd> call, Response<ReminderAdd> response) {
                if (response.isSuccessful()){
                    System.out.println( "reminderReponse"+response.raw());
                    if (response.body().getSuccess().equals( "true" )){
                        Intent i =new Intent( getApplicationContext(),ReaminderScreenActivity.class);
                        i.putExtra( "TaskCode", task_code );
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Reminder added successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<ReminderAdd> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);

            }
        } );
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showPopup(View view){
        final PopupMenu popupMenu = new PopupMenu( this,view );
        popupMenu.inflate( R.menu.delete);
        popupMenu.setGravity( Gravity.RIGHT|Gravity.CENTER);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_delete :
                        HashMap<String,String> userId = session.getUserDetails();
                        String id= userId.get(UserPrefUtils.ID);
                        String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
                        String reminderTaskId = mReminderUserTaskId.getText().toString();
                        Call<ReminderAdd> reminderDeleteCall = ANApplications.getANApi().checkTheReminderDelete(id,reminderTaskId,orgn_code);
                        System.out.println("deleteValues"+id+orgn_code+reminderTaskId);
                        reminderDeleteCall.enqueue( new Callback<ReminderAdd>() {
                            @Override
                            public void onResponse(Call<ReminderAdd> call, Response<ReminderAdd> response) {
                                if (response.isSuccessful()){
                                    System.out.println( "deletCall"+response.raw());
                                    if (response.body().getSuccess().equals("true")){
                                        Intent  i= new Intent(getApplicationContext(),ReaminderScreenActivity.class);
                                        startActivity(i);
                                        Toast.makeText(getApplicationContext(), "Reminder deleted successfully", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                                    }
                                }else { AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");

                                }
                            }

                            @Override
                            public void onFailure(Call<ReminderAdd> call, Throwable t) {

                            }
                        } );

                        return  true;
                    default:
                        return  false;
                }
            }
        } );
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
                                mUserReminder.setText(dataString);
                            }
                            individuvalArray = new JSONArray(selectedIds);
                        }

                        @Override
                        public void onCancel()  {
                            Log.d("TAG", "Dialog cancelled");
                        }
                    });
        }
    }

    private void setRemiderTaskList(List<ReminderTaskReinders> reminderTaskReindersList) {
        if (reminderTaskReindersList.size() > 0) {
            for (int i = 0; reminderTaskReindersList.size() > i; i++) {
                ReminderTaskReinders reminderTaskReinders = reminderTaskReindersList.get( i );
                ReminderTaskReinders reminderTaskReinders1 = new ReminderTaskReinders();
                reminderTaskReinders1.setReminder_date( reminderTaskReinders.getReminder_date() );
                reminderTaskReinders1.setReminder_task_id( reminderTaskReinders.getReminder_task_id() );
                reminderTaskReinders1.setStatus( reminderTaskReinders.getStatus());
                reminderTaskReindersArrayList.add( reminderTaskReinders );

            }
        }
        mReminderTaskList.setAdapter( new ReminderListAdapter( reminderTaskReindersArrayList, R.layout.reminder_time_date, getApplicationContext() ) );
        mReminderTaskList.addOnItemTouchListener( new ReaminderScreenActivity.RecyclerTouchListener( this, mReminderTaskList, new ReaminderScreenActivity.ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                mReminderUserTaskId=(TextView)view.findViewById(R.id.tv_riminderTaslkId);
                ImageView mReminderMenu =(ImageView)view.findViewById(R.id.img_menuComment);
                mReminderMenu.setOnClickListener( new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        showPopup(view);

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
        mUserReminder =(TextView)findViewById( R.id.tv_reminderUsers);
        ImageView mImageSend = (ImageView) findViewById( R.id.sendImg_reminder );
        mImageSend.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                addTheReminder();
            }
        } );
        mDateReminderTextView = (TextView) findViewById( R.id.tv_dateandTimeReminder );
        mDateReminderTextView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get( Calendar.YEAR );
                    mMonth = c.get( Calendar.MONTH );
                    mDay = c.get( Calendar.DAY_OF_MONTH );
                    DatePickerDialog datePickerDialog = new DatePickerDialog( ReaminderScreenActivity.this, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mDateReminderTextView.setText( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );

                        }
                    }, mYear, mMonth, mDay );
                    datePickerDialog.show();

            }
        } );
        mTimeReminderScreen = (TextView) findViewById( R.id.tv_timeReminderScreen );
        mTimeReminderScreen.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final Calendar c1 = Calendar.getInstance();
                    mHour = c1.get(Calendar.HOUR_OF_DAY);
                    mMinute = c1.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ReaminderScreenActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    mTimeReminderScreen .setText(hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }

        } );

        ImageView mUserIntivationReminder =(ImageView)findViewById( R.id.img_individualReminder);
        mUserIntivationReminder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndividuvalDialog.show(getSupportFragmentManager(), "mIndividuvalDialog");

            }
        } );
    }



}
