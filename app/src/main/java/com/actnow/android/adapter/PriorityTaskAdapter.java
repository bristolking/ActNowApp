package com.actnow.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.TaskListRecords;

import java.util.ArrayList;

public class PriorityTaskAdapter extends RecyclerView.Adapter<PriorityTaskAdapter.ViewHolder> {

    SortedList<TaskListRecords> list;

    public PriorityTaskAdapter() {
        list = new SortedList<TaskListRecords>( TaskListRecords.class, new SortedList.Callback<TaskListRecords>() {
            @Override
            public int compare(TaskListRecords taskListRecords, TaskListRecords t21) {
                return taskListRecords.getPriority().compareTo( t21.getPriority());
            }

            @Override
            public void onChanged(int i, int i1) {
                notifyItemRangeChanged( i, i1 );
            }

            @Override
            public boolean areContentsTheSame(TaskListRecords taskListRecords, TaskListRecords t21) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(TaskListRecords taskListRecords, TaskListRecords t21) {
                return false;
            }

            @Override
            public void onInserted(int i, int i1) {
                notifyItemRangeInserted( i, i1 );
            }

            @Override
            public void onRemoved(int i, int i1) {

            }

            @Override
            public void onMoved(int i, int i1) {
                notifyItemRangeRemoved( i, i1 );

            }
        });


    }



    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.task_list_cutsom, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TaskListRecords taskListResponse = list.get( i );
        viewHolder.mTaskListTaskName.setText( taskListResponse.getName() );
        viewHolder.mDudate.setText( taskListResponse.getDue_date() );
        viewHolder.mTaskProjectName.setText( taskListResponse.getProject_name() );
        viewHolder.mRemdnier.setText( taskListResponse.getRemindars_count() );
        viewHolder.mTaskPriorty.setText( taskListResponse.getPriority() );
        viewHolder.mTaskCode.setText( taskListResponse.getTask_code() );
        viewHolder.mTaskStatus.setText( taskListResponse.getStatus() );
        viewHolder.mProjectCode.setText( taskListResponse.getProject_code() );
        viewHolder.mRepeat_type.setText( taskListResponse.getRepeat_type() );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(ArrayList<TaskListRecords> taskListRecordsArrayList) {
        list.beginBatchedUpdates();
        for (int i = 0; i < taskListRecordsArrayList.size(); i++) {
            list.add( taskListRecordsArrayList.get( i ) );
        }
        list.endBatchedUpdates();
    }

    public TaskListRecords get(int i) {
        return list.get( i );
    }


    public void clear(){
        list.beginBatchedUpdates();
        while (list.size() > 0) {
            list.removeItemAt(list.size()-1 );
        }
        list.endBatchedUpdates();
    }

   /* public void filterList(ArrayList<TaskListRecords> taskListRecordsFilter) {
        taskListRecordsList = taskListRecordsFilter;
        notifyDataSetChanged();

    }*/

 /*   public void filterList(ArrayList<TaskListRecords> taskListRecordsFilter) {
        taskListRecordsList = taskListRecordsFilter;
        notifyDataSetChanged();
    }*/
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTaskListTaskName;
        TextView mDudate;
        TextView mTaskProjectName, mRemdnier;
        TextView mTaskPriorty;
        TextView mTaskCode;
        TextView mTaskStatus;
        TextView mProjectCode;
        TextView mRepeat_type;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mTaskListTaskName = itemView.findViewById( R.id.tv_taskListName );
            mDudate = itemView.findViewById( R.id.tv_taskListDate );
            mTaskProjectName = itemView.findViewById( R.id.tv_projectNameTaskList );
            mRemdnier = itemView.findViewById( R.id.tv_taskRaminder );
            mTaskPriorty = itemView.findViewById( R.id.tv_taskListPriority );
            mTaskCode = itemView.findViewById( R.id.tv_taskCode );
            mTaskStatus = itemView.findViewById( R.id.tv_taskstatus );
            mProjectCode = itemView.findViewById( R.id.tv_projectCodeTaskList );
            mRepeat_type = itemView.findViewById( R.id.tv_taskRepeatType );

        }
    }

}
