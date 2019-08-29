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
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.sdk.responses.TaskListResponse;
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

import static com.actnow.android.R.layout.task_list_cutsom;


public class AssignedFragment extends Fragment {
    FloatingActionButton fabAssignedTask;
    RecyclerView mAssignedTaskRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fabPriorityTask;
    TaskListAdapter mTaskListAdapter;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    private String selectedType = "";

    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();
    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();
    String id;
    MultiSelectDialog mIndividuvalDialogtime, mProjectDialogtime;

    public  AssignedFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils(getContext());
        View view = inflater.inflate(R.layout.fragment_assigned, container, false);

        mProgressView = view.findViewById(R.id.progress_bar);
        mContentLayout = view.findViewById(R.id.content_layout);
        mIndividuvalDialogtime = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add(0);
        mAssignedTaskRecylcerView = (RecyclerView) view.findViewById(R.id.assinedtask_recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAssignedTaskRecylcerView.setLayoutManager(mLayoutManager);
        mAssignedTaskRecylcerView.setItemAnimator(new DefaultItemAnimator());
        mTaskListAdapter = new TaskListAdapter(taskListRecordsArrayList, task_list_cutsom, getContext());
        mAssignedTaskRecylcerView.setAdapter(mTaskListAdapter);
        requestDynamicContent();
        attemptTaskList();
        fabAssignedTask = view.findViewById(R.id.fab_assignedtask);
        fabAssignedTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get( UserPrefUtils.ID);
                String taskOwnerName = userId.get(UserPrefUtils.NAME);
                Intent i = new Intent(getActivity(), ViewTasksActivity.class);
                i.putExtra("id", id);
                i.putExtra("taskOwnerName", taskOwnerName);
                startActivity(i);
            }
        });
     return view;
    }

    private void attemptTaskList() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<TaskListResponse> call = ANApplications.getANApi().checkTheTaskListResponse(id);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                if (response.isSuccessful()) {
                    System.out.println("url" + response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        setProjectFooterList(response.body().getTask_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    //   AndroidUtils.displayToast(getActivity(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);

            }
        });
    }

    private void setProjectFooterList(List<TaskListRecords> taskListRecordsList) {
        if (taskListRecordsList.size() > 0) {
            for (int i = 0; taskListRecordsList.size() > i; i++) {
                TaskListRecords taskListRecords = taskListRecordsList.get(i);
                TaskListRecords taskListRecords1 = new TaskListRecords();
                taskListRecords1.setName(taskListRecords.getName());
                taskListRecords1.setDue_date(taskListRecords.getDue_date());
                taskListRecords1.setPriority(taskListRecords.getPriority());
                //taskListRecords1.setRemindars_count(taskListRecords.getRemindars_count());
                taskListRecordsArrayList.add(taskListRecords1);
            }
            mAssignedTaskRecylcerView.setAdapter(new TaskListAdapter(taskListRecordsArrayList, task_list_cutsom, getContext()));
            mAssignedTaskRecylcerView.addOnItemTouchListener(new AssignedFragment.RecyclerTouchListener(this, mAssignedTaskRecylcerView, new AssignedFragment.ClickListener() {
                @Override
                public void onClick(final View view, int position) {
                    final View view1 = view.findViewById(R.id.taskList_liner);
                    RadioGroup groupTask = (RadioGroup) view.findViewById(R.id.taskradioGroupTask);
                    final RadioButton radioButtonTaskName = (RadioButton) view.findViewById(R.id.radio_buttonAction);
                    final TextView tv_dueDate = (TextView) view.findViewById(R.id.tv_taskListDate);
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
                                    TextView textView =(TextView)sbView.findViewById(R.id.snackbar_text);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Toast.makeText(getActivity(),"Compledt the TASK", Toast.LENGTH_SHORT).show();
                                            view1.setVisibility(View.GONE);
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
                    TextView mTaskName = (TextView) view.findViewById(R.id.tv_taskListName);
                    mTaskName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> userId = session.getUserDetails();
                            String taskOwnerName = userId.get(UserPrefUtils.NAME);
                            String s = radioButtonTaskName.getText().toString();
                            String s1 = tv_dueDate.getText().toString();
                            Intent i = new Intent(getActivity(), EditTaskActivity.class);
                            i.putExtra("TaskName", s);
                            i.putExtra("TaskDate", s1);
                            i.putExtra("taskOwnerName", taskOwnerName);
                            startActivity(i);
                        }
                    });
                    ImageView mImageUserAdd = (ImageView) view.findViewById(R.id.img_useraddTaskList);
                    mImageUserAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mIndividuvalDialogtime.show(getFragmentManager(), "mIndividuvalDialog");

                        }
                    });
                    ImageView mImageComment = (ImageView) view.findViewById(R.id.img_commentTaskList);
                    mImageComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), CommentsActivity.class);
                            startActivity(i);
                        }
                    });
                    ImageView mImageRaminder = (ImageView) view.findViewById(R.id.img_raminderTaskList);
                    mImageRaminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_Alert);
                            dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.remainder_list_add);
                            final Calendar remianderCalender = Calendar.getInstance();
                            final EditText ed_dateRaminder = (EditText) dialog.findViewById(R.id.ed_dateReminder);
                            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    remianderCalender.set(Calendar.YEAR, year);
                                    remianderCalender.set(Calendar.MONTH, monthOfYear);
                                    remianderCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    String myFormat = "yyyy-MM-dd"; //In which you need put here
                                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                                    ed_dateRaminder.setText(sdf.format(remianderCalender.getTime()));
                                }
                            };
                            ed_dateRaminder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new DatePickerDialog(getActivity(), date, remianderCalender
                                            .get(Calendar.YEAR), remianderCalender.get(Calendar.MONTH),
                                            remianderCalender.get(Calendar.DAY_OF_MONTH)).show();
                                }
                            });
                            final EditText ed_timeRemiander = (EditText) dialog.findViewById(R.id.ed_timeReminder);
                            ed_timeRemiander.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Calendar mcurrentTime = Calendar.getInstance();
                                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                    int minute = mcurrentTime.get(Calendar.MINUTE);
                                    TimePickerDialog mTimePicker;
                                    mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                            ed_timeRemiander.setText(selectedHour + ":" + selectedMinute);
                                        }
                                    }, hour, minute, true);//Yes 24 hour time
                                    mTimePicker.setTitle("Select Time");
                                    mTimePicker.show();
                                }
                            });
                            final TextView mAddTextView =(TextView)dialog.findViewById(R.id.tv_remainderAdd);
                            mAddTextView.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getActivity(), "Work in Progress!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            TextView mCancelTextView =(TextView)dialog.findViewById(R.id.tv_remainderCancel);
                            mCancelTextView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getActivity(), "Work in Progress!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            ImageView mImgDropRemainder =(ImageView)dialog.findViewById(R.id.imge_reminderDropDown);
                            mImgDropRemainder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mIndividuvalDialogtime.show(getFragmentManager(), "mIndividuvalDialog");

                                }
                            });

                            dialog.show();
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

        private AssignedFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(AssignedFragment context, final RecyclerView mRecylerViewSingleSub, AssignedFragment.ClickListener clickListener) {
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
                    AndroidUtils.displayToast(getActivity(), "Something Went Wrong!!");
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
            mIndividuvalDialogtime = new MultiSelectDialog()
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
                                // mIndividualCheckBox.setText(dataString);
                            }
                            individuvalArray = new JSONArray(selectedIds);
                        }

                        @Override
                        public void onCancel() {
                            Log.d("TAG", "Dialog cancelled");
                        }
                    });
        }
    }
}
