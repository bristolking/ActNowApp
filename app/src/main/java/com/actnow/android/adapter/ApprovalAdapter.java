package com.actnow.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.ApprovalResponse;

import java.util.ArrayList;
import java.util.List;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.ViewHolder> {
    private List<ApprovalResponse> approvalResponseList;

    public ApprovalAdapter(ArrayList<ApprovalResponse> approvalResponseArrayList, int custom_approval_tasklist, Context applicationContext) {
        this.approvalResponseList = approvalResponseArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_approval_tasklist,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
         ApprovalResponse approvalResponse = approvalResponseList.get(i);
         viewHolder.mApprovalTaskName.setText(approvalResponse.getName());
         viewHolder.mApprovalDate.setText(approvalResponse.getDate());
         viewHolder.mApprovalTaskPriority.setText(approvalResponse.getPriority());

    }

    @Override
    public int getItemCount() {
        return approvalResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mApprovalDate,mApprovalTaskName,mApprovalTaskPriority;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mApprovalTaskName =(TextView)itemView.findViewById(R.id.approvalTaskName);
            mApprovalDate =(TextView)itemView.findViewById(R.id.approvalTaskDate);
            mApprovalTaskPriority =(TextView)itemView.findViewById(R.id.tv_approvalTaskPriority);

        }
    }
}
