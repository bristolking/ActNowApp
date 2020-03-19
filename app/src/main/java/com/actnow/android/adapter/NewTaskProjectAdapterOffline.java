package com.actnow.android.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;

import java.util.ArrayList;
import java.util.List;

public class NewTaskProjectAdapterOffline extends RecyclerView.Adapter<NewTaskProjectAdapterOffline.ViewHolder> {
    private List<ProjectListResponseRecords> projectListResponseRecordsList;
    Context context;

    public NewTaskProjectAdapterOffline(ArrayList<ProjectListResponseRecords> projectListResponseRecordsArrayList, int projectoffline_custom, Context applicationContext) {
        this.projectListResponseRecordsList = projectListResponseRecordsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.projectoffline_custom, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ProjectListResponseRecords projectListResponseRecords = projectListResponseRecordsList.get(i);
        viewHolder.mProjectNameOFfline.setText(projectListResponseRecords.getName());
        viewHolder.mProjectCodeOffflne.setText(projectListResponseRecords.getProject_code());
    }

    @Override
    public int getItemCount() {
        return projectListResponseRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mProjectNameOFfline,mProjectCodeOffflne;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProjectNameOFfline = (TextView) itemView.findViewById(R.id.tv_projectNameDailogOFFline);
            mProjectCodeOffflne =(TextView)itemView.findViewById(R.id.tv_projectCodeDailogOffline);
        }
    }
}
