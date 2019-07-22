package com.actnow.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.TaskListRecords;


import java.util.ArrayList;

import java.util.List;


public class ThisWeekAdapter extends RecyclerView.Adapter<ThisWeekAdapter.ViewHolder> {
    private List<TaskListRecords> taskListRecordsList;
    private String currentDate;

    public ThisWeekAdapter(ArrayList<TaskListRecords> taskListRecordsArrayList, int custom_task_list, Context applicationContext) {
        this.taskListRecordsList = taskListRecordsArrayList;
    }

    @NonNull
    @Override
    public ThisWeekAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_this_week, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThisWeekAdapter.ViewHolder viewHolder, int i) {
        TaskListRecords taskListResponse = taskListRecordsList.get(i);
        //viewHolder.mTilte.setText(taskListResponse.getTask_id());
        viewHolder.mRadiobuttonTaskName.setText(taskListResponse.getName());
        viewHolder.mDateThisWeek.setText(taskListResponse.getDue_date());

    }

    @Override
    public int getItemCount() {
        return taskListRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTilte;
        RadioButton mRadiobuttonTaskName;
        TextView mDateThisWeek;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //mTilte = (TextView) itemView.findViewById(R.id.tv_titleThisweek);
            mRadiobuttonTaskName = (RadioButton) itemView.findViewById(R.id.thisweekTaskName);
            mDateThisWeek = (TextView) itemView.findViewById(R.id.tv_dateTaskThisweek);

    /*        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-EEE, MM, yyyy");
            String stringDate2 = sdf2.format(new Date());
            System.out.println("Today is : " + stringDate2);
            mTilte.setText(stringDate2);*/
            /*Calendar now = Calendar.getInstance();
            mDateThisWeek.setText((now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
           // System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

            String[] strDays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thusday",
                    "Friday", "Saturday" };
            // Day_OF_WEEK starts from 1 while array index starts from 0
            //System.out.println("Current day is : " + strDays[now.get(Calendar.DAY_OF_WEEK) - 1]);
            mTilte.setText(strDays[now.get(Calendar.DAY_OF_WEEK) - 1]);
*/

        }
    }
}
