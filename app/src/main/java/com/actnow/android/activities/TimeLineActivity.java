package com.actnow.android.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.NewTaskProjectAdapter;
import com.actnow.android.adapter.TimeLineAdapter;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.sdk.responses.TimeLineRecordsTaskList;
import com.actnow.android.sdk.responses.TimeLineTaskList;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.Gravity.NO_GRAVITY;

public class TimeLineActivity extends AppCompatActivity {
    final Context context = this;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserPriorty = new ArrayList<>();
    UserPrefUtils session;

    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;

    View mProgressView, mContentLayout;
    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();
    String id;
    MultiSelectDialog mIndividuvalDialogtime, mProjectDialogtime;

    EditText mTaskQucikSearch;
    Button mButtonAdavancedSearch;
    ImageView mImageBulbTask;

    private int mYear, mMonth, mDay;

    RecyclerView mRecylerViewTimeline;
    RecyclerView.LayoutManager mLayoutManager;
    TimeLineAdapter mTimeLineAdapter;
    ArrayList<TimeLineRecordsTaskList> timeLineRecordsTaskListArrayList = new ArrayList<TimeLineRecordsTaskList>();

    TextView tv_individual;
    TextView tv_projectCode;
    TextView tv_date;
    TextView tv_timeLineActionType;
    Button mButtonTimeLIneInfo;

    RecyclerView mRecyclerViewDateTime;
    NewTaskProjectAdapter mNewTaskProjectAdapter;
    RecyclerView.LayoutManager mProjectLayoutManager;
    private ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList = new ArrayList<ProjectListResponseRecords>();

    String projectcode;
    String projectName;

    TextView mProjectNameDailog;
    TextView mProjectCodeDailog;

