package com.actnow.android.activities.insights;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.utils.UserPrefUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;

public class WeekelyTaskChartActivity extends AppCompatActivity  implements OnChartGestureListener, OnChartValueSelectedListener {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    String[] arrayItems = { "Weekly","Monthly","Yearly","Daily",};
    LineChart mlineChartWeekely;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_weekely_task_chart);
        appHeaderTwo();
        initializeViews();
        appFooter();
        setData();
    }
    private void appHeaderTwo() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back_two);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1_two);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2_two);
        btnLink2.setVisibility(GONE);
        btnLink1.setText("Thisweek");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));
        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendarAppHeaderTwo);
        btnCalendar.setVisibility(GONE);
        ImageView btnNotifications = (ImageView) findViewById(R.id.btn_notificationsAppHeaderTwo);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mContentLayout, "Work in progress!", Snackbar.LENGTH_SHORT).show();
            }
        });
        ImageView btnSettings = (ImageView) findViewById(R.id.btn_settingsAppHeaderTwo);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                i.putExtra("email", accountEmail);
                startActivity(i);
                finish();
            }
        });
        ImageView btnMenu = (ImageView) findViewById(R.id.img_menuTopTwo);
        btnMenu.setOnClickListener(new View.OnClickListener() {
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
                                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
                                startActivity(iTimeLine);
                                break;
                            case R.id.nav_filter:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_profile:
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get(UserPrefUtils.ID);
                                String name = userId.get(UserPrefUtils.NAME);
                                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                                Intent iprofile=new Intent(getApplicationContext(), EditAccountActivity.class);
                                iprofile.putExtra("id", id);
                                iprofile.putExtra("name", name);
                                iprofile.putExtra("email", accountEmail);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium=new Intent(getApplicationContext(), PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_thisweek:
                                Intent iThisWeek =new Intent(getApplicationContext(), ThisWeekActivity.class);
                                startActivity(iThisWeek);
                                break;
                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_weekelyChart);
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
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mlineChartWeekely =(LineChart) findViewById(R.id.line_chartGraphWeeekely);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_chart);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayItems);
        //arrayAdapter.setDropDownViewResource(R.layout.spinner_text_color);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // ((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorAccent));
                switch ( position ) {
                    case 0:
                        Toast.makeText(getApplicationContext(),"Selected the Weekely Chart",Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Intent iMonthley = new Intent(getApplicationContext(), MonthlyTaskChartActivity.class);
                        startActivity(iMonthley);
                        finish();
                        break;
                    case 2:
                        Intent iYealy = new Intent(getApplicationContext(), YearlyTaskChartActivity.class);
                        startActivity(iYealy);
                        finish();
                        break;
                    case 3:
                        Intent iToady = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
                        startActivity(iToady);
                        finish();
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
    private ArrayList<String> setXAxisValues(){
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");
        return xVals;
    }
    private ArrayList<Entry> setYAxisValues(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(60, 0));
        yVals.add(new Entry(48, 1));
        yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));
        return yVals;
    }
    private void setData() {
        ArrayList<String> xVals = setXAxisValues();
        ArrayList<Entry> yVals = setYAxisValues();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setFillAlpha(110);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mlineChartWeekely.setData(data);

    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            // or highlightTouch(null) for callback to onNothingSelected(...)
            mlineChartWeekely.highlightValues(null);

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mlineChartWeekely.getLowestVisibleXIndex()
                + ", high: " + mlineChartWeekely.getHighestVisibleXIndex());

        Log.i("MIN MAX", "xmin: " + mlineChartWeekely.getXChartMin()
                + ", xmax: " + mlineChartWeekely.getXChartMax()
                + ", ymin: " + mlineChartWeekely.getYChartMin()
                + ", ymax: " + mlineChartWeekely.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");

    }
    private void appFooter() {
    View btnMe = findViewById(R.id.btn_me);
        btnMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityToady();
            }
        });
        View btnProject = findViewById(R.id.btn_projects);
        btnProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityProject();
            }
        });
        View btnTask = findViewById(R.id.btn_task);
        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityTasks();
            }
        });
        View btnIndividuals = findViewById(R.id.btn_individuals);
        btnIndividuals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityIndividuals();
            }
        });
        View btnInsights = findViewById(R.id.btn_insights);
        btnInsights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityInsights();
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

    }

    private void activityTasks() {
        Intent i = new Intent(getApplicationContext(), TaskAddListActivity.class);
        startActivity(i);

    }

    private void activityIndividuals() {
        Intent i = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
        startActivity(i);

    }
    private void activityInsights() {
    /*    Intent i = new Intent(getApplicationContext(), PersonalActivity.class);
        startActivity(i);*/

    Toast.makeText(getApplicationContext(),"selected Insights",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

