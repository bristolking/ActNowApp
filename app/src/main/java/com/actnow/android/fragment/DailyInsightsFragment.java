package com.actnow.android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.TaskDailyInsightsData;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;

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

import static com.facebook.FacebookSdk.getApplicationContext;


public class DailyInsightsFragment extends Fragment {
    ArrayList<Entry> x;
    ArrayList<String> y;
    UserPrefUtils session;
    LineChart mlineChart;
    View mProgressView, mContentLayout;

    TextView mDaily_no_of_tasks, mDaily_no_of_tasks_cricle;
    TextView mDaily_no_of_ctasks;
    ArrayList<TaskDailyInsightsData> taskDailyInsightsDataArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_insights, container, false);
        session = new UserPrefUtils(getApplicationContext());

        mProgressView = view.findViewById( R.id.progress_bar );
        mContentLayout = view.findViewById( R.id.content_layout );
        x = new ArrayList<Entry>();
        y = new ArrayList<String>();
        mlineChart = (LineChart) view.findViewById(R.id.line_chartGraph);
        mDaily_no_of_tasks = (TextView) view.findViewById(R.id.tv_numberTotalTaks);
        mDaily_no_of_ctasks = (TextView) view.findViewById(R.id.tv_numberOfComleteTaks);
        mDaily_no_of_tasks_cricle = (TextView) view.findViewById(R.id.tv_totalTaksInCricle);
        apiCallDailyInsights();
        return view;
    }

    private void apiCallDailyInsights() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);

        Call<ResponseBody> responseBodyCall = ANApplications.getANApi().taskInsightsDaily(id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getString("success").equals("true")) {
                                String no_of_tasks = jsonObject.getString("no_of_tasks");
                                String no_of_ctasks = jsonObject.getString("no_of_ctasks");
                                System.out.println("no_of_tasks" + no_of_tasks);
                                System.out.println("no_of_ctasks" + no_of_ctasks);
                                mDaily_no_of_tasks.setText(no_of_tasks);
                                mDaily_no_of_ctasks.setText(no_of_ctasks);
                                mDaily_no_of_tasks_cricle.setText(no_of_tasks);

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                System.out.println("SeverReponse3" + jsonArray);
                                setDailyInsightsData(jsonArray);

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

    private void setDailyInsightsData(JSONArray jsonArray) {
        for (int i = 0; jsonArray.length() > i; i++) {
            TaskDailyInsightsData taskDailyInsightsData = new TaskDailyInsightsData();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String time = jsonObject.getString("time");
                System.out.println("time" + time);
                String[] date = time.split("-");
                System.out.println("date" + date);
                String ctasks = jsonObject.getString("ctasks");
                x.add(new Entry(Integer.parseInt(ctasks), i));
                y.add(String.valueOf(date));
                taskDailyInsightsData.setTime(time);
                taskDailyInsightsData.setCtasks(ctasks);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            LineDataSet set1 = new LineDataSet(x, "NAV Data Value");
            set1.setFillAlpha(110);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            LineData data = new LineData(y, set1);
            XAxis xAxis = mlineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            mlineChart.setData(data);
            mlineChart.invalidate();
            taskDailyInsightsDataArrayList.add(taskDailyInsightsData);
        }

    }
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            // or highlightTouch(null) for callback to onNothingSelected(...)
            mlineChart.highlightValues(null);

    }

    public void onChartLongPressed(MotionEvent me) {

    }


    public void onChartDoubleTapped(MotionEvent me) {

    }


    public void onChartSingleTapped(MotionEvent me) {

    }
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);


    }
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mlineChart.getLowestVisibleXIndex()
                + ", high: " + mlineChart.getHighestVisibleXIndex());

        Log.i("MIN MAX", "xmin: " + mlineChart.getXChartMin()
                + ", xmax: " + mlineChart.getXChartMax()
                + ", ymin: " + mlineChart.getYChartMin()
                + ", ymax: " + mlineChart.getYChartMax());

    }

    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");

    }


}





