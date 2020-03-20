package com.actnow.android.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.insights.InsightsChart;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.AdavncedSearchAdapter;
import com.actnow.android.adapter.NewTaskProjectAdapter;
import com.actnow.android.sdk.responses.AdavancedSearch;
import com.actnow.android.sdk.responses.AdavancedTaskRecords;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.sdk.responses.TaskComplete;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.activeandroid.Cache.getContext;

public class AdvancedSearchActivity extends AppCompatActivity {
    final Context context = this;
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    TextView mDateAdvanced, mProjectAdvanced, mAdvancedIndividuals;
    private int mYear, mMonth, mDay;
    private ProgressDialog mProgressDialog;


    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();

    MultiSelectDialog mIndividuvalDialog, mProjectDialog;
    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;

    RecyclerView mRecyclerViewAdavanced;
    RecyclerView.LayoutManager mLayoutManager;
    AdavncedSearchAdapter mAdavncedSearchAdapter;
    private ArrayList<AdavancedTaskRecords> adavancedTaskRecordsArrayList = new ArrayList<AdavancedTaskRecords>();

    Button mButtonInfo;
    EditText mAdvancedSearchEditText;

    TextView mDateInVisible, mProjectCodeInvisible, mIndividualInvisible;


    RecyclerView mRecyclerViewDateTime;
    NewTaskProjectAdapter mNewTaskProjectAdapter;
    RecyclerView.LayoutManager mProjectLayoutManager;
    private ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList = new ArrayList<ProjectListResponseRecords>();

    String projectcode;
    String projectName;

    TextView mProjectNameDailog;
    TextView mProjectCodeDailog;

    private View view;
    private boolean add = false;
    private Paint p = new Paint();
    String task_code;
    private int edit_position;

