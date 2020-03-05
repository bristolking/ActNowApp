package com.actnow.android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.MontnlyInsighsRepnseData;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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


public class MonthlyInsightsFragment extends Fragment {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    LineChart mlineChartMonthly;

    TextView mMonthly_no_of_tasks, mMonthly_no_of_tasks_cricle;
    TextView mMonthly_no_of_ctasks;
    TextView mMonthlyDayTask;
    TextView mCompltedMonthlyTask;

    ArrayList<Entry> y;
    ArrayList<String> x;
    ArrayList<MontnlyInsighsRepnseData> montnlyInsighsRepnseDataArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils(getContext());
        View view= inflater.inflate(R.layout.fragment_monthly_insights, container, false);
        y = new ArrayList<Entry>();
        x = new ArrayList<String>();
        mProgressView = view.findViewById( R.id.progress_bar );
        mContentLayout = view.findViewById( R.id.content_layout );
        mlineChartMonthly = (LineChart)view.findViewById(R.id.line_chartGraphMonthly);
        mMonthly_no_of_tasks = (TextView)view.findViewById(R.id.tv_numberTotalTaks);
        mMonthly_no_of_ctasks = (TextView)view.findViewById(R.id.tv_numberOfComleteTaks);
        mMonthly_no_of_tasks_cricle = (TextView)view.findViewById(R.id.tv_totalTaksInCricle);
        mMonthlyDayTask = (TextView)view.findViewById(R.id.monthlyData);
        mCompltedMonthlyTask = (TextView)view.findViewById(R.id.monthlyComlData);
        apiCallMonthlyInsights();
        return view;
    }
    private void apiCallMonthlyInsights() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<ResponseBody> insightMonthlyDataCall = ANApplications.getANApi().taskInsightsMonthly(id);
        insightMonthlyDataCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
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
}
