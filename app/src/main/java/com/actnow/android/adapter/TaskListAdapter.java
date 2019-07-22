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

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private List<TaskListRecords>  taskListRecordsList;
    public TaskListAdapter(ArrayList<TaskListRecords> taskListResponseArrayList, int custom_task_list, Context applicationContext) {
        this.taskListRecordsList = taskListResponseArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_task_list,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TaskListRecords taskListResponse = taskListRecordsList.get(i);
        viewHolder.mRadioButtonTaskName.setText(taskListResponse.getName());
        viewHolder.mDudate.setText(taskListResponse.getDue_date());
        viewHolder.mTaskRemider.setText(taskListResponse.getRemindars_count());
        System.out.println("name"+ taskListRecordsList);
    }
    @Override
    public int getItemCount() {
        return taskListRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton mRadioButtonTaskName;
        TextView mDudate;
        TextView mTaskRemider;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRadioButtonTaskName = itemView.findViewById(R.id.rb_taskName);
            mDudate = itemView.findViewById(R.id.tv_TasktDate);
            mTaskRemider = itemView.findViewById(R.id.tv_TaskRaminder);
        }
    }
}