    TextView tv_taskKeyName;
    TextView tv_taskadavncedId;
    TextView tv_taskCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_adanced_search);
        advancedSearch();
        initializeViews();
        appFooter();

    }

    private void advancedSearch() {
        ImageView mImgBackSearchAdvanced = (ImageView) findViewById(R.id.img_backAdvnaced);
        mImgBackSearchAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final ImageView imgProjects = (ImageView) findViewById(R.id.img_advancedfilterProject);
        imgProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectDailog();
                imgProjects.setImageResource(R.drawable.ic_project_list_black);

            }
        });

        final ImageView imgIndividuals = (ImageView) findViewById(R.id.img_advancedbyEvent);
        imgIndividuals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgIndividuals.setImageResource(R.drawable.ic_menu_black_user);
                mIndividuvalDialog.show(getSupportFragmentManager(), "mIndividuvalDialog");
            }
        });
        final ImageView imgDate = (ImageView) findViewById(R.id.img_advancedcalender);
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgDate.setImageResource(R.drawable.ic_calendar);
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdvancedSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDateInVisible.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mProjectDialog = new MultiSelectDialog();
        projectListCheckBox = new ArrayList<>();
        projectListCheckBox.add(0);

        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add(0);
        requestIndividualDynamicContent();
        mAdvancedSearchEditText = (EditText) findViewById(R.id.edit_advncedSearch);
        view = getLayoutInflater().inflate(R.layout.custom_approval_tasklist, null);


        mButtonInfo = (Button) findViewById(R.id.bt_getInfoAdvanced);
        mButtonInfo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                getInfoAdvanced();
            }
        });
        mDateInVisible = (TextView) findViewById(R.id.tv_dateAdvancedMain);
        mProjectCodeInvisible = (TextView) findViewById(R.id.tv_projectAdvancedMain);
        mIndividualInvisible = (TextView) findViewById(R.id.tv_individualAdvancedMain);
        mRecyclerViewAdavanced = findViewById(R.id.advancedSearch_recyclerView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewAdavanced.setLayoutManager(mLayoutManager);
        mRecyclerViewAdavanced.setItemAnimator(new DefaultItemAnimator());
        mAdavncedSearchAdapter = new AdavncedSearchAdapter(adavancedTaskRecordsArrayList, R.layout.custom_advanced_list, getApplicationContext());
        mRecyclerViewAdavanced.setAdapter(mAdavncedSearchAdapter);
        view = getLayoutInflater().inflate(R.layout.custom_advanced_list, null);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getInfoAdvanced() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        String editText = mAdvancedSearchEditText.getText().toString();
        mAdvancedSearchEditText.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(editText)) {
            mAdvancedSearchEditText.setError("please entere the Task_key");
            focusView = mAdvancedSearchEditText;
            cancel = true;
        } else {
            Call<AdavancedSearch> adavancedSearchCall = ANApplications.getANApi().adavancedSearch(id, editText, "", "", "");
            System.out.println("adavmcedValues " + id + editText + "" + "" + String.valueOf(individuvalArray).replace("[", "").replace("]", ""));
            adavancedSearchCall.enqueue(new Callback<AdavancedSearch>() {
                @Override
                public void onResponse(Call<AdavancedSearch> call, Response<AdavancedSearch> response) {
                    System.out.println("advancedReponse2" + response.raw());
                    if (response.isSuccessful()) {
                        System.out.println("advancedReponse" + response.raw());
                        if (response.body().getSuccess().equals("true")) {
                            System.out.println("advancedReponse1" + response.body().getTask_records());
                            advanceList(response.body().getTask_records());
                        } else {
                            Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                    }
                }

                @Override
                public void onFailure(Call<AdavancedSearch> call, Throwable t) {
                    Log.d("CallBack", " Throwable is " + t);

                }
            });
        }
    }

    private void advanceList(List<AdavancedTaskRecords> task_records) {
        if (task_records.size() > 0) {
            for (int i = 0; task_records.size() > i; i++) {
                AdavancedTaskRecords adavancedTaskRecords = task_records.get(i);
                AdavancedTaskRecords adavancedTaskRecords1 = new AdavancedTaskRecords();
                adavancedTaskRecords1.setName(adavancedTaskRecords.getName());
                adavancedTaskRecords1.setTask_code(adavancedTaskRecords.getTask_code());
                adavancedTaskRecords1.setTask_id(adavancedTaskRecords.getTask_id());
                adavancedTaskRecordsArrayList.add(adavancedTaskRecords1);
                System.out.println("Adavanceddate" + adavancedTaskRecords1);
            }
            mRecyclerViewAdavanced.setAdapter(new AdavncedSearchAdapter(adavancedTaskRecordsArrayList, R.layout.custom_advanced_list, getApplicationContext()));
            mRecyclerViewAdavanced.addOnItemTouchListener(new AdvancedSearchActivity.RecyclerTouchListener(this, mRecyclerViewAdavanced, new AdvancedSearchActivity.ClickListener() {
                @Override
                public void onClick(final View view, final int position) {
                    initSwipe();
                     tv_taskKeyName = (TextView) findViewById(R.id.tv_taskKeyName);
                     tv_taskadavncedId = (TextView) findViewById(R.id.tv_taskAdvancedID);
                     tv_taskCode = (TextView) findViewById(R.id.tv_taskCode);
                }

                @Override
                public void onLongClick(View view, int position) {

                }

            }));
        }
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    mAdavncedSearchAdapter.removeItem(position);
                    HashMap<String, String> userId = session.getUserDetails();
                    String id = userId.get(UserPrefUtils.ID);
                    String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
                    task_code = tv_taskCode.getText().toString();
                    Call<TaskComplete> callDelete = ANApplications.getANApi().checkTheTaskApprove(id, task_code, orgn_code);
                    callDelete.enqueue(new Callback<TaskComplete>() {
                        @Override
                        public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getSuccess().equals("true")) {
                                    //Toast.makeText(getApplicationContext(),"TASK APPROVE",Toast.LENGTH_SHORT ).show();

                                } else {
                                    Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                            }
                        }

                        @Override
                        public void onFailure(Call<TaskComplete> call, Throwable t) {
                            Log.d("CallBack", " Throwable is " + t);

                        }
                    });

                } else {
                    removeView();
                    edit_position = position;
                    mAdavncedSearchAdapter.removeItem(position);
                    HashMap<String, String> userId = session.getUserDetails();
                    String id = userId.get(UserPrefUtils.ID);
                    String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
                    task_code = tv_taskCode.getText().toString();
                    Call<TaskComplete> callEdit = ANApplications.getANApi().checkTheDisApprove(id, task_code, orgn_code);
                    callEdit.enqueue(new Callback<TaskComplete>() {
                        @Override
                        public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getSuccess().equals("true")) {
                                } else {
                                    Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                            }
                        }

                        @Override
                        public void onFailure(Call<TaskComplete> call, Throwable t) {
                            Log.d("CallBack", " Throwable is " + t);

                        }
                    });

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.check);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.letter);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerViewAdavanced);
    }

    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getApplicationContext());
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


    private void projectDailog() {
        requestDynamicProjectList();
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dailog_projectname_projectcode);
        mRecyclerViewDateTime = dialog.findViewById(R.id.recyleView_projectNameCode);
        mProjectLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewDateTime.setLayoutManager(mProjectLayoutManager);
        mRecyclerViewDateTime.setItemAnimator(new DefaultItemAnimator());
        mNewTaskProjectAdapter = new NewTaskProjectAdapter(projectListResponseRecordsArrayList, R.layout.custom_project_dailog, getApplicationContext());
        mRecyclerViewDateTime.setAdapter(mNewTaskProjectAdapter);
        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_dailogOk);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectName = mProjectNameDailog.getText().toString();
                projectcode = mProjectCodeDailog.getText().toString();
                mProjectCodeInvisible.setText(projectcode);
                dialog.dismiss();
            }
        });
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_dailogCancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void requestDynamicProjectList() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse(id);
        call.enqueue(new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                //AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        setProjectFooterList(response.body().getProject_records());
                    } else {
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

    private void setLoadCheckBox(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get(i);
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setEmail(orgnUserRecordsCheckBox.getEmail());
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
                                mIndividualInvisible.setText(dataString);
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

    private void requestIndividualDynamicContent() {
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
                        // Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
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

    private void setProjectFooterList(List<ProjectListResponseRecords> project_records) {
        if (project_records.size() > 0) {
            for (int i = 0; project_records.size() > i; i++) {
                ProjectListResponseRecords projectListResponseRecords = project_records.get(i);
                ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
                projectListResponseRecords1.setName(projectListResponseRecords.getName());
                projectListResponseRecords1.setProject_code((projectListResponseRecords.getProject_code()));
                projectListResponseRecordsArrayList.add(projectListResponseRecords1);
            }
            mRecyclerViewDateTime.setAdapter(new NewTaskProjectAdapter(projectListResponseRecordsArrayList, R.layout.custom_project_footer, getApplicationContext()));
            mRecyclerViewDateTime.addOnItemTouchListener(new AdvancedSearchActivity.RecyclerTouchListener(this, mRecyclerViewDateTime, new AdvancedSearchActivity.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    mProjectNameDailog = (TextView) view.findViewById(R.id.tv_projectNameDailog);
                    projectName = mProjectNameDailog.getText().toString();
                    mProjectCodeDailog = (TextView) view.findViewById(R.id.tv_projectCodeDailog);

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
        private AdvancedSearchActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(AdvancedSearchActivity context, final RecyclerView mRecylerViewSingleSub, AdvancedSearchActivity.ClickListener clickListener) {
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    private void appFooter() {
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_marignBottom);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityInsights();
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottomNavigationToday:
                        activityToady();
                        return true;
                    case R.id.bottomNavigationProjects:
                        activityProject();
                        return true;
                    case R.id.bottomNavigationTask:
                        activityTasks();
                        return true;
                    case R.id.bottomNavigationIndividuals:
                        activityIndividuals();
                        return true;

                }
                return false;
            }
        });
    }

    private void activityToady() {
        Intent i = new Intent(getApplicationContext(), TodayTaskActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityProject() {
        Intent i = new Intent(getApplicationContext(), ProjectFooterActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityTasks() {
        Intent i = new Intent(getApplicationContext(), TaskAddListActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityIndividuals() {
        Intent i = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void activityInsights() {
        Intent i = new Intent(getApplicationContext(), InsightsChart.class);
        startActivity(i);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    public void onBackPressed() {
        super.onBackPressed();

    }

}
