package com.actnow.android.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.CommentsActivity;
import com.actnow.android.activities.ReaminderScreenActivity;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.tasks.EditTaskActivity;
import com.actnow.android.adapter.OverDueTaskAdapter;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.adapter.TaskOfflineAdapter;
import com.actnow.android.databse.TaskDBHelper;
import com.actnow.android.sdk.responses.TaskComplete;
import com.actnow.android.sdk.responses.TaskDelete;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


public class WeekNameOneFragment extends Fragment {
    RecyclerView mWeekOneRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fabAllTask;
    TaskListAdapter mTaskListAdapter;
    TaskOfflineAdapter mTaskOfflineAdapter;
    UserPrefUtils session;
    View mProgressView, mContentLayout;

    private String selectedType = "";
    private ArrayList<TaskListRecords> taskListRecordsArrayList;

    final WeekNameOneFragment context = this;

    TextView mTaskName;
    String id;

    TextView mWeekNameOne;
    private ProgressDialog mProgressDialog;
    public WeekNameOneFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils( getContext());

        View view=  inflater.inflate( R.layout.fragment_week_name_one, container, false );
        mProgressView = view.findViewById( R.id.progress_bar );
        mContentLayout = view.findViewById( R.id.content_layout );
        taskListRecordsArrayList = new ArrayList<TaskListRecords>();
        mWeekNameOne =(TextView)view.findViewById(R.id.dayweekNameOne);

        mWeekOneRecylcerView = (RecyclerView) view.findViewById( R.id.weekNameOne_recyclerView );
        mLayoutManager = new LinearLayoutManager( getContext() );
        mWeekOneRecylcerView.setLayoutManager( mLayoutManager );
        mWeekOneRecylcerView.setItemAnimator( new DefaultItemAnimator() );
        mTaskListAdapter = new TaskListAdapter( taskListRecordsArrayList );
        mWeekOneRecylcerView.setAdapter( mTaskListAdapter );

        mTaskOfflineAdapter = new TaskOfflineAdapter( taskListRecordsArrayList );
        mWeekOneRecylcerView.setAdapter( mTaskOfflineAdapter );

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date tomorrow = calendar.getTime();
        SimpleDateFormat dfWeek = new SimpleDateFormat( "EEEE MMM dd" );
        String dateWeekMonth =dfWeek.format( tomorrow);
        mWeekNameOne.setText( " " + dateWeekMonth );
        if (AndroidUtils.isNetworkAvailable( getApplicationContext() )) {
            attemptTaskList();
        } else {
            weeKOneFrgmentNoConnection();
        }

        return view;
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
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

