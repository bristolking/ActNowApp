package com.actnow.android.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.TaskListRecords;


import java.util.ArrayList;
import java.util.List;


public class ThisWeekAdapter extends RecyclerView.Adapter<ThisWeekAdapter.ViewHolder> {
    private List<TaskListRecords> taskListRecordsList;
    private  Context context;

    public ThisWeekAdapter(ArrayList<TaskListRecords> taskListRecordsArrayList, int custom_task_weekname_list, Context applicationContext) {
      this.taskListRecordsList = taskListRecordsArrayList;
      this.context = applicationContext;
    }

    public ThisWeekAdapter(ArrayList<TaskListRecords> taskListRecordsArrayList) {
        this.taskListRecordsList = taskListRecordsArrayList;
    }


    @NonNull
    @Override
    public ThisWeekAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_task_weekname_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThisWeekAdapter.ViewHolder viewHolder, int i) {
        TaskListRecords taskListResponse = taskListRecordsList.get(i);
        viewHolder.mTaskWeekTaskName.setText(taskListResponse.getName());
        viewHolder.mWeekDudate.setText(taskListResponse.getDue_date());
        viewHolder.mTaskWeekProjectName.setText(taskListResponse.getProject_name());
        viewHolder.mWeekRemdnier.setText(taskListResponse.getRemindars_count());
        viewHolder.mWeekTaskPriorty.setText( taskListResponse.getPriority());
        viewHolder.mWeekTaskCode.setText(taskListResponse.getTask_code());
        viewHolder.mWeekTaskStatus.setText( taskListResponse.getStatus());
        viewHolder.mWeekProjectCode.setText( taskListResponse.getProject_code());
        viewHolder.mWeekRepeat_type.setText(taskListResponse.getRepeat_type());

    }

    @Override
    public int getItemCount() {
        return taskListRecordsList.size();
    }

    public void filterList(ArrayList<TaskListRecords> taskListRecordsFilter) {
        taskListRecordsList = taskListRecordsFilter;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTaskWeekTaskName;
        TextView mWeekDudate;
        TextView mTaskWeekProjectName,mWeekRemdnier;
        TextView mWeekTaskPriorty;
        TextView mWeekTaskCode;
        TextView mWeekTaskStatus;
        TextView mWeekProjectCode;
        TextView mWeekRepeat_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskWeekTaskName = itemView.findViewById(R.id.tv_WeektaskListName);
            mWeekDudate = itemView.findViewById(R.id.tv_weektaskListDate);
            mTaskWeekProjectName = itemView.findViewById(R.id.tv_WeekprojectNameTaskList);
            mWeekRemdnier = itemView.findViewById(R.id.tv_WeektaskRaminder);
            mWeekTaskPriorty = itemView.findViewById(R.id.tv_WeektaskListPriority);
            mWeekTaskCode = itemView.findViewById(R.id.tv_WeektaskCode);
            mWeekTaskStatus = itemView.findViewById(R.id.tv_Weektaskstatus);
            mWeekProjectCode = itemView.findViewById(R.id.tv_WeekprojectCodeTaskList);
            mWeekRepeat_type = itemView.findViewById(R.id.tv_WeektaskRepeatType);

        }
    }

}
