package com.actnow.android.activities.taskspinner;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.sdk.responses.OverDueTaskRecords;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverDueTaskActivity extends AppCompatActivity {
    RecyclerView mRecyclerViewPriority;
    RecyclerView.LayoutManager mLayoutManager;
    String[] arrayItems = {"Over Due TaskOffline", "Priorty TaskOffline"};
    private ArrayList<OverDueTaskRecords> overDueTaskRecordsArrayList = new ArrayList<OverDueTaskRecords>();
    UserPrefUtils session;
    View mProgressView,mContentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_over_due_task);
        appHeader();
        initializeViews();
    }

    private void appHeader() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.txt_title);
        tv_title.setVisibility(View.GONE);
        ImageView imageMenu = (ImageView) findViewById(R.id.image_menu);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
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
                });                TextView mTextName =(TextView)findViewById(R.id.tv_nameProfile);
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
                          /*  case R.id.nav_filter:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;*/
                            case R.id.nav_profile:
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get(UserPrefUtils.ID);
                                String name = userId.get(UserPrefUtils.NAME);
                                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                                Intent iprofile=new Intent(OverDueTaskActivity.this, EditAccountActivity.class);
                                iprofile.putExtra("id", id);
                                iprofile.putExtra("name", name);
                                iprofile.putExtra("email", accountEmail);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium=new Intent(OverDueTaskActivity.this,PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek= new Intent(OverDueTaskActivity.this, ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
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
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayItems);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text_color);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorAccent));
                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Over Due TaskOffline", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Intent i = new Intent(OverDueTaskActivity.this, PriorityTaskActivity.class);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mRecyclerViewPriority = (RecyclerView) findViewById(R.id.re_priorityTask);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewPriority.setLayoutManager(mLayoutManager);
        mRecyclerViewPriority.setItemAnimator(new DefaultItemAnimator());

        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
       // System.out.println("userId" + id);

      /*  Call<OverDueTaskListResponse> call = ANApplications.getANApi().checkOverDueTaskList(id);
        call.enqueue(new Callback<OverDueTaskListResponse>() {
            @Override
            public void onResponse(Call<OverDueTaskListResponse> call, Response<OverDueTaskListResponse> response) {
                AndroidUtils.showProgress(false,mProgressView,mContentLayout);
                if (response.isSuccessful()) {
                    //System.out.println("response1" + response.raw());
                    if (response.body().getSuccess().equals("true")) {
                      //  System.out.println("response2" + response.body().getSuccess());
                        setOverDueList(response.body().getOverdue_tasks());
                    }else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<OverDueTaskListResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);

            }
        });*/

    }

    private void setOverDueList(List<OverDueTaskRecords> overdue_tasks) {
        if (overdue_tasks.size() > 0) {
            for (int i = 0; overdue_tasks.size() > i; i++) {
                OverDueTaskRecords overDueTaskRecords = overdue_tasks.get(i);
                OverDueTaskRecords overDueTaskRecords1 = new OverDueTaskRecords();
                overDueTaskRecords1.setName(overDueTaskRecords.getName());
                overDueTaskRecords1.setDue_date(overDueTaskRecords.getDue_date());
                overDueTaskRecords1.setCreated_date(overDueTaskRecords.getCreated_date());
                overDueTaskRecordsArrayList.add(overDueTaskRecords1);
            }
        }
    }
}
