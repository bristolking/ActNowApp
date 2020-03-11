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
import com.actnow.android.sdk.responses.TaskWeeklyInsightsData;
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


public class WeeklyInsightsFragment extends Fragment {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    LineChart mlineChartWeekely;

    TextView mWeekly_no_of_tasks,mWeekly_no_of_tasks_cricle;
    TextView mWeekly_no_of_ctasks;
    TextView mMonthlyDayTask;
    TextView mCompltedMonthlyTask;
    ArrayList<Entry> x;
    ArrayList<String> y;
    ArrayList<TaskWeeklyInsightsData> taskWeeklyInsightsDataArrayList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils(getContext());
        View view = inflater.inflate(R.layout.fragment_weekly_insights, container, false);
        x = new ArrayList<Entry>();
        y = new ArrayList<String>();
        mProgressView = view.findViewById( R.id.progress_bar );
        mContentLayout = view.findViewById( R.id.content_layout );
        mlineChartWeekely = (LineChart)view.findViewById(R.id.line_chartGraphWeeekely);
        mWeekly_no_of_tasks  = (TextView)view.findViewById(R.id.tv_numberTotalTaks);
        mWeekly_no_of_ctasks = (TextView)view.findViewById(R.id.tv_numberOfComleteTaks);
        mWeekly_no_of_tasks_cricle =(TextView)view.findViewById(R.id.tv_totalTaksInCricle);
        apiCallInsightWeekly();
        return view;
    }
    private void apiCallInsightWeekly() {
        HashMap<String,String > userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<ResponseBody> responseBodyCall = ANApplications.getANApi().taskInsightsWeekly(id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()){
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getString("success").equals("true")){
                                String no_of_tasks =  jsonObject.getString("no_of_tasks");
                                String  no_of_ctasks = jsonObject.getString("no_of_ctasks");
                                System.out.println("no_of_tasks" + no_of_tasks);
                                System.out.println("no_of_ctasks" + no_of_ctasks);
                                mWeekly_no_of_tasks.setText(no_of_tasks);
                                mWeekly_no_of_ctasks.setText(no_of_ctasks);
                                mWeekly_no_of_tasks_cricle.setText(no_of_tasks);

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                System.out.println( "SeverReponse3" + jsonArray);
                                setWeeklyInsightsData(jsonArray);

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void setWeeklyInsightsData(JSONArray jsonArray) {
        for (int i=0;jsonArray.length() >i;i++){
            TaskWeeklyInsightsData taskWeeklyInsightsData = new TaskWeeklyInsightsData();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String day = jsonObject.getString("day");
                String[] date = day.split("-");
                String day_name = jsonObject.getString("day_name");
                String ctasks = jsonObject.getString("ctasks");
                x.add(new Entry(Integer.parseInt(date[2]),i));
                y.add(day_name);
                taskWeeklyInsightsData.setDay(day);
                taskWeeklyInsightsData.setDay_name(day_name);
                taskWeeklyInsightsData.setCtasks(ctasks);
            }catch (JSONException e){
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
            XAxis xAxis = mlineChartWeekely.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            mlineChartWeekely.setData(data);
            mlineChartWeekely.invalidate();
            taskWeeklyInsightsDataArrayList.add(taskWeeklyInsightsData);

        }
    }
}
