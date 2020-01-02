package com.actnow.android.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.CommentsActivity;
import com.actnow.android.activities.ReaminderScreenActivity;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.tasks.EditTaskActivity;
import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.sdk.responses.TaskComplete;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.actnow.android.R.layout.task_list_cutsom;


public class TodayFragment extends Fragment {
    RecyclerView mTodayRecylcerView;
    RecyclerView.LayoutManager mTodayLayoutManager;
    FloatingActionButton fabTodayTask;
    TaskListAdapter mTodayTaskListAdapter;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    private String selectedType = "";
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();


    final TodayFragment context = this;
    String id;
    TextView mTaskName;

    public TodayFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils( getContext() );
        View view =  inflater.inflate( R.layout.fragment_today, container, false );
        mProgressView = view.findViewById( R.id.progress_bar );
        mContentLayout = view.findViewById( R.id.content_layout );
        fabTodayTask = view.findViewById( R.id.fab_todaytask );
        fabTodayTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get( UserPrefUtils.ID );
                String taskOwnerName = userId.get( UserPrefUtils.NAME );
                Intent i = new Intent( getActivity(), ViewTasksActivity.class );
                i.putExtra( "id", id );
                i.putExtra( "taskOwnerName", taskOwnerName );
                startActivity( i );
            }
        } );
        mTodayRecylcerView = (RecyclerView) view.findViewById( R.id.toady_recylerView );
        mTodayLayoutManager = new LinearLayoutManager( getContext() );
        mTodayRecylcerView.setLayoutManager( mTodayLayoutManager );
        mTodayRecylcerView.setItemAnimator( new DefaultItemAnimator() );
        mTodayTaskListAdapter = new TaskListAdapter( taskListRecordsArrayList, task_list_cutsom, getContext() );
        mTodayRecylcerView.setAdapter( mTodayTaskListAdapter );
        attemptTaskList();
        return view;
    }

    private void attemptTaskList() {
        HashMap<String,String> userTodayId = session.getUserDetails();
        String id = userTodayId.get(UserPrefUtils.ID);
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse(id);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                System.out.println("ress"+ response.raw() );
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        setTaskList(response.body().getTask_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getActivity(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {

            }
        });
    }
    private void setTaskList(List<TaskListRecords> taskListRecordsList) {
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get( i );
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName( taskListRecords.getName() );
                taskListRecords1.setDue_date( taskListRecords.getDue_date() );
                taskListRecords1.setPriority( taskListRecords.getPriority() );
                taskListRecords1.setProject_code( taskListRecords.getProject_code() );
                taskListRecords1.setTask_code( taskListRecords.getTask_code() );
                taskListRecords1.setProject_name( taskListRecords.getProject_name() );
                taskListRecords1.setRepeat_type( taskListRecords.getRepeat_type() );
                Date date1 = new Date();
                SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
                String formattedDate = df.format( date1 );
                System.out.println( "formattedDate" + formattedDate );
                String date2[] = taskListRecords.getDue_date().split( " " );
                String date3 = date2[0];
                if (date3.equals( formattedDate ) && taskListRecords.getStatus().equals( "1" )) {
                    taskListRecordsArrayList.add( taskListRecords1 );
                }

            }
            mTodayRecylcerView.setAdapter(new TaskListAdapter(taskListRecordsArrayList, R.layout.task_list_cutsom, getActivity()));
            mTodayRecylcerView.addOnItemTouchListener( new TodayFragment.RecyclerTouchListener( this, mTodayRecylcerView, new TodayFragment.ClickListener() {
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
                                            TodayFragment mTodayFragment = new TodayFragment();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.fragment_today, mTodayFragment);
                                            fragmentTransaction.commit();
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
                                            Call<TaskComplete> callComplete = ANApplications.getANApi().checkTheTaskComplete( id, task_code, orgn_code );
                                            callComplete.enqueue( new Callback<TaskComplete>() {
                                                @Override
                                                public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.body().getSuccess().equals( "true" )) {
                                                            TodayFragment mTodayFragment = new TodayFragment();
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                            fragmentTransaction.replace(R.id.fragment_today, mTodayFragment);
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
                            Intent i= new Intent(getActivity(), InvitationActivity.class);
                            startActivity(i);

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
                            Intent i=new Intent( getActivity(), ReaminderScreenActivity.class);
                            i.putExtra( "TaskCode", task_code );
                            System.out.println( "maximum" + task_code );
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

        private TodayFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(TodayFragment context, final RecyclerView mRecylerViewSingleSub, TodayFragment.ClickListener clickListener) {
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


}
