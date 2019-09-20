package com.actnow.android.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.CommentsActivity;
import com.actnow.android.activities.tasks.EditTaskActivity;
import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.databse.TaskDatabaseHelper;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.Task;
import com.actnow.android.sdk.responses.TaskComplete;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Date;

import static com.actnow.android.R.layout.task_list_cutsom;


public class OverdueFragment extends Fragment {
    RecyclerView mTaskRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fabTask;
    TaskListAdapter mTaskListAdapter;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    private String selectedType = "";
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();

    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;

    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();
    String id;
    MultiSelectDialog mIndividuvalDialogtime, mProjectDialogtime;
    final OverdueFragment context = this;

    TextView mTaskName;

    public OverdueFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils( getContext() );
        View view = inflater.inflate( R.layout.fragment_overdue, container, false );

        mIndividuvalDialogtime = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add( 0 );
        requestDynamicContent();

        if (AndroidUtils.isNetworkAvailable( getActivity() )) {
            attemptTaskList();
        } else {
            intentNoConnection();
        }

        mProgressView = view.findViewById( R.id.progress_bar );
        mContentLayout = view.findViewById( R.id.content_layout );
        fabTask = view.findViewById( R.id.fab_task );
        fabTask.setOnClickListener( new View.OnClickListener() {
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
        mTaskRecylcerView = (RecyclerView) view.findViewById( R.id.task_recyclerView );
        mLayoutManager = new LinearLayoutManager( getContext() );
        mTaskRecylcerView.setLayoutManager( mLayoutManager );
        mTaskRecylcerView.setItemAnimator( new DefaultItemAnimator() );
        mTaskListAdapter = new TaskListAdapter( taskListRecordsArrayList, task_list_cutsom, getContext() );
        mTaskRecylcerView.setAdapter( mTaskListAdapter );

        return view;
    }

    private void intentNoConnection() {
        TaskDatabaseHelper taskDatabaseHelper = new TaskDatabaseHelper( getActivity() );
        Task task = new Task();
        task.setId( id );

        Task task1 = taskDatabaseHelper.getSpecificTask( task );
        JSONObject data = new JSONObject();
        try {
            data.put( "id", task1.getId() );
            data.put( "name", task1.getName() );
            data.put( "task_code", task1.getTask_code() );
            data.put( "project_code", task1.getProject_code() );
            data.put( "priority", task1.getPriority() );
            data.put( "due_date", task1.getDue_date() );
            data.put( "task_members", task1.getTask_members() );
            data.put( "status", task1.getStatus() );
            data.put( "approval_status", task1.getApproval_status() );
            data.put( "created_by", task1.getCreated_by() );
            data.put( "created_date", task1.getCreated_date() );
            data.put( "updated_by", task1.getUpdated_by() );
            data.put( "updated_date", task1.getUpdated_date() );
            data.put( "orgn_code", task1.getOrgn_code() );
            data.put( "repeat_type", task1.getRepeat_type() );
            data.put( "repeat_months", task1.getRepeat_months() );
            data.put( "repeat_week", task1.getRepeat_week() );
            data.put( "repeat_days", task1.getRepeat_days() );
            data.put( "parenrt_task_code", task1.getParenrt_task_code() );
        } catch (JSONException e) {

        }
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
                    System.out.println( "url" + response.raw() );
                    if (response.body().getSuccess().equals( "true" )) {
                        setProjectFooterList( response.body().getTask_records() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    //   AndroidUtils.displayToast(getActivity(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );

            }
        } );
    }

