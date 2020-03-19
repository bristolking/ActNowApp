package com.actnow.android.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.TaskListRecords;

import java.util.ArrayList;
import java.util.List;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.ViewHolder> {
    private List<TaskListRecords> mTaskListRecords;



    public ApprovalAdapter(ArrayList<TaskListRecords> taskListRecordsArrayList) {
        this.mTaskListRecords = taskListRecordsArrayList;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.custom_approval_tasklist, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TaskListRecords taskListRecords = mTaskListRecords.get( i );
        viewHolder.mApprovalTaskName.setText( taskListRecords.getName() );
        viewHolder.mApprovalDate.setText( taskListRecords.getDue_date() );
        viewHolder.mApprovalTaskPriority.setText( taskListRecords.getPriority() );
        viewHolder.mTaskCode.setText( taskListRecords.getTask_code() );
        viewHolder.mProjectNameApproval.setText(taskListRecords.getProject_name());
    }

    @Override
    public int getItemCount() {
        return mTaskListRecords.size();
    }

    public void filterList(ArrayList<TaskListRecords> taskListRecordsFilter) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout viewBackground,viewForeground,viewBackgroundRight;
        TextView mApprovalDate, mApprovalTaskName, mApprovalTaskPriority, mTaskCode,mProjectNameApproval;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mApprovalTaskName = (TextView) itemView.findViewById( R.id.approvalTaskName );
            mApprovalDate = (TextView) itemView.findViewById( R.id.approvalTaskDate );
            mApprovalTaskPriority = (TextView) itemView.findViewById( R.id.tv_approvalTaskPriority );
            mTaskCode = (TextView) itemView.findViewById( R.id.tv_taskCodeApproval );
            mProjectNameApproval =(TextView)itemView.findViewById(R.id.projectName_approval);
           /* viewBackground = itemView.findViewById( R.id.view_background );
            viewForeground = itemView.findViewById( R.id.view_foreground );*/
       /*     viewBackgroundRight = itemView.findViewById(R.id.view_background_left );*/

        }
    }


    public void addItem(TaskListRecords taskListRecords) {
        mTaskListRecords.add(taskListRecords);
        notifyItemInserted(mTaskListRecords.size());
    }

    public void removeItem(int position) {
        mTaskListRecords.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mTaskListRecords.size());
    }

    /*public void removeItem(int position) {
        mTaskListRecords.remove( position );

        notifyItemRemoved( position );
    }
    public void restoreItem(TaskListRecords item, int position) {
        mTaskListRecords.add( position, item );
        notifyItemInserted( position );
    }*/
}
