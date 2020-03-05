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
import com.actnow.android.sdk.responses.TaskInsightDataYear;
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


public class YearlyInsightsFragment extends Fragment {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    LineChart mlineChartYearly;
    TextView mYearly_no_of_tasks,mYearly_no_of_tasks_cricle;
    TextView mYearly_no_of_ctasks;
    ArrayList<Entry> x;
    ArrayList<String> y;
    ArrayList<TaskInsightDataYear> taskInsightDataYearArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils( getContext() );
        View view =  inflater.inflate(R.layout.fragment_yearly_insights, container, false);
        x = new ArrayList<Entry>();
        y = new ArrayList<String>();
        mProgressView = view.findViewById( R.id.progress_bar );
        mContentLayout = view.findViewById( R.id.content_layout );
        mlineChartYearly = (LineChart)view.findViewById(R.id.line_chartGraphYearly);
        mYearly_no_of_tasks  = (TextView)view.findViewById(R.id.tv_numberTotalTaks);
        mYearly_no_of_ctasks = (TextView)view.findViewById(R.id.tv_numberOfComleteTaks);
        mYearly_no_of_tasks_cricle =(TextView)view.findViewById(R.id.tv_totalTaksInCricle);
        apiCallYearlyInsights();

        return view;
    }
    private void apiCallYearlyInsights() {
        HashMap<String,String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        Call<ResponseBody> responseBodyCall = ANApplications.getANApi().taskInsightsYearly(id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                System.out.println( "SeverReponse" + response.raw() );
                if (response.isSuccessful()) {
                    System.out.println( "SeverReponse1" + response.raw() );
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject( response.body().string() );
                            if (jsonObject.getString( "success" ).equals( "true" )) {
                                System.out.println( "SeverReponse2" + response.body().toString());
                                String no_of_tasks =  jsonObject.getString("no_of_tasks");
                                String  no_of_ctasks = jsonObject.getString("no_of_ctasks");
                                System.out.println("no_of_tasks" + no_of_tasks);
                                System.out.println("no_of_ctasks" + no_of_ctasks);
                                mYearly_no_of_tasks.setText(no_of_tasks);
                                mYearly_no_of_ctasks.setText(no_of_ctasks);
                                mYearly_no_of_tasks_cricle.setText(no_of_tasks);
                                JSONArray jsonArray = jsonObject.getJSONArray( "data" );
                                System.out.println( "SeverReponse3" + jsonArray);
                                setDataYearly(jsonArray);
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

    private void setDataYearly(JSONArray jsonArray) {
        for (int i=0; jsonArray.length() > i; i++) {
            TaskInsightDataYear taskInsightDataYear = new TaskInsightDataYear();
            try {
                JSONObject jsonObject =  jsonArray.getJSONObject(i);
                String month = jsonObject.getString("month");
                String month_name = jsonObject.getString("month_name");
                String ctasks = jsonObject.getString("ctasks");
                x.add(new Entry(Integer.parseInt(month),i));
                y.add(month_name);
                System.out.println("SeverReponse4" + month_name);
                System.out.println("SeverReponse5" + ctasks);
                taskInsightDataYear.setMonth(month);
                taskInsightDataYear.setMonth_name(month_name);
                taskInsightDataYear.setCtasks(ctasks);
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
            XAxis xAxis = mlineChartYearly.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            mlineChartYearly.setData(data);
            mlineChartYearly.invalidate();
            taskInsightDataYearArrayList.add(taskInsightDataYear);
        }
    }
}
