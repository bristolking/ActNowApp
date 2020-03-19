package com.actnow.android.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.TimeLineRecordsTaskList;

import java.util.ArrayList;
import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {
    List<TimeLineRecordsTaskList> timeLineRecordsTaskListList;

    public TimeLineAdapter(ArrayList<TimeLineRecordsTaskList> timeLineRecordsTaskListArrayList, int timeline_custom, Context applicationContext) {
        this.timeLineRecordsTaskListList = timeLineRecordsTaskListArrayList;
    }

    @NonNull
    @Override
    public TimeLineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.timeline_custom,viewGroup,false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineAdapter.ViewHolder viewHolder, int i) {
        TimeLineRecordsTaskList timeLineRecordsTaskList = timeLineRecordsTaskListList.get( i );
        viewHolder.timeLineName.setText( timeLineRecordsTaskList.getCreated_by());
        viewHolder.timeLinedate.setText( timeLineRecordsTaskList.getCreated_at());
        viewHolder.timeLineTaskStatus.setText( timeLineRecordsTaskList.getAction());

    }

    @Override
    public int getItemCount() {
        return  timeLineRecordsTaskListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeLineName;
        TextView timeLinedate;
        TextView timeLineTaskStatus;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            timeLineName =(TextView)itemView.findViewById(R.id.tv_nametimeLine);
            timeLinedate =(TextView)itemView.findViewById(R.id.tv_timeLineDate);
            timeLineTaskStatus =(TextView)itemView.findViewById(R.id.tv_actiontype);

        }
    }
}
