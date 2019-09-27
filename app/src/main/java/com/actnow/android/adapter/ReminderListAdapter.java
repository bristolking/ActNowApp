package com.actnow.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.ReminderTaskReinders;

import java.util.ArrayList;
import java.util.List;


public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {
    private List<ReminderTaskReinders> reminderTaskReindersList;

    public ReminderListAdapter(ArrayList<ReminderTaskReinders> reminderTaskReindersArrayList, int reminder_time_date, Context applicationContext) {
        this.reminderTaskReindersList = reminderTaskReindersArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_time_date,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ReminderTaskReinders reminderTaskReinders= reminderTaskReindersList.get(i);
        viewHolder.mDateReminder.setText( reminderTaskReinders.getReminder_date());
        viewHolder.mUserTaskCodeReminder.setText(reminderTaskReinders.getTask_code());
        viewHolder.mReminderTaskId.setText( reminderTaskReinders.getReminder_task_id());

    }

    @Override
    public int getItemCount() {
        return  reminderTaskReindersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDateReminder,mUserTaskCodeReminder,mReminderTaskId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDateReminder = (TextView)itemView.findViewById(R.id.tv_userDateReminder);
            mUserTaskCodeReminder =(TextView)itemView.findViewById(R.id.tv_userTaskCodeReminder);
            mReminderTaskId =(TextView)itemView.findViewById(R.id.tv_riminderTaslkId);
        }
    }
}