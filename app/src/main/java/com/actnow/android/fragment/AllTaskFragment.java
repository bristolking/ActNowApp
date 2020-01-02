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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.AdvancedSearchActivity;
import com.actnow.android.activities.CommentsActivity;
import com.actnow.android.activities.ReaminderScreenActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.invitation.InvitationActivity;
import com.actnow.android.activities.tasks.EditTaskActivity;
import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.sdk.responses.TaskComplete;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllTaskFragment extends Fragment {
    RecyclerView mAllTaskRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fabAllTask;
    TaskListAdapter mTaskListAdapter;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    EditText mTaskQucikSearch;
    Button mButtonAdavancedSearch;
    ImageView mImageBulbTask;
    private String selectedType = "";
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();

    final AllTaskFragment context = this;

    TextView mTaskName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils( getContext() );
        View view = inflater.inflate( R.layout.fragment_all_task, container, false );
        mProgressView = view.findViewById( R.id.progress_bar );
        mContentLayout = view.findViewById( R.id.content_layout );
        fabAllTask = view.findViewById( R.id.fab_alltask );
        fabAllTask.setOnClickListener( new View.OnClickListener() {
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
        mAllTaskRecylcerView = (RecyclerView) view.findViewById( R.id.alltask_recyclerView );
        mLayoutManager = new LinearLayoutManager( getContext() );
        mAllTaskRecylcerView.setLayoutManager( mLayoutManager );
        mAllTaskRecylcerView.setItemAnimator( new DefaultItemAnimator() );
        mTaskListAdapter = new TaskListAdapter( taskListRecordsArrayList);
        mAllTaskRecylcerView.setAdapter( mTaskListAdapter );
        attemptTaskList();

        mImageBulbTask = view.findViewById( R.id.image_bulbTask );
        mImageBulbTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getActivity(), ViewIdeasActivity.class );
                startActivity( i );
            }
        } );
        mTaskQucikSearch = view.findViewById( R.id.edit_searchTask );
        mTaskQucikSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        } );



        mButtonAdavancedSearch = view.findViewById( R.id.button_searchTask );
        mButtonAdavancedSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getActivity(), AdvancedSearchActivity.class );
                startActivity( i );
            }
        } );
        return view;
    }

    private void filter(String toString) {
        ArrayList<TaskListRecords>  taskListRecordsFilter = new ArrayList<TaskListRecords>( );
        for (TaskListRecords name  :taskListRecordsArrayList){
            if (name.getName().toLowerCase().contains( toString.toLowerCase() )){
                taskListRecordsFilter.add(name);
            }
        }
        mTaskListAdapter.filterList(taskListRecordsFilter);
    }


    private void attemptTaskList() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
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
                taskListRecords1.setStatus( taskListRecords.getStatus() );
                taskListRecords1.setProject_name( taskListRecords.getProject_name() );
                taskListRecords1.setRepeat_type( taskListRecords.getRepeat_type() );
                if (taskListRecords.getStatus().equals( "1" )) {
                    taskListRecordsArrayList.add( taskListRecords1 );
                }
            }
            mAllTaskRecylcerView.setAdapter(mTaskListAdapter);
            //mAllTaskRecylcerView.setAdapter( new TaskListAdapter( taskListRecordsArrayList, task_list_cutsom, getContext() ) );
            mAllTaskRecylcerView.addOnItemTouchListener( new AllTaskFragment.RecyclerTouchListener( this, mAllTaskRecylcerView, new AllTaskFragment.ClickListener() {
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
                            if (checkedId == R.id.radio_buttonAction) {
                                if (checkedId == R.id.radio_buttonAction) {
                                    selectedType = radioButtonTaskName.getText().toString();
                                    Snackbar snackbar = Snackbar.make( mContentLayout, "Completed.", Snackbar.LENGTH_LONG ).setAction( "UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            view1.setVisibility( View.VISIBLE );
                                            AllTaskFragment allTaskFragment = new AllTaskFragment();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace( R.id.fragment_allTask, allTaskFragment );
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
                                                            AllTaskFragment allTaskFragment = new AllTaskFragment();
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                            fragmentTransaction.replace( R.id.fragment_allTask, allTaskFragment );
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
                            String task_code = tv_taskcode.getText().toString();
                            String projectCode = tv_projectCode.getText().toString();
                            Intent i = new Intent( getActivity(), InvitationActivity.class );
                            i.putExtra( "TaskCode", task_code );
                            i.putExtra( "SenIvitaionprojectCode",projectCode);
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
                            System.out.println( "maximum" + task_code );
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

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private AllTaskFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(AllTaskFragment context, final RecyclerView mRecylerViewSingleSub, AllTaskFragment.ClickListener clickListener) {
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



