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

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.sdk.responses.MontnlyInsighsRepnseData;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class MonthlyTaskChartActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    String[] arrayItems = {"Monthly", "Yearly", "Daily", "Weekly",};
    LineChart mlineChartMonthly;

    TextView mMonthly_no_of_tasks, mMonthly_no_of_tasks_cricle;
    TextView mMonthly_no_of_ctasks;
    TextView mMonthlyDayTask;
    TextView mCompltedMonthlyTask;

    ArrayList<Entry> y;
    ArrayList<String> x;

    ArrayList<MontnlyInsighsRepnseData> montnlyInsighsRepnseDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_monthly_task_chart);
        appHeaderTwo();
        initializeViews();
        appFooter();

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
        btnLink1.setText("This month");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));

        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_insightsrAppHeaderTwo);
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
                String email = userId.get(UserPrefUtils.EMAIL);
                ImageView mImageProfile = (ImageView) findViewById(R.id.img_profile);
                String img = userId.get(UserPrefUtils.IMAGEPATH);
                System.out.println("img" + img);
                Glide.with(getApplicationContext())
                        .load(img)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(mImageProfile);
                mImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), EditAccountActivity.class);
                        startActivity(i);
                    }
                });
                TextView mTextName = (TextView) findViewById(R.id.tv_nameProfile);
                mTextName.setText(taskOwnerName);
                TextView mTextEmail = (TextView) findViewById(R.id.tv_emailProfile);
                mTextEmail.setText(email);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToday = new Intent(getApplicationContext(), TodayTaskActivity.class);
                                startActivity(iToday);
                                break;
                            case R.id.nav_idea:
                                Intent iIdea = new Intent(getApplicationContext(), ViewIdeasActivity.class);
                                startActivity(iIdea);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent(getApplicationContext(), ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                            case R.id.nav_taskfilter:
                                Intent iTaskfilter = new Intent(getApplicationContext(), TaskAddListActivity.class);
                                startActivity(iTaskfilter);
                                break;
                            case R.id.nav_project:
                                Intent iProject = new Intent(getApplicationContext(), ProjectFooterActivity.class);
                                startActivity(iProject);
                                break;
                            case R.id.nav_individuals:
                                Intent iIndividuals = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
                                startActivity(iIndividuals);
                                break;
                            case R.id.nav_insights:
                                Toast.makeText(getApplicationContext(), "Selected Insights", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
                                startActivity(iTimeLine);
                                break;
                            case R.id.nav_profile:
                                Intent iprofile = new Intent(getApplicationContext(), AccountSettingActivity.class);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium = new Intent(getApplicationContext(), PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_logout:
                                session.logoutUser();
                                break;

                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_monthlyChart);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                ImageView imgeClose = (ImageView) findViewById(R.id.nav_close);
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

        y = new ArrayList<Entry>();
        x = new ArrayList<String>();
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mlineChartMonthly = (LineChart) findViewById(R.id.line_chartGraphMonthly);

        mMonthly_no_of_tasks = (TextView) findViewById(R.id.tv_numberTotalTaks);
        mMonthly_no_of_ctasks = (TextView) findViewById(R.id.tv_numberOfComleteTaks);
        mMonthly_no_of_tasks_cricle = (TextView) findViewById(R.id.tv_totalTaksInCricle);

        mMonthlyDayTask = (TextView) findViewById(R.id.monthlyData);
        mCompltedMonthlyTask = (TextView) findViewById(R.id.monthlyComlData);


        final Spinner spinner = (Spinner) findViewById(R.id.spinner_chart);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayItems);
        //arrayAdapter.setDropDownViewResource(R.remider_footer_layout.spinner_text_color);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.colorWhite.colorAccent));
                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Seleted the Monthly Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Intent iYearly = new Intent(getApplicationContext(), YearlyTaskChartActivity.class);
                        startActivity(iYearly);
                        finish();
                        break;
                    case 2:
                        Intent iToady = new Intent(getApplicationContext(), InsightsChart.class);
                        startActivity(iToady);
                        finish();
                        break;
                    case 3:
                        Intent iWeekly = new Intent(getApplicationContext(), WeekelyTaskChartActivity.class);
                        startActivity(iWeekly);
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
        apiCallMonthlyInsights();

    }

    private void apiCallMonthlyInsights() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<ResponseBody> insightMonthlyDataCall = ANApplications.getANApi().taskInsightsMonthly(id);
        insightMonthlyDataCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("SeverReponse" + response.raw());
                if (response.isSuccessful()) {
                    System.out.println("SeverReponse1" + response.raw());
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getString("success").equals("true")) {
                                System.out.println("SeverReponse2" + response.body().toString());
                                String no_of_tasks = jsonObject.getString("no_of_tasks");
                                String no_of_ctasks = jsonObject.getString("no_of_ctasks");
                                System.out.println("no_of_tasks" + no_of_tasks);
                                System.out.println("no_of_ctasks" + no_of_ctasks);
                                mMonthly_no_of_tasks.setText(no_of_tasks);
                                mMonthly_no_of_ctasks.setText(no_of_ctasks);
                                mMonthly_no_of_tasks_cricle.setText(no_of_tasks);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                setLineData(jsonArray);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void setLineData(JSONArray jsonArray) {
        for (int i = 0; jsonArray.length() > i; i++) {
            MontnlyInsighsRepnseData montnlyInsighsRepnseData = new MontnlyInsighsRepnseData();
            try {
                JSONObject jsonObjectValues = jsonArray.getJSONObject(i);
                String day = jsonObjectValues.getString("day");
                String ctaks = jsonObjectValues.getString("ctasks");
                montnlyInsighsRepnseData.setDay(day);
                montnlyInsighsRepnseData.setCtasks(ctaks);
                y.add(new Entry(Integer.parseInt(day), i));
                x.add(ctaks);
                System.out.println("SeverReponse4" + day);
                System.out.println("SeverReponse5" + ctaks);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            LineDataSet set1 = new LineDataSet(y, "NAV Data Value");
            set1.setFillAlpha(110);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            LineData data = new LineData(x, set1);
            XAxis xAxis = mlineChartMonthly.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            mlineChartMonthly.setData(data);
            mlineChartMonthly.invalidate();
            montnlyInsighsRepnseDataArrayList.add(montnlyInsighsRepnseData);

        }

    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            // or highlightTouch(null) for callback to onNothingSelected(...)
            mlineChartMonthly.highlightValues(null);

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
        Log.i("LOWHIGH", "low: " + mlineChartMonthly.getLowestVisibleXIndex()
                + ", high: " + mlineChartMonthly.getHighestVisibleXIndex());

        Log.i("MIN MAX", "xmin: " + mlineChartMonthly.getXChartMin()
                + ", xmax: " + mlineChartMonthly.getXChartMax()
                + ", ymin: " + mlineChartMonthly.getYChartMin()
                + ", ymax: " + mlineChartMonthly.getYChartMax());

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
     /*   ImageView imgProject = (ImageView) findViewById(R.id.img_individuals);
        imgProject.setImageResource(R.drawable.ic_individuals_red);
        TextView txtIndividual = (TextView) findViewById(R.id.txt_individuals);
        txtIndividual.setTextColor(getResources().getColor(R.colorWhite.colorAccent));*/
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
    /*    Intent i = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
        startActivity(i);*/
        Toast.makeText(getApplicationContext(), "selected Insights", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

