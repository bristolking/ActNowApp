package com.actnow.android.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.TaskWeeknameResponse;

import java.util.ArrayList;
import java.util.List;

public class TaskWeeknameAdapter extends RecyclerView.Adapter<TaskWeeknameAdapter.ViewHolder> {
    private List<TaskWeeknameResponse> taskWeeknameResponseList;

    public TaskWeeknameAdapter(ArrayList<TaskWeeknameResponse> taskWeeknameResponseArrayList, int custom_task_weekname_dailog, Context applicationContext) {
        this.taskWeeknameResponseList = taskWeeknameResponseArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.custom_task_weekname_dailog,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i){
        TaskWeeknameResponse taskWeeknameResponse = taskWeeknameResponseList.get(i);
        viewHolder.mTaskWekID.setText( taskWeeknameResponse.getId());
        viewHolder.mTaskWeekname.setText( taskWeeknameResponse.getName());
    }

    @Override
    public int getItemCount() {
        return taskWeeknameResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTaskWeekname,mTaskWekID;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mTaskWeekname =(TextView)itemView.findViewById( R.id.tv_taskWeeekname);
            mTaskWekID =(TextView)itemView.findViewById(R.id.tv_taskWeeekId);
        }
    }
}