    private void setProjectFooterList(List<TaskListRecords> taskListRecordsList) {
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
                if (taskListRecords.getStatus().equals("1")) {
                    Date date1 = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(date1);
                    String date2[] = taskListRecords.getDue_date().split( " " );
                    String date3 = date2[0];
                    System.out.println( "yestarday"+ date1 );
                    try {
                       Date date4 = new SimpleDateFormat("yyyy-MM-dd" ).parse( date3);
                       System.out.println( "date3"+ date4 );
                        if(date4.before(date1)){
                            taskListRecordsArrayList.add(taskListRecords1);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            mTaskRecylcerView.setAdapter( new TaskListAdapter( taskListRecordsArrayList, task_list_cutsom, getContext() ) );
            mTaskRecylcerView.addOnItemTouchListener( new RecyclerTouchListener( this, mTaskRecylcerView, new ClickListener() {
                @Override
                public void onClick(final View view, int position) {
                    final View view1 = view.findViewById( R.id.taskList_liner );
                    RadioGroup groupTask = (RadioGroup) view.findViewById( R.id.taskradioGroupTask );
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById( R.id.radio_buttonAction );
                    final TextView tv_dueDate = (TextView) view.findViewById( R.id.tv_taskListDate );
                    final TextView tv_taskcode = (TextView) view.findViewById( R.id.tv_taskCode );
                    final TextView tv_priority = (TextView) view.findViewById( R.id.tv_taskListPriority );
                    final TextView tv_status = (TextView) view.findViewById( R.id.tv_taskstatus );
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
                                            OverdueFragment fragment2 = new OverdueFragment();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.fragment_overDue, fragment2);
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
                                                            OverdueFragment fragment2 = new OverdueFragment();
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                            fragmentTransaction.replace(R.id.fragment_overDue, fragment2);
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
                            mIndividuvalDialogtime.show( getFragmentManager(), "mIndividuvalDialog" );

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
                            System.out.println( "maximum" + task_code );
                            startActivity( i );
                        }
                    } );
                    ImageView mImageRaminder = (ImageView) view.findViewById( R.id.img_raminderTaskList );
                    mImageRaminder.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog( getActivity(), android.R.style.Theme_DeviceDefault_Dialog_Alert );
                            dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                            dialog.setCancelable( true );
                            dialog.setContentView( R.layout.remainder_list_add );
                            final Calendar remianderCalender = Calendar.getInstance();
                            final EditText ed_dateRaminder = (EditText) dialog.findViewById( R.id.ed_dateReminder );
                            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    remianderCalender.set( Calendar.YEAR, year );
                                    remianderCalender.set( Calendar.MONTH, monthOfYear );
                                    remianderCalender.set( Calendar.DAY_OF_MONTH, dayOfMonth );
                                    String myFormat = "yyyy-MM-dd"; //In which you need put here
                                    SimpleDateFormat sdf = new SimpleDateFormat( myFormat, Locale.UK );
                                    ed_dateRaminder.setText( sdf.format( remianderCalender.getTime() ) );
                                }
                            };
                            ed_dateRaminder.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new DatePickerDialog( getActivity(), date, remianderCalender
                                            .get( Calendar.YEAR ), remianderCalender.get( Calendar.MONTH ),
                                            remianderCalender.get( Calendar.DAY_OF_MONTH ) ).show();
                                }
                            } );
                            final EditText ed_timeRemiander = (EditText) dialog.findViewById( R.id.ed_timeReminder );
                            ed_timeRemiander.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Calendar mcurrentTime = Calendar.getInstance();
                                    int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
                                    int minute = mcurrentTime.get( Calendar.MINUTE );
                                    TimePickerDialog mTimePicker;
                                    mTimePicker = new TimePickerDialog( getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                            ed_timeRemiander.setText( selectedHour + ":" + selectedMinute );
                                        }
                                    }, hour, minute, true );
                                    mTimePicker.setTitle( "Select Time" );
                                    mTimePicker.show();
                                }
                            } );
                            final TextView mAddTextView = (TextView) dialog.findViewById( R.id.tv_remainderAdd );
                            mAddTextView.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText( getActivity(), "Work in Progress!", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                            TextView mCancelTextView = (TextView) dialog.findViewById( R.id.tv_remainderCancel );
                            mCancelTextView.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText( getActivity(), "Work in Progress!", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                            ImageView mImgDropRemainder = (ImageView) dialog.findViewById( R.id.imge_reminderDropDown );
                            mImgDropRemainder.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mIndividuvalDialogtime.show( getFragmentManager(), "mIndividuvalDialog" );

                                }
                            } );

                            dialog.show();
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

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(OverdueFragment context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
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
                    AndroidUtils.displayToast( getActivity(), "Something Went Wrong!!" );
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
                                // mIndividualCheckBox.setText(dataString);
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
}