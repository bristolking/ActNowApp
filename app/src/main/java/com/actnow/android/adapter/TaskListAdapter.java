package com.actnow.android.adapter;

import android.content.ClipData;
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
    public TaskListAdapter(ArrayList<TaskListRecords> taskListResponseArrayList, int task_list_cutsom, Context applicationContext) {
        this.taskListRecordsList = taskListResponseArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_list_cutsom,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TaskListRecords taskListResponse = taskListRecordsList.get(i);
        viewHolder.mTaskListTaskName.setText(taskListResponse.getName());
        viewHolder.mDudate.setText(taskListResponse.getDue_date());
        viewHolder.mTaskProjectName.setText(taskListResponse.getProject_code());
        viewHolder.mRemdnier.setText(taskListResponse.getRemindars_count());
        viewHolder.mTaskPriorty.setText(taskListResponse.getPriority());
        System.out.println("name"+ taskListRecordsList);
    }
    @Override
    public int getItemCount() {
        return taskListRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       //RadioButton mRadioButtonTaskName;
        TextView mTaskListTaskName;
        TextView mDudate;
        TextView mTaskProjectName,mRemdnier;
        TextView mTaskPriorty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskListTaskName = itemView.findViewById(R.id.tv_taskListName);
            mDudate = itemView.findViewById(R.id.tv_taskListDate);
            mTaskProjectName = itemView.findViewById(R.id.tv_projectNameTaskList);
            mRemdnier = itemView.findViewById(R.id.tv_taskRaminder);
            mTaskPriorty = itemView.findViewById(R.id.tv_taskListPriority);
        }
    }
    public void removeItem(int position) {
        taskListRecordsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem( TaskListRecords taskListRecords, int position) {
        taskListRecordsList.add(position, taskListRecords);
        // notify item added by position
        notifyItemInserted(position);
    }
}
