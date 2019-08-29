package com.actnow.android.activities.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;

import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
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

import static android.view.View.GONE;

public class EditTaskActivity extends AppCompatActivity {

    EditText mTaskEditName, mEditTaskOwner, mDateTaskEdit, mTaskCommentEdit;
    View mEditProjectNewTask, mEditReminderNewTask, mEditIndvalNewTask, mEditPriorityNewTask;
    View mProgressView, mContentLayout;
    final Context context = this;
    UserPrefUtils session;
    String id;
    String taskOwnerName;
    String taskName;
    String taskDate;

    TextView mEditProjectCheckBox, mEditIndividuvalCheckBox;
    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<>();
    MultiSelectDialog mIndividuvalDialog, mProjectDialog;
    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray, projectArray;

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserPriorty=new ArrayList<>();
    TextView mPriortyEditTask;

    Spinner mSpinnerEditReptOption, mSpinnerEditWeekely, mSpinnerEditMonthly, mSpinnerEditYearly;

    ArrayAdapter<String> arrayAdapterReapt;
    ArrayAdapter<String> arrayAdapterWeekley;
    ArrayAdapter<String> arrayAdapterMonthly;
    ArrayAdapter<String> arrayAdapterYearly;

    String[] reapt = {"Daily", "Weekly", "Monthly", "Yearly"};
    String[] weekly = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    String[] days = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    String[] monthly = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_edit_task);
        header();
        initializeViews();
        footer();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            taskOwnerName = (String) b.get("taskOwnerName");
            mEditTaskOwner.setText(" " + taskOwnerName);
            taskName = (String) b.get("TaskName");
            mTaskEditName.setText("" + taskName);
            taskDate = (String) b.get("TaskDate");
            mDateTaskEdit.setText("" + taskDate);
            System.out.println("passsed" + taskOwnerName + id + taskName + taskDate);
        }
    }
    private void header() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.txt_title);
        tv_title.setText("Task Edit Name");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        ImageView imageMenu = (ImageView) findViewById(R.id.image_menu);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get(UserPrefUtils.ID);
                String taskOwnerName = userId.get(UserPrefUtils.NAME);
                ImageView mImageProfile = (ImageView) findViewById(R.id.img_profile);
                mImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),EditAccountActivity.class);
                        startActivity(i);
                    }
                });
                TextView mTextName =(TextView)findViewById(R.id.tv_nameProfile);
                mTextName.setText(taskOwnerName);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToady = new Intent(getApplicationContext(), TodayTaskActivity.class);
                                startActivity(iToady);
                                finish();
                                break;
                            case R.id.nav_timeLine:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_filter:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_profile:
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get(UserPrefUtils.ID);
                                String name = userId.get(UserPrefUtils.NAME);
                                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                                Intent iprofile=new Intent(EditTaskActivity.this, EditAccountActivity.class);
                                iprofile.putExtra("id", id);
                                iprofile.putExtra("name", name);
                                iprofile.putExtra("email", accountEmail);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium=new Intent(EditTaskActivity.this, PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek= new Intent(EditTaskActivity.this, ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutTaskEditView);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                ImageView imgeClose =(ImageView)findViewById(R.id.nav_close);
                imgeClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    }
                });
            }
        });
    }

    private void initializeViews() {
        spinnerEdit();
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mTaskEditName = findViewById(R.id.et_newEditTaskName);
        mEditTaskOwner = findViewById(R.id.et_newEdittaskOwner);
        mDateTaskEdit = findViewById(R.id.et_duedateNewEditTaskName);
        mTaskCommentEdit = findViewById(R.id.et_commentNewEditTask);

        mEditReminderNewTask = findViewById(R.id.re_editreminderNewTask);
        mEditReminderNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
                        new DatePickerDialog(getApplicationContext(), date, remianderCalender
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
                        mTimePicker = new TimePickerDialog(getApplicationContext(), new TimePickerDialog.OnTimeSetListener() {
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
                mAddTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Work in Progress!", Toast.LENGTH_SHORT).show();
                    }
                });
                TextView mCancelTextView =(TextView)dialog.findViewById(R.id.tv_remainderCancel);
                mCancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Work in Progress!", Toast.LENGTH_SHORT).show();
                    }
                });
                ImageView mImgDropRemainder =(ImageView)dialog.findViewById(R.id.imge_reminderDropDown);
                mImgDropRemainder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIndividuvalDialog.show(getSupportFragmentManager(), "mIndividuvalDialog");

                    }
                });
                dialog.show();
            }
        });
        mEditPriorityNewTask = findViewById(R.id.re_EditpriorityNewTask);
        mPriortyEditTask  =(TextView)findViewById(R.id.tv_editTaskPriorty);
        listItems = getResources().getStringArray(R.array.priorty);
        checkedItems = new boolean[listItems.length];
        mEditPriorityNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditTaskActivity.this);
                mBuilder.setTitle("ADD TO PRIORITY");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked){
                            if (! mUserPriorty.contains(position)){
                                mUserPriorty.add(position);
                            }else {
                                mUserPriorty.remove(position);
                            }
                        }

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item =" ";
                        for (int i=0;i<mUserPriorty.size();i++){
                            item = item + listItems[mUserPriorty.get(i)];
                            if (i != mUserPriorty.size()-1){
                                item = item + " ";
                            }
                        }
                        mPriortyEditTask.setText(item);

                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        });
        mEditProjectNewTask = (View)findViewById(R.id.re_EditTaskProject);
        mEditProjectCheckBox =(TextView)findViewById(R.id.tv_editTaskProject);
        mProjectDialog = new MultiSelectDialog();
        projectListCheckBox = new ArrayList<>();
        projectListCheckBox.add(0);
        mEditProjectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProjectDialog.show(getSupportFragmentManager(), "mProjectDialog");
            }
        });
        mEditIndvalNewTask = (View) findViewById(R.id.re_EditTaskNewIndividual);
        mEditIndividuvalCheckBox = (TextView) findViewById(R.id.tv_editTaskIndividuval);
        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add(0);
        mEditIndividuvalCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndividuvalDialog.show(getSupportFragmentManager(), "mIndividuvalDialog");
            }
        });
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get(UserPrefUtils.ID);
        requestDynamicContent(id);
        requestDynamicProjectList(id);

    }

    private void spinnerEdit() {

        mSpinnerEditReptOption = (Spinner) findViewById(R.id.spinner_ReaptEdit);
        mSpinnerEditWeekely = (Spinner) findViewById(R.id.spinner_weekleyEdit);
        mSpinnerEditMonthly = (Spinner) findViewById(R.id.spinner_monthlyEdit);
        mSpinnerEditYearly = (Spinner) findViewById(R.id.spinner_yearlyEdit);

        arrayAdapterMonthly = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, days);
        mSpinnerEditMonthly.setAdapter(arrayAdapterMonthly);

        arrayAdapterYearly = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, monthly);
        mSpinnerEditYearly.setAdapter(arrayAdapterYearly);

        arrayAdapterWeekley = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, weekly);
        mSpinnerEditWeekely.setAdapter(arrayAdapterWeekley);

        arrayAdapterReapt = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, reapt);
        mSpinnerEditReptOption.setAdapter(arrayAdapterReapt);

        mSpinnerEditReptOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getSelectedItem().toString();

                if (selectedItem.equals("Weekly")) {
                    mSpinnerEditWeekely.setVisibility(View.VISIBLE);
                    mSpinnerEditMonthly.setVisibility(GONE);
                    mSpinnerEditYearly.setVisibility(GONE);
                }if (selectedItem.equals("Monthly")){
                    mSpinnerEditMonthly.setVisibility(View.VISIBLE);
                    mSpinnerEditWeekely.setVisibility(GONE);
                    mSpinnerEditYearly.setVisibility(GONE);

                }if (selectedItem.equals("Yearly")){
                    mSpinnerEditYearly.setVisibility(View.VISIBLE);
                    mSpinnerEditMonthly.setVisibility(View.VISIBLE);
                    mSpinnerEditWeekely.setVisibility(GONE);
                }if (selectedItem.equals("Daily")){
                    mSpinnerEditYearly.setVisibility(GONE);
                    mSpinnerEditMonthly.setVisibility(GONE);
                    mSpinnerEditWeekely.setVisibility(GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void requestDynamicContent(String id) {
        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse(id);
        call.enqueue(new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                System.out.println("xxx"+ response.raw());
                if (response.isSuccessful()) {
                    System.out.println("raw"+ response.raw());
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
        System.out.println("check"+ orgn_users_records);
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get(i);
                listOfIndividuval.add(new MultiSelectModel(Integer.parseInt(orgnUserRecordsCheckBox.getId()), orgnUserRecordsCheckBox.getName()));
                System.out.println("dta"+ listOfIndividuval);
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
                                mEditIndividuvalCheckBox.setText(dataString);
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
    private void requestDynamicProjectList(String id) {
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse(id);
        call.enqueue(new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                System.out.println("api"+ response.raw());
                if (response.isSuccessful()) {
                    System.out.println("data"+response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        setProjectFooterList(response.body().getProject_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<ProjectListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });

    }
    private void setProjectFooterList(List<ProjectListResponseRecords> project_records) {
        if (project_records.size() > 0) {
            for (int i = 0; project_records.size() > i; i++) {
                ProjectListResponseRecords projectListResponseRecords = project_records.get(i);
                listOfProjectNames.add(new MultiSelectModel(Integer.parseInt(projectListResponseRecords.getProject_id()), projectListResponseRecords.getName()));
                System.out.println("output" + projectListResponseRecords);
            }
            mProjectDialog = new MultiSelectDialog()
                    .title("Project") //setting title for dialog
                    .titleSize(25)
                    .positiveText("Done")
                    .negativeText("Cancel")
                    .preSelectIDsList(projectListCheckBox)
                    .setMinSelectionLimit(0)
                    .setMaxSelectionLimit(listOfProjectNames.size())
                    .multiSelectList(listOfProjectNames) // the multi select model list with ids and name
                    .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                        @Override
                        public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                            for (int i = 0; i < selectedIds.size(); i++) {
                                mEditProjectCheckBox.setText(dataString);
                            }
                            projectArray = new JSONArray(selectedIds);
                        }

                        @Override
                        public void onCancel() {
                            Log.d("TAG", "Dialog cancelled");
                        }
                    });
        }


    }

    private void footer() {
        ImageView imageGallery = (ImageView) findViewById(R.id.image_gallery);
        imageGallery.setVisibility(GONE);
        ImageView imageAttachament = (ImageView) findViewById(R.id.image_attachament);
        imageAttachament.setVisibility(GONE);

        ImageView imageCamera = (ImageView) findViewById(R.id.image_camera);
        imageCamera.setVisibility(GONE);
        ImageView imageProfile = (ImageView) findViewById(R.id.image_profile);
        imageProfile.setVisibility(GONE);

        TextView tv_create = (TextView) findViewById(R.id.tv_create);
        tv_create.setText("Update");
        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditTaskActivity.this, TaskAddListActivity.class);
                startActivity(i);
                //attemptUpdateTask();
            }
        });
    }



}