    String date;
    String projectCode;
    String action;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_time_line );
        mProjectDialogtime = new MultiSelectDialog();
        projectListCheckBox = new ArrayList<>();
        projectListCheckBox.add( 0 );

        mIndividuvalDialogtime = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add( 0 );
        header();
        initializeViews();
        appFooter();
        requestDynamicContent();
    }

    private void header() {
        ImageView imgeBack = (ImageView) findViewById( R.id.image_backTimeLine );
        imgeBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        TextView tv_title = (TextView) findViewById( R.id.txt_titlesTimeLine );
        tv_title.setText( "Time Line" );
        ImageView mImgeProjectFilter = (ImageView) findViewById( R.id.img_filterProject );
        mImgeProjectFilter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectDailog();
            }
        } );
        tv_individual = (TextView) findViewById( R.id.txt_timelineuserId );
        tv_projectCode = (TextView) findViewById( R.id.txt_timeLineProjectCode );
        tv_date = (TextView) findViewById( R.id.txt_timeLineMainDate );
        tv_timeLineActionType =(TextView)findViewById(R.id.txt_timelineActoinType);
        ImageView imgCalnder = (ImageView) findViewById( R.id.img_calenderTimeLine );
        imgCalnder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get( Calendar.YEAR );
                mMonth = c.get( Calendar.MONTH );
                mDay = c.get( Calendar.DAY_OF_MONTH );
                DatePickerDialog datePickerDialog = new DatePickerDialog( TimeLineActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tv_date.setText( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );
                    }
                }, mYear, mMonth, mDay );
                datePickerDialog.show();

            }
        } );

        ImageView imgeMenuEvent = (ImageView) findViewById( R.id.img_byEvent );
        imgeMenuEvent.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                showPopupTime( view );
            }
        } );
    }

    private void projectDailog() {
        requestDynamicProjectList();
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView( R.layout.dailog_projectname_projectcode);
        mRecyclerViewDateTime = dialog.findViewById(R.id.recyleView_projectNameCode);
        mProjectLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewDateTime.setLayoutManager(mProjectLayoutManager);
        mRecyclerViewDateTime.setItemAnimator(new DefaultItemAnimator());
        mNewTaskProjectAdapter = new NewTaskProjectAdapter(projectListResponseRecordsArrayList, R.layout.custom_project_dailog, getApplicationContext());
        mRecyclerViewDateTime.setAdapter(mNewTaskProjectAdapter);
        TextView tv_ok =(TextView)dialog.findViewById(R.id.tv_dailogOk);
        tv_ok.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectName = mProjectNameDailog.getText().toString();
                projectcode = mProjectCodeDailog.getText().toString();
                tv_projectCode.setText(projectcode);
                dialog.dismiss();


            }
        } );
        TextView tv_cancel =(TextView)dialog.findViewById(R.id.tv_dailogCancel);
        tv_cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        } );
        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void showPopupTime(View view) {
        PopupMenu popupMenu = new PopupMenu( this, view, Gravity.RIGHT | NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0 );
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate( R.menu.menu_time_line, popupMenu.getMenu() );
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.by_eventtype:
                        listItems = getResources().getStringArray( R.array.eventtype );
                        checkedItems = new boolean[listItems.length];
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder( TimeLineActivity.this );
                        mBuilder.setTitle( "Filter by event type" );
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
                                tv_timeLineActionType.setText(item);
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
                        return true;
                    case R.id.by_collaborater:
                        mIndividuvalDialogtime.show( getSupportFragmentManager(), "mIndividuvalDialog" );
                        Snackbar.make( mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT ).show();

                        return true;
                    default:
                        return false;
                }
            }
        } );
    }

    private void initializeViews() {
        mProgressView = findViewById( R.id.progress_bar );
        mContentLayout = findViewById( R.id.content_layout );

        mButtonTimeLIneInfo = (Button)findViewById(R.id.bt_getInfoTimeLIne);
        mButtonTimeLIneInfo.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                getInfo();
            }
        } );

        mImageBulbTask = findViewById( R.id.image_bulbTask );
        mImageBulbTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), ViewIdeasActivity.class );
                startActivity( i );
            }
        } );
        mTaskQucikSearch = findViewById( R.id.edit_searchTask );
        mTaskQucikSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "Work in Progress!", Toast.LENGTH_LONG ).show();

            }
        } );
        mButtonAdavancedSearch = findViewById( R.id.button_searchTask );
        mButtonAdavancedSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), AdvancedSearchActivity.class );
                startActivity( i );
            }
        } );
        mRecylerViewTimeline = (RecyclerView) findViewById( R.id.recyclerViewTimeline );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mRecylerViewTimeline.setLayoutManager( mLayoutManager );
        mRecylerViewTimeline.setItemAnimator( new DefaultItemAnimator() );
        mTimeLineAdapter = new TimeLineAdapter( timeLineRecordsTaskListArrayList, R.layout.timeline_custom, getApplicationContext() );
        mRecylerViewTimeline.setAdapter( mTimeLineAdapter );
        showProgressDialog();
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
        Call<TimeLineTaskList> timeLineTaskListCall = ANApplications.getANApi().checktheTimeLineTaskList( id, "", "", "", "" );
        timeLineTaskListCall.enqueue( new Callback<TimeLineTaskList>() {
            @Override
            public void onResponse(Call<TimeLineTaskList> call, Response<TimeLineTaskList> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setTimeLIneRecords( response.body().getTimeline_records());
                        hideProgressDialog();
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<TimeLineTaskList> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );

            }
        } );

    }
    private void setTimeLIneRecords(List<TimeLineRecordsTaskList> timeline_records) {
        if (timeline_records.size() > 0) {
            for (int i = 0; timeline_records.size() > i; i++) {
                TimeLineRecordsTaskList timeLineRecordsTaskList = timeline_records.get( i );
                TimeLineRecordsTaskList timeLineRecordsTaskList1 = new TimeLineRecordsTaskList();
                timeLineRecordsTaskList1.setAction( timeLineRecordsTaskList.getAction() );
                timeLineRecordsTaskList1.setAction_code( timeLineRecordsTaskList.getAction_code() );
                timeLineRecordsTaskList1.setCreated_by( timeLineRecordsTaskList.getCreated_by());
                timeLineRecordsTaskList1.setCreated_at( timeLineRecordsTaskList.getCreated_at());
                timeLineRecordsTaskListArrayList.add( timeLineRecordsTaskList1 );
            }
            mRecylerViewTimeline.setAdapter( mTimeLineAdapter);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getInfo() {
      /*  String date = tv_date.getText().toString();
        String projectCode = tv_projectCode.getText().toString();
        String action = tv_timeLineActionType.getText().toString();*/
        if (tv_date != null){
            date = tv_date.getText().toString();
        }
        if (tv_projectCode != null){
            projectCode = tv_projectCode.getText().toString();
        }
        if (tv_timeLineActionType != null){
            action = tv_timeLineActionType.getText().toString();
        }
        if (individuvalArray !=null ){
            individuvalArray.remove( 0 );
        }
        Call<TimeLineTaskList> timeLineTaskListCall = ANApplications.getANApi().checktheTimeLineTaskList(
                id, action, date, projectCode,
                String.valueOf( individuvalArray).replace( "[", "" ).replace( "]", "" ));
        System.out.println( "timeLineValues" + id + action +   date  +   projectCode
                +    String.valueOf( individuvalArray).replace( "[", "" ).replace( "]", "" ) );
        timeLineTaskListCall.enqueue( new Callback<TimeLineTaskList>() {
            @Override
            public void onResponse(Call<TimeLineTaskList> call, Response<TimeLineTaskList> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    System.out.println( "timeLineReponse1" + response.raw());
                    if (response.body().getSuccess().equals( "true" )) {
                        System.out.println( "timeLineReponse2" + response.body().getTimeline_records());
                        timeLineRecordsTaskListArrayList.clear();
                        setTimeLIneRecords( response.body().getTimeline_records());
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<TimeLineTaskList> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );

            }
        } );

    }
    private void requestDynamicContent() {
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
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
            mIndividuvalDialogtime = new MultiSelectDialog()
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
                                tv_individual.setText( dataString );
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
    private void requestDynamicProjectList() {
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse( id );
        call.enqueue( new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
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
                projectListResponseRecords1.setName( projectListResponseRecords.getName());
                projectListResponseRecords1.setProject_code( (projectListResponseRecords.getProject_code()));
                projectListResponseRecordsArrayList.add( projectListResponseRecords1 );
            }
            mRecyclerViewDateTime.setAdapter(new NewTaskProjectAdapter(projectListResponseRecordsArrayList, R.layout.custom_project_footer, getApplicationContext()));
            mRecyclerViewDateTime.addOnItemTouchListener(new TimeLineActivity.RecyclerTouchListener(this, mRecyclerViewDateTime, new TimeLineActivity.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    mProjectNameDailog =(TextView)view.findViewById( R.id.tv_projectNameDailog);
                    projectName =mProjectNameDailog.getText().toString();
                    mProjectCodeDailog =(TextView)view.findViewById(R.id.tv_projectCodeDailog);

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

        private TimeLineActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(TimeLineActivity context, final RecyclerView mRecylerViewSingleSub, TimeLineActivity.ClickListener clickListener) {
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

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    private void appFooter() {
        View btnMe = findViewById( R.id.btn_me );
        btnMe.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityToady();
            }
        } );
        View btnProject = findViewById( R.id.btn_projects );
        btnProject.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityProject();
            }
        } );
        View btnTask = findViewById( R.id.btn_task );
        btnTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityTasks();
            }
        } );
        View btnIndividuals = findViewById( R.id.btn_individuals );
        btnIndividuals.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityIndividuals();
            }
        } );
        View btnInsights = findViewById( R.id.btn_insights );
        btnInsights.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityInsights();
            }
        } );
    }

    private void activityToady() {
        Intent i = new Intent( getApplicationContext(), TodayTaskActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    private void activityProject() {
        Intent i = new Intent( getApplicationContext(), ProjectFooterActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    private void activityTasks() {
        Intent i = new Intent( getApplicationContext(), TaskAddListActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    private void activityIndividuals() {
        Intent i = new Intent( getApplicationContext(), ViewIndividualsActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    private void activityInsights() {
        Intent i = new Intent( getApplicationContext(), DailyTaskChartActivity.class );
        startActivity( i );
        overridePendingTransition( R.anim.from_right_in, R.anim.from_left_out );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
