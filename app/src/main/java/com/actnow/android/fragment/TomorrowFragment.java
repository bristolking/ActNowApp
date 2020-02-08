package com.actnow.android.fragment;

import android.annotation.SuppressLint;
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


public class TomorrowFragment extends Fragment {
    RecyclerView mTomorrowRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fabAllTask;
    TaskListAdapter mTaskListAdapter;
    TaskOfflineAdapter mTaskOfflineAdapter;
    UserPrefUtils session;
    View mProgressView, mContentLayout;

    private String selectedType = "";
    private ArrayList<TaskListRecords> taskListRecordsArrayList;

    final TomorrowFragment context = this;

    TextView mTaskName;
    String id;

    TextView mWeekNameTomorrow;

    public TomorrowFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils( getContext());
        View view = inflater.inflate( R.layout.fragment_tomorrow, container, false );

        mProgressView = view.findViewById( R.id.progress_bar );
        mContentLayout = view.findViewById( R.id.content_layout );
        taskListRecordsArrayList = new ArrayList<TaskListRecords>();
        mWeekNameTomorrow =(TextView)view.findViewById(R.id.weekNametomorrow);

        mTomorrowRecylcerView = (RecyclerView) view.findViewById( R.id.tomorrow_recyclerView );
        mLayoutManager = new LinearLayoutManager( getContext() );
        mTomorrowRecylcerView.setLayoutManager( mLayoutManager );
        mTomorrowRecylcerView.setItemAnimator( new DefaultItemAnimator() );
        mTaskListAdapter = new TaskListAdapter( taskListRecordsArrayList );
        mTomorrowRecylcerView.setAdapter( mTaskListAdapter );

        mTaskOfflineAdapter = new TaskOfflineAdapter( taskListRecordsArrayList );
        mTomorrowRecylcerView.setAdapter( mTaskOfflineAdapter );

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        Date tomorrow = calendar.getTime();
        SimpleDateFormat dfWeek = new SimpleDateFormat( "E MMM dd" );
        String dateWeekMonth =dfWeek.format( tomorrow);
        mWeekNameTomorrow.setText( " " + dateWeekMonth );


        if (AndroidUtils.isNetworkAvailable( getApplicationContext() )) {
            attemptTaskList();
        } else {
            tomorrowFrgmentNoConnection();
        }

        return view;
    }

    private void attemptTaskList() {
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse( id );
        call.enqueue( new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setTaskList( response.body().getTask_records() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
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
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
                Date tomorrow = calendar.getTime();
                SimpleDateFormat dfWeek = new SimpleDateFormat( "E MMM dd" );
                String dateWeekMonth =dfWeek.format( tomorrow);
                mWeekNameTomorrow.setText( " " + dateWeekMonth );

                String tomorrowDate = df.format(tomorrow);
                System.out.println("tomorrow: " + tomorrowDate);

                String date2[] = taskListRecords.getDue_date().split( " " );
                String date3 = date2[0];

                if (taskListRecords.getStatus().equals( "1" )&& date3.equals(tomorrowDate)) {
                    taskListRecordsArrayList.add( taskListRecords );
                }
            }
            mTomorrowRecylcerView.setAdapter( mTaskListAdapter );
            mTomorrowRecylcerView.addOnItemTouchListener( new TomorrowFragment.RecyclerTouchListener( this, mTomorrowRecylcerView, new TomorrowFragment.ClickListener() {
                @Override
                public void onClick(final View view, int position) {
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
                            Toast.makeText(getApplicationContext(),"WORK IN PROGRESS!",Toast.LENGTH_LONG ).show();

                           /* if (checkedId == R.id.radio_buttonAction) {
                                if (checkedId == R.id.radio_buttonAction) {
                                    selectedType = radioButtonTaskName.getText().toString();
                                    Snackbar snackbar = Snackbar.make( mContentLayout, "Completed.", Snackbar.LENGTH_LONG ).setAction( "UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            view1.setVisibility( View.VISIBLE );
                                            TomorrowFragment tomorrowFragment = new TomorrowFragment();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace( R.id.fragment_tomorrow, tomorrowFragment );
                                            fragmentTransaction.commit();
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
                                                            TomorrowFragment tomorrowFragment = new TomorrowFragment();
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                            fragmentTransaction.replace( R.id.fragment_tomorrow, tomorrowFragment );
                                                            fragmentTransaction.commit();
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
                            }*/
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
                           /* HashMap<String, String> userId = session.getUserDetails();
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
                                            TomorrowFragment tomorrowFragment = new TomorrowFragment();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace( R.id.fragment_tomorrow, tomorrowFragment );
                                            fragmentTransaction.commit();
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
                            } );*/
                            Toast.makeText(getApplicationContext(),"WORK IN PROGRESS!",Toast.LENGTH_LONG ).show();


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

        private TomorrowFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(TomorrowFragment context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
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


    private void tomorrowFrgmentNoConnection() {
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
                if (status.equals( "1" )) {
                    taskListRecordsArrayList.add( taskListRecords );
                }
            }
        }
        mTomorrowRecylcerView.setAdapter( mTaskOfflineAdapter );
        mTomorrowRecylcerView.addOnItemTouchListener( new TomorrowFragment.RecyclerTouchListener( this, mTomorrowRecylcerView, new TomorrowFragment.ClickListener() {
            @Override
            public void onClick(final View view, int position) {
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
                       /* if (checkedId == R.id.radio_buttonAction) {
                            if (checkedId == R.id.radio_buttonAction) {
                                selectedType = radioButtonTaskName.getText().toString();
                                Snackbar snackbar = Snackbar.make( mContentLayout, "Completed.", Snackbar.LENGTH_LONG ).setAction( "UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        view1.setVisibility( View.VISIBLE );
                                        TomorrowFragment tomorrowFragment = new TomorrowFragment();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace( R.id.fragment_tomorrow, tomorrowFragment );
                                        fragmentTransaction.commit();
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
                                                        TomorrowFragment tomorrowFragment = new TomorrowFragment();
                                                        FragmentManager fragmentManager = getFragmentManager();
                                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        fragmentTransaction.replace( R.id.fragment_tomorrow, tomorrowFragment );
                                                        fragmentTransaction.commit();
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
                        }*/
                        Toast.makeText(getApplicationContext(),"WORK IN PROGRESS!",Toast.LENGTH_LONG ).show();

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
                        Toast.makeText( getApplicationContext(), "WORK IN PROGRESS!", Toast.LENGTH_LONG ).show();
                    }
                } );

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        } ) );
    }
}