    private void attemptTaskList() {
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse( id );
        call.enqueue( new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                //AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setTaskList( response.body().getTask_records() );
                    } else {
                        Toast.makeText( getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT ).show();                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );

            }
        } );
    }
    private void setTaskList(List<TaskListRecords> taskListRecordsList) {
        TaskDBHelper dbHelper = new TaskDBHelper( getContext() );
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords1 = taskListRecordsList.get( i );
                TaskListRecords taskListRecords = new TaskListRecords();
                taskListRecords.setTask_id( taskListRecords1.getTask_id() );
                taskListRecords.setName( taskListRecords1.getName() );
                taskListRecords.setDue_date( taskListRecords1.getDue_date() );
                taskListRecords.setPriority( taskListRecords1.getPriority() );
                taskListRecords.setProject_code( taskListRecords1.getProject_code() );
                taskListRecords.setTask_code( taskListRecords1.getTask_code() );
                taskListRecords.setRemindars_count( taskListRecords1.getRemindars_count() );
                taskListRecords.setStatus( taskListRecords1.getStatus() );
                taskListRecords.setProject_name( taskListRecords1.getProject_name() );
                taskListRecords.setRepeat_type( taskListRecords1.getRepeat_type() );
                dbHelper.insertTaskDetails( taskListRecords );

                if (taskListRecords.getDue_date()!=null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, 2);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date tomorrow = calendar.getTime();
                    String tomorrowDate = df.format(tomorrow);
          /*      SimpleDateFormat dfWeek = new SimpleDateFormat( "EEEE MMM dd" );
                String dateWeekMonth =dfWeek.format( tomorrow);
                mWeekNameOne.setText( " " + dateWeekMonth );*/

                    System.out.println("tomorrow: " + tomorrowDate);

                    String date2[] = taskListRecords.getDue_date().split(" ");
                    String date3 = date2[0];

                    if (taskListRecords.getStatus().equals("1") && date3.equals(tomorrowDate)) {
                        taskListRecordsArrayList.add(taskListRecords);
                    }
                }
            }
            mWeekOneRecylcerView.setAdapter( mTaskListAdapter );
            mWeekOneRecylcerView.addOnItemTouchListener( new WeekNameOneFragment.RecyclerTouchListener( this, mWeekOneRecylcerView, new WeekNameOneFragment.ClickListener() {
                @Override
                public void onClick(final View view, final int position) {
                    final View view1 = view.findViewById( R.id.taskList_liner );
                    RadioGroup groupTask = (RadioGroup) view.findViewById( R.id.taskradioGroupTask );
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById( R.id.radio_buttonAction );
                    final TextView tv_dueDate = (TextView) view.findViewById( R.id.tv_taskListDate );
                    final TextView tv_taskcode = (TextView) view.findViewById( R.id.tv_taskCode );
                    final TextView tv_priority = (TextView) view.findViewById( R.id.tv_taskListPriority );
                    final TextView tv_status = (TextView) view.findViewById( R.id.tv_taskstatus );
                    final TextView tv_projectName = (TextView) view.findViewById( R.id.tv_projectNameTaskList );
                    final TextView tv_projectCode = (TextView) view.findViewById( R.id.tv_projectCodeTaskList );
                    groupTask.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.radio_buttonAction) {
                                if (checkedId == R.id.radio_buttonAction) {
                                    selectedType = radioButtonTaskName.getText().toString();
                                    Snackbar snackbar = Snackbar.make( mContentLayout, "Completed.", Snackbar.LENGTH_LONG ).setAction( "UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            view1.setVisibility( View.VISIBLE );
                                            Snackbar snackbar1 = Snackbar.make( mContentLayout, "Task is restored!", Snackbar.LENGTH_SHORT );
                                            snackbar1.show();
                                        }
                                    } );
                                    View sbView = snackbar.getView();
                                    TextView textView = (TextView) sbView.findViewById( R.id.snackbar_text );
                                    textView.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mTaskListAdapter.removeItem(position);
                                            view1.setVisibility( View.GONE );
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
                                                        } else {
                                                            Toast.makeText( getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT ).show();                                                        }
                                                    } else {
                                                        AndroidUtils.displayToast( getActivity(), "Something Went Wrong!!" );
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<TaskComplete> call, Throwable t) {
                                                    Log.d( "CallBack", " Throwable is " + t );
                                                }
                                            } );
                                            Snackbar snackbar2 = Snackbar.make( mContentLayout, "Task is completed!", Snackbar.LENGTH_SHORT );
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
                            Intent i = new Intent( getActivity(), EditTaskActivity.class );
                            i.putExtra( "TaskName", name );
                            i.putExtra( "TaskDate", date );
                            i.putExtra( "TaskCode", task_code );
                            i.putExtra( "taskOwnerName", taskOwnerName );
                            startActivity( i );
                            System.out.println( "user" + task_code );
                        }
                    } );
                    ImageView mImageUserAdd = (ImageView) view.findViewById( R.id.img_useraddTaskList );
                    mImageUserAdd.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            String projectCode = tv_projectCode.getText().toString();
                            Intent i = new Intent( getActivity(), InvitationActivity.class );
                            i.putExtra( "TaskCode", task_code );
                            i.putExtra( "SenIvitaionprojectCode", projectCode );
                            startActivity( i );
                        }
                    } );
                    ImageView mImageComment = (ImageView) view.findViewById( R.id.img_commentTaskList );
                    mImageComment.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent( getActivity(), CommentsActivity.class );
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
                            Intent i = new Intent( getActivity(), ReaminderScreenActivity.class );
                            i.putExtra( "TaskCode", task_code );
                            startActivity( i );
                        }
                    } );
                    ImageView mImageDelete = (ImageView) view.findViewById( R.id.img_delete );
                    mImageDelete.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showProgressDialog();
                            HashMap<String, String> userId = session.getUserDetails();
                            String id = userId.get( UserPrefUtils.ID );
                            String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                            String task_code = tv_taskcode.getText().toString();
                            Call<TaskDelete> taskDeleteCall = ANApplications.getANApi().checkTheDelete( id, task_code, orgn_code );
                            System.out.println( "deleteFields" + id + task_code + orgn_code );
                            taskDeleteCall.enqueue( new Callback<TaskDelete>() {
                                @Override
                                public void onResponse(Call<TaskDelete> call, Response<TaskDelete> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getSuccess().equals( "true" )) {
                                            System.out.println( "deleteResponse2" + response.raw() );
                                            hideProgressDialog();
                                            mTaskListAdapter.removeItem(position);
                                            Snackbar.make( mContentLayout, "Task Deleted Sucessfully", Snackbar.LENGTH_SHORT ).show();
                                        } else {
                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                        }
                                    } else {
                                        AndroidUtils.displayToast( getActivity(), "Something Went Wrong!!" );
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

            } ) );
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private WeekNameOneFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(WeekNameOneFragment context, final RecyclerView mRecylerViewSingleSub, WeekNameOneFragment.ClickListener clickListener) {
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

    private void  weeKOneFrgmentNoConnection() {
        //AndroidUtils.showProgress( false, mProgressView, mContentLayout );
        TaskDBHelper taskDBHelper = new TaskDBHelper( getContext() );
        Cursor cursor = taskDBHelper.getAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                TaskListRecords taskListRecords = new TaskListRecords();
                String name = cursor.getString( cursor.getColumnIndex( taskDBHelper.KEY_NAME ) );
                String date = cursor.getString( cursor.getColumnIndex( taskDBHelper.KEY_DUEDATE ) );
                String priority = cursor.getString( cursor.getColumnIndex( taskDBHelper.KEY_PRIORITY ) );
                String projectcode = cursor.getString( cursor.getColumnIndex( taskDBHelper.KEY_PROJECT_CODE ) );
                String taskcode = cursor.getString( cursor.getColumnIndex( taskDBHelper.KEY_TASK_CODE ) );
                String remindarscount = cursor.getString( cursor.getColumnIndex( taskDBHelper.KEY_REMINDARS_COUNT ) );
                String status = cursor.getString( cursor.getColumnIndex( taskDBHelper.KEY_STATUS ) );
                String projectName = cursor.getString( cursor.getColumnIndex( taskDBHelper.KEY_PROJECT_NAME ) );
                String type = cursor.getString( cursor.getColumnIndex( taskDBHelper.KEY_REPEAT_TYPE ) );
                taskListRecords.setName( name );
                taskListRecords.setDue_date( date );
                taskListRecords.setPriority( priority );
                taskListRecords.setProject_code( projectcode );
                taskListRecords.setTask_code( taskcode );
                taskListRecords.setRemindars_count( remindarscount );
                taskListRecords.setStatus( status );
                taskListRecords.setProject_name( projectName );
                taskListRecords.setRepeat_type( type );
                if (taskListRecords.getDue_date()!=null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, 2);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date tomorrow = calendar.getTime();
                    String tomorrowDate = df.format(tomorrow);
                    String date2[] = taskListRecords.getDue_date().split(" ");
                    String date3 = date2[0];
                    if (taskListRecords.getStatus().equals("1") && date3.equals(tomorrowDate)) {
                        taskListRecordsArrayList.add(taskListRecords);
                    }
                }

            }
        }
        mWeekOneRecylcerView.setAdapter( mTaskOfflineAdapter );
        mWeekOneRecylcerView.addOnItemTouchListener( new WeekNameOneFragment.RecyclerTouchListener( this, mWeekOneRecylcerView, new WeekNameOneFragment.ClickListener() {
            @Override
            public void onClick(final View view, final int position) {
                final View view1 = view.findViewById( R.id.taskList_liner );
                RadioGroup groupTask = (RadioGroup) view.findViewById( R.id.taskradioGroupTask );
                final RadioButton radioButtonTaskName = (RadioButton) view.findViewById( R.id.radio_buttonAction );
                final TextView tv_dueDate = (TextView) view.findViewById( R.id.tv_taskListDate );
                final TextView tv_taskcode = (TextView) view.findViewById( R.id.tv_taskCode );
                final TextView tv_priority = (TextView) view.findViewById( R.id.tv_taskListPriority );
                final TextView tv_status = (TextView) view.findViewById( R.id.tv_taskstatus );
                final TextView tv_projectName = (TextView) view.findViewById( R.id.tv_projectNameTaskList );
                final TextView tv_projectCode = (TextView) view.findViewById( R.id.tv_projectCodeTaskList );
                groupTask.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.radio_buttonAction) {
                            if (checkedId == R.id.radio_buttonAction) {
                                selectedType = radioButtonTaskName.getText().toString();
                                Snackbar snackbar = Snackbar.make( mContentLayout, "Completed.", Snackbar.LENGTH_LONG ).setAction( "UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        view1.setVisibility( View.VISIBLE );
                                        Snackbar snackbar1 = Snackbar.make( mContentLayout, "Task is restored!", Snackbar.LENGTH_SHORT );
                                        snackbar1.show();
                                    }
                                } );
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById( R.id.snackbar_text );
                                textView.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        view1.setVisibility( View.GONE );
                                        mTaskOfflineAdapter.removeItem(position);
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
                                                    } else {
                                                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                                    }
                                                } else {
                                                    AndroidUtils.displayToast( getActivity(), "Something Went Wrong!!" );
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<TaskComplete> call, Throwable t) {
                                                Log.d( "CallBack", " Throwable is " + t );
                                            }
                                        } );
                                        Snackbar snackbar2 = Snackbar.make( mContentLayout, "Task is completed!", Snackbar.LENGTH_SHORT );
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
                        Intent i = new Intent( getActivity(), EditTaskActivity.class );
                        i.putExtra( "TaskName", name );
                        i.putExtra( "TaskDate", date );
                        i.putExtra( "TaskCode", task_code );
                        i.putExtra( "taskOwnerName", taskOwnerName );
                        startActivity( i );
                        System.out.println( "user" + task_code );
                    }
                } );
                ImageView mImageUserAdd = (ImageView) view.findViewById( R.id.img_useraddTaskList );
                mImageUserAdd.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String task_code = tv_taskcode.getText().toString();
                        String projectCode = tv_projectCode.getText().toString();
                        Intent i = new Intent( getActivity(), InvitationActivity.class );
                        i.putExtra( "TaskCode", task_code );
                        i.putExtra( "SenIvitaionprojectCode", projectCode );
                        startActivity( i );
                    }
                } );
                ImageView mImageComment = (ImageView) view.findViewById( R.id.img_commentTaskList );
                mImageComment.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent( getActivity(), CommentsActivity.class );
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
                        Intent i = new Intent( getActivity(), ReaminderScreenActivity.class );
                        i.putExtra( "TaskCode", task_code );
                        startActivity( i );
                    }
                } );

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        } ) );
    }
}

