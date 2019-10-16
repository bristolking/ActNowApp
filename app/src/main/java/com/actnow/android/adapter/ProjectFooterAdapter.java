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
import com.actnow.android.sdk.responses.ProjectListResponseRecords;

import java.util.ArrayList;
import java.util.List;

public class ProjectFooterAdapter extends RecyclerView.Adapter<ProjectFooterAdapter.MyViewHolder> {
    private List<ProjectListResponseRecords> projectListResponseRecordsList;

    public ProjectFooterAdapter(ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList, int custom_project_footer, Context applicationContext) {
        this.projectListResponseRecordsList = projectListResponseRecordsArrayList;
    }

  /*  public ProjectFooterAdapter(ProjectFooterActivity projectFooterActivity, ArrayList<ProjectListResponseRecords> projectListResponseRecordsList) {
        this.projectListResponseRecordsList = projectListResponseRecordsList;
    }*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_project_footer, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ProjectListResponseRecords projectListResponseRecords = projectListResponseRecordsList.get(i);
        myViewHolder.mProjectNameFooter.setText(projectListResponseRecords.getName());
        myViewHolder.mProjectCode.setText(projectListResponseRecords.getProject_code());
        myViewHolder.mProjectId.setText(projectListResponseRecords.getProject_id());
    }

    @Override
    public int getItemCount() {
        return projectListResponseRecordsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton mProjectNameFooter;
        TextView mProjectCode;
        TextView mProjectId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mProjectNameFooter = (RadioButton) itemView.findViewById(R.id.projectNameFooter);
            mProjectCode = (TextView) itemView.findViewById(R.id.tv_projectCode);
            mProjectId =(TextView)itemView.findViewById(R.id.tv_projectId);
        }
    }
}
