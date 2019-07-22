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
import com.actnow.android.sdk.responses.PriorityTaskListRecords;

import java.util.List;

public class PriorityTaskAdapter extends RecyclerView.Adapter<PriorityTaskAdapter.ViewHolder> {
    private List<PriorityTaskListRecords> priorityTaskListRecordsList;
    String valid_until = "01/05/2019";
    int  catalog_outdated = 1;



    public PriorityTaskAdapter(List<PriorityTaskListRecords> priorityTaskListRecords, int over_due_task, Context applicationContext) {
        this.priorityTaskListRecordsList = priorityTaskListRecords;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_priority_task,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PriorityTaskListRecords priorityTaskListRecords = priorityTaskListRecordsList.get(i);
        /*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(valid_until);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(strDate)) {
           catalog_outdated = 1;
        }*/
        viewHolder.mOverTaskProject.setText(priorityTaskListRecords.getPriority());
        viewHolder.mOverDueTaskDate.setText(priorityTaskListRecords.getDue_date());
        viewHolder.mOverDueTaskName.setText(priorityTaskListRecords.getName());
    }

    @Override
    public int getItemCount() {
        return priorityTaskListRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton mOverDueTaskName;
        TextView mOverDueTaskDate,mOverTaskProject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mOverDueTaskDate=(TextView)itemView.findViewById(R.id.tv_overDueTaskDate);
            mOverTaskProject=(TextView)itemView.findViewById(R.id.tv_timgs);
            mOverDueTaskName=(RadioButton)itemView.findViewById(R.id.overDueTaskName);
        }
    }
}
