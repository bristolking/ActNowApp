package com.actnow.android.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import java.text.DateFormat;
import java.text.ParseException;
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


public class OverDueTodayFragment extends Fragment {

    RecyclerView mtodayOverDueRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    TaskListAdapter mTaskListAdapter;
    TaskOfflineAdapter mTaskOfflineAdapter;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    private ProgressDialog mProgressDialog;
    private String selectedType = "";
    ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();

    final OverDueTodayFragment context = this;
    String id;
    TextView mTaskName;
    Dialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils(getContext());
        View view = inflater.inflate(R.layout.fragment_over_due_today, container, false);
        //showProgressDialog();
        taskListRecordsArrayList = new ArrayList<TaskListRecords>();

        mProgressView = view.findViewById(R.id.progress_bar);
        mContentLayout = view.findViewById(R.id.content_layout);

        mtodayOverDueRecylcerView = (RecyclerView) view.findViewById(R.id.task_overdue_todayrecyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mtodayOverDueRecylcerView.setLayoutManager(mLayoutManager);
        mtodayOverDueRecylcerView.setItemAnimator(new DefaultItemAnimator());
        mTaskListAdapter = new TaskListAdapter(taskListRecordsArrayList);
        mtodayOverDueRecylcerView.setAdapter(mTaskListAdapter);

        mTaskOfflineAdapter = new TaskOfflineAdapter(taskListRecordsArrayList);
        mtodayOverDueRecylcerView.setAdapter(mTaskOfflineAdapter);
        if (AndroidUtils.isNetworkAvailable(getActivity())) {
            attemptTaskList();
            showProgressDialog();
        } else {
            overDueTodayFragmentNoConnection();
        }
        TextView mReschedule =(TextView)view.findViewById(R.id.tv_reschedule);
        mReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get(UserPrefUtils.ID);
            }
        });
        return view;
    }

    private void filter(String toString) {
        ArrayList<TaskListRecords> taskListRecordsFilter = new ArrayList<TaskListRecords>();
        for (TaskListRecords name : taskListRecordsArrayList) {
            if (name.getName().toLowerCase().contains(toString.toLowerCase())) {
                taskListRecordsFilter.add(name);
            }
        }
        mTaskListAdapter.filterList(taskListRecordsFilter);
    }


    private void attemptTaskList() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse(id);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                hideProgressDialog();
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    System.out.println("url" + response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        setTaskList(response.body().getTask_records());
                    } else {
                        Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getActivity(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);

            }
        });
    }

    /*private void showProgressDialog() {
          dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_Alert );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.setCancelable( true );
        dialog.setContentView( R.layout.custom_dialog_progress );
        ProgressBar llProgressBar = (ProgressBar)dialog.findViewById(R.id.progressBar);
        dialog.show();

    }

    private void hideProgressDialog() {
        dialog.dismiss();
    }*/
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private void setTaskList(List<TaskListRecords> taskListRecordsList) {
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get(i);
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName(taskListRecords.getName());
                taskListRecords1.setDue_date(taskListRecords.getDue_date());
                taskListRecords1.setPriority(taskListRecords.getPriority());
                taskListRecords1.setProject_code(taskListRecords.getProject_code());
                taskListRecords1.setTask_code(taskListRecords.getTask_code());
                taskListRecords1.setRemindars_count(taskListRecords.getRemindars_count());
                taskListRecords1.setStatus(taskListRecords.getStatus());
                taskListRecords1.setProject_name(taskListRecords.getProject_name());
                taskListRecords1.setRepeat_type(taskListRecords.getRepeat_type());

                if (taskListRecords.getDue_date() != null) {
                    Date date1 = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(date1);
                    String date2[] = taskListRecords.getDue_date().split(" ");
                    String date3 = date2[0];
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String dateYes = dateFormat.format(yesterday());
                    Date dat6 = new Date(dateYes);
                    System.out.println("dateys" + dat6);
                    try {
                        Date date4 = new SimpleDateFormat("yyyy-MM-dd").parse(date3);
                        System.out.println("date3" + date4);
                        if (date4.before(dat6) && taskListRecords.getStatus().equals("1")&& taskListRecords.getDue_date() != null) {
                            taskListRecordsArrayList.add(taskListRecords1);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
            mtodayOverDueRecylcerView.setAdapter(mTaskListAdapter);
            mtodayOverDueRecylcerView.addOnItemTouchListener(new OverDueTodayFragment.RecyclerTouchListener(this, mtodayOverDueRecylcerView, new OverDueTodayFragment.ClickListener() {
                @Override
                public void onClick(final View view, final int position) {
                    final View view1 = view.findViewById(R.id.taskList_liner);
                    RadioGroup groupTask = (RadioGroup) view.findViewById(R.id.taskradioGroupTask);
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById(R.id.radio_buttonAction);
                    final TextView tv_dueDate = (TextView) view.findViewById(R.id.tv_taskListDate);
                    final TextView tv_taskcode = (TextView) view.findViewById(R.id.tv_taskCode);
                    final TextView tv_priority = (TextView) view.findViewById(R.id.tv_taskListPriority);
                    final TextView tv_status = (TextView) view.findViewById(R.id.tv_taskstatus);
                    final TextView tv_projectName = (TextView) view.findViewById(R.id.tv_projectNameTaskList);
                    final TextView tv_projectCode = (TextView) view.findViewById(R.id.tv_projectCodeTaskList);
                    groupTask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.radio_buttonAction) {
                                if (checkedId == R.id.radio_buttonAction) {
                                    selectedType = radioButtonTaskName.getText().toString();
                                    Snackbar snackbar = Snackbar.make(mContentLayout, "Confirm.", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            view1.setVisibility(View.VISIBLE);
                                            Snackbar snackbar1 = Snackbar.make(mContentLayout, "Task is restored!", Snackbar.LENGTH_SHORT);
                                            snackbar1.show();
                                        }
                                    });
                                    View sbView = snackbar.getView();
                                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            view1.setVisibility(View.GONE);
                                            mTaskListAdapter.removeItem(position);
                                            HashMap<String, String> userId = session.getUserDetails();
                                            String id = userId.get(UserPrefUtils.ID);
                                            final String taskOwnerName = userId.get(UserPrefUtils.NAME);
                                            final String name = mTaskName.getText().toString();
                                            final String date = tv_dueDate.getText().toString();
                                            String task_code = tv_taskcode.getText().toString();
                                            String task_prioroty = tv_priority.getText().toString();
                                            String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
                                            Call<TaskComplete> callComplete = ANApplications.getANApi().checkTheTaskComplete(id, task_code, orgn_code);
                                            callComplete.enqueue(new Callback<TaskComplete>() {
                                                @Override
                                                public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.body().getSuccess().equals("true")) {
                                                        } else {
                                                            Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        AndroidUtils.displayToast(getActivity(), "Something Went Wrong!!");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<TaskComplete> call, Throwable t) {
                                                    Log.d("CallBack", " Throwable is " + t);
                                                }
                                            });
                                            Snackbar snackbar2 = Snackbar.make(mContentLayout, "Task is completed!", Snackbar.LENGTH_SHORT);
                                            snackbar2.show();
                                        }
                                    });
                                    snackbar.show();
                                } else if (checkedId == 0) {
                                    selectedType = radioButtonTaskName.getText().toString();

                                }
                            }
                        }
                    });
                    mTaskName = (TextView) view.findViewById(R.id.tv_taskListName);
                    mTaskName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String taskOwnerName = userId.get(UserPrefUtils.NAME);
                            String name = mTaskName.getText().toString();
                            String date = tv_dueDate.getText().toString();
                            String task_code = tv_taskcode.getText().toString();
                            String task_prioroty = tv_priority.getText().toString();
                            String projectcode = tv_projectCode.getText().toString();

                            Intent i = new Intent(getActivity(), EditTaskActivity.class);
                            i.putExtra("TaskName", name);
                            i.putExtra("TaskDate", date);
                            i.putExtra("TaskCode", task_code);
                            i.putExtra("taskOwnerName", taskOwnerName);
                            i.putExtra("projectCode",projectcode);
                            i.putExtra("priority",task_prioroty);
                            startActivity(i);
                            System.out.println("user" + task_code);
                        }
                    });
                    ImageView mImageUserAdd = (ImageView) view.findViewById(R.id.img_useraddTaskList);
                    mImageUserAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            String projectCode = tv_projectCode.getText().toString();
                            Intent i = new Intent(getActivity(), InvitationActivity.class);
                            i.putExtra("TaskCode", task_code);
                            i.putExtra("SenIvitaionprojectCode", projectCode);
                            startActivity(i);

                        }
                    });
                    ImageView mImageComment = (ImageView) view.findViewById(R.id.img_commentTaskList);
                    mImageComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), CommentsActivity.class);
                            String name = mTaskName.getText().toString();
                            String date = tv_dueDate.getText().toString();
                            String task_code = tv_taskcode.getText().toString();
                            i.putExtra("TaskName", name);
                            i.putExtra("TaskDate", date);
                            i.putExtra("TaskCode", task_code);
                            startActivity(i);
                        }
                    });
                    ImageView mImageRaminder = (ImageView) view.findViewById(R.id.img_raminderTaskList);
                    mImageRaminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            Intent i = new Intent(getActivity(), ReaminderScreenActivity.class);
                            i.putExtra("TaskCode", task_code);
                            System.out.println("maximum" + task_code);
                            startActivity(i);

                        }
                    });
                    ImageView mImageDelete = (ImageView) view.findViewById(R.id.img_delete);
                    mImageDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showProgressDialog();
                            HashMap<String, String> userId = session.getUserDetails();
                            String id = userId.get(UserPrefUtils.ID);
                            String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
                            String task_code = tv_taskcode.getText().toString();
                            Call<TaskDelete> taskDeleteCall = ANApplications.getANApi().checkTheDelete(id, task_code, orgn_code);
                            taskDeleteCall.enqueue(new Callback<TaskDelete>() {
                                @Override
                                public void onResponse(Call<TaskDelete> call, Response<TaskDelete> response) {
                                    System.out.println("reponsedelete" + response.raw());
                                    if (response.isSuccessful()) {
                                        System.out.println("deleteResponse1" + response.raw());
                                        if (response.body().getSuccess().equals("true")) {
                                            hideProgressDialog();
                                            mTaskListAdapter.removeItem(position);
                                            Snackbar.make(mContentLayout, "Task Deleted Sucessfully", Snackbar.LENGTH_SHORT).show();
                                        } else {
                                            Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        AndroidUtils.displayToast(getActivity(), "Something Went Wrong!!");
                                    }

                                }

                                @Override
                                public void onFailure(Call<TaskDelete> call, Throwable t) {
                                    Log.d("CallBack", " Throwable is " + t);

                                }
                            });
                        }
                    });

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

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private OverDueTodayFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(OverDueTodayFragment context, final RecyclerView mRecylerViewSingleSub, OverDueTodayFragment.ClickListener clickListener) {
            this.clicklistener = clickListener;
            gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

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

    // offfline Data
    private void overDueTodayFragmentNoConnection() {
        AndroidUtils.showProgress( false, mProgressView, mContentLayout );
        TaskDBHelper taskDBHelper = new TaskDBHelper(getContext());
        Cursor cursor = taskDBHelper.getAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                TaskListRecords taskListRecords = new TaskListRecords();
                String name = cursor.getString(cursor.getColumnIndex(taskDBHelper.KEY_NAME));
                String date = cursor.getString(cursor.getColumnIndex(taskDBHelper.KEY_DUEDATE));
                String priority = cursor.getString(cursor.getColumnIndex(taskDBHelper.KEY_PRIORITY));
                String projectcode = cursor.getString(cursor.getColumnIndex(taskDBHelper.KEY_PROJECT_CODE));
                String taskcode = cursor.getString(cursor.getColumnIndex(taskDBHelper.KEY_TASK_CODE));
                String remindarscount = cursor.getString(cursor.getColumnIndex(taskDBHelper.KEY_REMINDARS_COUNT));
                String status = cursor.getString(cursor.getColumnIndex(taskDBHelper.KEY_STATUS));
                String projectName = cursor.getString(cursor.getColumnIndex(taskDBHelper.KEY_PROJECT_NAME));
                String type = cursor.getString(cursor.getColumnIndex(taskDBHelper.KEY_REPEAT_TYPE));
                taskListRecords.setName(name);
                taskListRecords.setDue_date(date);
                taskListRecords.setPriority(priority);
                taskListRecords.setProject_code(projectcode);
                taskListRecords.setTask_code(taskcode);
                taskListRecords.setRemindars_count(remindarscount);
                taskListRecords.setStatus(status);
                taskListRecords.setProject_name(projectName);
                taskListRecords.setRepeat_type(type);
                if (taskListRecords.getDue_date() != null) {
                    Date date1 = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(date1);
                    String date2[] = taskListRecords.getDue_date().split(" ");
                    String date3 = date2[0];
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String dateYes = dateFormat.format(yesterday());
                    Date dat6 = new Date(dateYes);
                    System.out.println("dateys" + dat6);
                    try {
                        Date date4 = new SimpleDateFormat("yyyy-MM-dd").parse(date3);
                        System.out.println("date3" + date4);
                        if (date4.before(dat6) && taskListRecords.getStatus().equals("1")) {
                            taskListRecordsArrayList.add(taskListRecords);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            mtodayOverDueRecylcerView.setAdapter(mTaskOfflineAdapter);
            mtodayOverDueRecylcerView.addOnItemTouchListener(new OverDueTodayFragment.RecyclerTouchListener(this, mtodayOverDueRecylcerView, new OverDueTodayFragment.ClickListener() {
                @Override
                public void onClick(final View view, final int position) {
                    final View view1 = view.findViewById(R.id.taskList_liner);
                    RadioGroup groupTask = (RadioGroup) view.findViewById(R.id.taskradioGroupTask);
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById(R.id.radio_buttonAction);
                    final TextView tv_dueDate = (TextView) view.findViewById(R.id.tv_taskListDate);
                    final TextView tv_taskcode = (TextView) view.findViewById(R.id.tv_taskCode);
                    final TextView tv_priority = (TextView) view.findViewById(R.id.tv_taskListPriority);
                    final TextView tv_status = (TextView) view.findViewById(R.id.tv_taskstatus);
                    final TextView tv_projectName = (TextView) view.findViewById(R.id.tv_projectNameTaskList);
                    final TextView tv_projectCode = (TextView) view.findViewById(R.id.tv_projectCodeTaskList);
                    groupTask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.radio_buttonAction) {
                                if (checkedId == R.id.radio_buttonAction) {
                                    selectedType = radioButtonTaskName.getText().toString();
                                    Snackbar snackbar = Snackbar.make(mContentLayout, "Completed.", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            view1.setVisibility(View.VISIBLE);
                                            Snackbar snackbar1 = Snackbar.make(mContentLayout, "Task is restored!", Snackbar.LENGTH_SHORT);
                                            snackbar1.show();
                                        }
                                    });
                                    View sbView = snackbar.getView();
                                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            view1.setVisibility(View.GONE);
                                            mTaskOfflineAdapter.removeItem(position);
                                            HashMap<String, String> userId = session.getUserDetails();
                                            String id = userId.get(UserPrefUtils.ID);
                                            final String taskOwnerName = userId.get(UserPrefUtils.NAME);
                                            final String name = mTaskName.getText().toString();
                                            final String date = tv_dueDate.getText().toString();
                                            String task_code = tv_taskcode.getText().toString();
                                            String task_prioroty = tv_priority.getText().toString();
                                            String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
                                            Call<TaskComplete> callComplete = ANApplications.getANApi().checkTheTaskComplete(id, task_code, orgn_code);
                                            callComplete.enqueue(new Callback<TaskComplete>() {
                                                @Override
                                                public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.body().getSuccess().equals("true")) {
                                                        } else {
                                                            Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        AndroidUtils.displayToast(getActivity(), "Something Went Wrong!!");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<TaskComplete> call, Throwable t) {
                                                    Log.d("CallBack", " Throwable is " + t);
                                                }
                                            });
                                            Snackbar snackbar2 = Snackbar.make(mContentLayout, "Task is completed!", Snackbar.LENGTH_SHORT);
                                            snackbar2.show();
                                        }
                                    });
                                    snackbar.show();
                                } else if (checkedId == 0) {
                                    selectedType = radioButtonTaskName.getText().toString();

                                }
                            }

                        }
                    });
                    mTaskName = (TextView) view.findViewById(R.id.tv_taskListName);
                    mTaskName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String taskOwnerName = userId.get(UserPrefUtils.NAME);
                            String name = mTaskName.getText().toString();
                            String date = tv_dueDate.getText().toString();
                            String task_code = tv_taskcode.getText().toString();
                            Intent i = new Intent(getActivity(), EditTaskActivity.class);
                            i.putExtra("TaskName", name);
                            i.putExtra("TaskDate", date);
                            i.putExtra("TaskCode", task_code);
                            i.putExtra("taskOwnerName", taskOwnerName);
                            startActivity(i);
                            System.out.println("user" + task_code);
                        }
                    });
                    ImageView mImageUserAdd = (ImageView) view.findViewById(R.id.img_useraddTaskList);
                    mImageUserAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            String projectCode = tv_projectCode.getText().toString();
                            Intent i = new Intent(getActivity(), InvitationActivity.class);
                            i.putExtra("TaskCode", task_code);
                            i.putExtra("SenIvitaionprojectCode", projectCode);
                            startActivity(i);

                        }
                    });
                    ImageView mImageComment = (ImageView) view.findViewById(R.id.img_commentTaskList);
                    mImageComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), CommentsActivity.class);
                            String name = mTaskName.getText().toString();
                            String date = tv_dueDate.getText().toString();
                            String task_code = tv_taskcode.getText().toString();
                            i.putExtra("TaskName", name);
                            i.putExtra("TaskDate", date);
                            i.putExtra("TaskCode", task_code);
                            startActivity(i);
                        }
                    });
                    ImageView mImageRaminder = (ImageView) view.findViewById(R.id.img_raminderTaskList);
                    mImageRaminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task_code = tv_taskcode.getText().toString();
                            Intent i = new Intent(getActivity(), ReaminderScreenActivity.class);
                            i.putExtra("TaskCode", task_code);
                            System.out.println("maximum" + task_code);
                            startActivity(i);

                        }
                    });

                }

                @Override
                public void onLongClick(View view, int position) {

                }

            }));
        }
    }
}


