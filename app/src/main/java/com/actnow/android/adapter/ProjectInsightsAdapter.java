package com.actnow.android.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.actnow.android.R;
import com.actnow.android.sdk.responses.ProjectInsightsRecords;

import java.util.ArrayList;
import java.util.List;

public class ProjectInsightsAdapter extends RecyclerView.Adapter<ProjectInsightsAdapter.ViewHolder> {
    List<ProjectInsightsRecords> projectInsightsRecordsList;
    private Context context;

    public ProjectInsightsAdapter(ArrayList<ProjectInsightsRecords> projectInsightsRecordsArrayList, int custom_project_insights, Context applicationContext) {
        this.projectInsightsRecordsList = projectInsightsRecordsArrayList;

    }


    @NonNull
    @Override
    public ProjectInsightsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.custom_project_insights,viewGroup,false );
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProjectInsightsAdapter.ViewHolder viewHolder, int i) {
        ProjectInsightsRecords projectInsightsRecords = projectInsightsRecordsList.get(i);
        viewHolder.mInsightsProjectName.setText( projectInsightsRecords.getName());
        viewHolder.mProjectsInsightsApproval.setText( projectInsightsRecords.getApproval());
        viewHolder.mProjectInsightscompleted.setText(projectInsightsRecords.getCompleted());
        viewHolder.mProjectsInsightsOngoing.setText(projectInsightsRecords.getOngoing());
        viewHolder.mProjectsInsightsPeniding.setText(projectInsightsRecords.getPending());
        viewHolder.mProjectInsightsColor.setText(projectInsightsRecords.getColor());

    }

    @Override
    public int getItemCount() {
        return  projectInsightsRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mInsightsProjectName,mProjectInsightsColor,mProjectsInsightsApproval,mProjectsInsightsPeniding,mProjectsInsightsOngoing,mProjectInsightscompleted;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mInsightsProjectName = itemView.findViewById(R.id.tv_projects_InsgihtsName);
            mProjectInsightsColor = itemView.findViewById(R.id.tv_projects_InsgihtsColor);
            mProjectsInsightsApproval = itemView.findViewById(R.id.tv_projectInsightsApprovalTasks);
            mProjectsInsightsPeniding = itemView.findViewById(R.id.tv_projectInsightsPendingTasks);
            mProjectsInsightsOngoing = itemView.findViewById(R.id.tv_projectInsightsOngoingTasks);
            mProjectInsightscompleted = itemView.findViewById(R.id.tv_projectInsightsCompleteTasks);

        }
    }
}
