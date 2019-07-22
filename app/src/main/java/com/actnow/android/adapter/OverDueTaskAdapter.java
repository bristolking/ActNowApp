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
import com.actnow.android.sdk.responses.OverDueTaskRecords;

import java.util.ArrayList;
import java.util.List;

public class OverDueTaskAdapter extends RecyclerView.Adapter<OverDueTaskAdapter.ViewHolder> {
    private List<OverDueTaskRecords> overDueTaskRecordsList;

    public OverDueTaskAdapter(ArrayList<OverDueTaskRecords> overDueTaskRecordsArrayList, int custom_over_due, Context applicationContext) {
        this.overDueTaskRecordsList = overDueTaskRecordsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_over_due,viewGroup,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        OverDueTaskRecords overDueTaskRecords = overDueTaskRecordsList.get(i);
        viewHolder.mRadioButtonName.setText(overDueTaskRecords.getName());
        viewHolder.mCreateDate.setText(overDueTaskRecords.getCreated_date());
        viewHolder.mEndDate.setText(overDueTaskRecords.getDue_date());
    }
    @Override
    public int getItemCount() {
        return overDueTaskRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton mRadioButtonName;
        TextView mCreateDate,mEndDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRadioButtonName =itemView.findViewById(R.id.priorityTaskButton);
            mCreateDate = itemView.findViewById(R.id.tv_crateDate);
            mEndDate = itemView.findViewById(R.id.tv_endDate);

        }
    }
}
