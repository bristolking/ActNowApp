package com.actnow.android.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;

import java.util.ArrayList;
import java.util.List;

public class NewTaskProjectAdapter extends RecyclerView.Adapter<NewTaskProjectAdapter.MyViewHolder> {
    private List<ProjectListResponseRecords> projectListResponseRecordsList;
    Context context;

    public NewTaskProjectAdapter(ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList, int custom_project_dailog, Context applicationContext) {
       this.projectListResponseRecordsList = projectListResponseRecordsArrayList;
    }

    public NewTaskProjectAdapter(Context applicationContext, ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList) {
        this.projectListResponseRecordsList = projectListResponseRecordsArrayList;
        this.context = applicationContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.custom_project_dailog, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ProjectListResponseRecords projectListResponseRecords = projectListResponseRecordsList.get(i);
        myViewHolder.mProjectName.setText(projectListResponseRecords.getName());
        myViewHolder.mProjectCode.setText(projectListResponseRecords.getProject_code());
    }

    @Override
    public int getItemCount() {
        return projectListResponseRecordsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mProjectName,mProjectCode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mProjectName = (TextView) itemView.findViewById(R.id.tv_projectNameDailog);
            mProjectCode =(TextView)itemView.findViewById(R.id.tv_projectCodeDailog);
        }
    }
}
