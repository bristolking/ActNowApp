package com.actnow.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.ReminderModel;

import java.util.ArrayList;
import java.util.List;


public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {
    private List<ReminderModel> reminderModelList;

    public ReminderListAdapter(ArrayList<ReminderModel> reminderModelArrayList, int remainder_list, Context applicationContext) {
        this.reminderModelList = reminderModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.remainder_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ReminderModel model= reminderModelList.get(i);
        viewHolder.mDateReminder.setText(model.getDate());
        viewHolder.mTimeReminder.setText(model.getTime());
        viewHolder.mNameReminder.setText(model.getName());

    }

    @Override
    public int getItemCount() {
        return  reminderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDateReminder,mTimeReminder,mNameReminder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDateReminder = (TextView)itemView.findViewById(R.id.tv_dateRemainder);
            mTimeReminder =(TextView)itemView.findViewById(R.id.tv_timeRemainder);
            mNameReminder =(TextView)itemView.findViewById(R.id.tv_userNameRaminder);
        }
    }
}