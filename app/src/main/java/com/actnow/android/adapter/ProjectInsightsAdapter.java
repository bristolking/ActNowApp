package com.actnow.android.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.actnow.android.R;
import com.actnow.android.sdk.responses.ProjectsInsights;

import java.util.ArrayList;
import java.util.List;

public class ProjectInsightsAdapter extends RecyclerView.Adapter<ProjectInsightsAdapter.ViewHolder> {
    private Context context;

    List<ProjectsInsights> projectsInsights;

    public ProjectInsightsAdapter( ArrayList<ProjectsInsights> projectsInsightsArrayList) {
        this.projectsInsights = projectsInsightsArrayList;
    }



    @NonNull
    @Override
    public ProjectInsightsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.custom_project_insights,viewGroup,false );
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProjectInsightsAdapter.ViewHolder viewHolder, int i) {
        ProjectsInsights projectsInsights1 = projectsInsights.get(i);
        viewHolder.mInsightsProjectName.setText( projectsInsights1.getName());
        viewHolder.mProjectsInsightsApproval.setText( projectsInsights1.getApproval());
        viewHolder.mProjectInsightscompleted.setText(projectsInsights1.getCompleted());
        viewHolder.mProjectsInsightsOngoing.setText(projectsInsights1.getOngoing());
        viewHolder.mProjectsInsightsPeniding.setText(projectsInsights1.getPending());
        viewHolder.mProjectInsightsColor.setText(projectsInsights1.getColor());

    }

    @Override
    public int getItemCount() {
        return projectsInsights.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mInsightsProjectName,mProjectInsightsColor,mProjectsInsightsApproval,mProjectsInsightsPeniding,mProjectsInsightsOngoing,mProjectInsightscompleted,mProjectInsightsDueDate;

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
