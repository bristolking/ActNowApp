package com.actnow.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.OverDueTaskRecords;
import com.actnow.android.sdk.responses.TaskListRecords;

import java.util.ArrayList;
import java.util.List;

public class OverDueTaskAdapter extends RecyclerView.Adapter<OverDueTaskAdapter.ViewHolder> {
    private List<TaskListRecords> taskListRecordsList;

    public OverDueTaskAdapter(ArrayList<TaskListRecords> taskListRecordsArrayList, int task_list_cutsom, Context applicationContext) {
        this.taskListRecordsList =  taskListRecordsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_list_cutsom,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TaskListRecords taskListRecords = taskListRecordsList.get(i);
        viewHolder.mOverDueTaskListTaskName.setText(taskListRecords.getName());
        viewHolder.mOverDueDudate.setText(taskListRecords.getDue_date());
        viewHolder.mOverDueTaskProjectName.setText(taskListRecords.getProject_name());
        viewHolder.mOverDueRemdnier.setText(taskListRecords.getRemindars_count());
        viewHolder.mOverDueTaskPriorty.setText(taskListRecords.getPriority());
        viewHolder.mOverDueTaskCode.setText(taskListRecords.getTask_code());
        viewHolder.mOverDueTaskStatus.setText(taskListRecords.getStatus());
        viewHolder.mOverDueProjectCode.setText(taskListRecords.getProject_code());
        viewHolder.mOverDueRepeat_type.setText(taskListRecords.getRepeat_type());

    }
    @Override
    public int getItemCount() {
        return taskListRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mOverDueTaskListTaskName;
        TextView mOverDueDudate;
        TextView mOverDueTaskProjectName,mOverDueRemdnier;
        TextView mOverDueTaskPriorty;
        TextView mOverDueTaskCode;
        TextView mOverDueTaskStatus;
        TextView mOverDueProjectCode;
        TextView mOverDueRepeat_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mOverDueTaskListTaskName = itemView.findViewById(R.id.tv_taskListName);
            mOverDueDudate = itemView.findViewById(R.id.tv_taskListDate);
            mOverDueTaskProjectName = itemView.findViewById(R.id.tv_projectNameTaskList);
            mOverDueRemdnier = itemView.findViewById(R.id.tv_taskRaminder);
            mOverDueTaskPriorty = itemView.findViewById(R.id.tv_taskListPriority);
            mOverDueTaskCode = itemView.findViewById(R.id.tv_taskCode);
            mOverDueTaskStatus = itemView.findViewById(R.id.tv_taskstatus);
            mOverDueProjectCode = itemView.findViewById(R.id.tv_projectCodeTaskList);
            mOverDueRepeat_type = itemView.findViewById(R.id.tv_taskRepeatType);


        }
    }
}
