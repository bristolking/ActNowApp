package com.actnow.android.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;

import java.util.ArrayList;
import java.util.List;

public class ProjectOfflineAdapter extends RecyclerView.Adapter<ProjectOfflineAdapter.ViewHolder> {
    private List<ProjectListResponseRecords> projectListResponseRecordsList;

    public ProjectOfflineAdapter(ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList) {
        this.projectListResponseRecordsList = projectListResponseRecordsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_project_footer, viewGroup, false);
        return new ProjectOfflineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ProjectListResponseRecords projectListResponseRecords = projectListResponseRecordsList.get(i);
        viewHolder.mProjectNameFooter.setText(projectListResponseRecords.getName());
        viewHolder.mProjectCode.setText(projectListResponseRecords.getProject_code());
        viewHolder.mProjectId.setText(projectListResponseRecords.getProject_id());
        viewHolder.mProjectColor.setText( projectListResponseRecords.getColor());
    }

    @Override
    public int getItemCount() {
        return projectListResponseRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton mProjectNameFooter;
        TextView mProjectCode;
        TextView mProjectId,mProjectColor,mProjectDueDate;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mProjectNameFooter = (RadioButton) itemView.findViewById( R.id.projectNameFooter);
            mProjectCode = (TextView) itemView.findViewById(R.id.tv_projectCode);
            mProjectId =(TextView)itemView.findViewById(R.id.tv_projectId);
            mProjectColor =(TextView)itemView.findViewById(R.id.tv_projectColor);
            ImageView mImageUserAddProject = (ImageView) itemView.findViewById( R.id.img_useraddProjectList );
            mImageUserAddProject.setVisibility( View.GONE );

        }
    }
}
