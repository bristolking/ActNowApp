package com.actnow.android.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.AdavancedTaskRecords;

import java.util.ArrayList;
import java.util.List;

public class AdavncedSearchAdapter extends RecyclerView.Adapter<AdavncedSearchAdapter.viewHolder> {
    private List<AdavancedTaskRecords> adavancedTaskRecordsList;
     private  Context context;

    public AdavncedSearchAdapter(ArrayList<AdavancedTaskRecords> adavancedTaskRecordsArrayList, int custom_advanced, Context applicationContext) {
        this.adavancedTaskRecordsList = adavancedTaskRecordsArrayList;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext()).inflate( R.layout.custom_advanced_list,viewGroup,false );
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        AdavancedTaskRecords adavancedTaskRecords = adavancedTaskRecordsList.get(i);
        viewHolder.tv_taskKeyName.setText( adavancedTaskRecords.getName());
        viewHolder.tv_taskCode.setText(adavancedTaskRecords.getTask_code());
        viewHolder.tv_taskadavncedId.setText( adavancedTaskRecords.getTask_id());
    }

    @Override
    public int getItemCount() {
        return adavancedTaskRecordsList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tv_taskKeyName,tv_taskadavncedId,tv_taskCode;
        public viewHolder(@NonNull View itemView) {
            super( itemView );
            tv_taskKeyName = (TextView)itemView.findViewById(R.id.tv_taskKeyName);
            tv_taskadavncedId =(TextView)itemView.findViewById(R.id.tv_taskAdvancedID);
            tv_taskCode =(TextView)itemView.findViewById(R.id.tv_taskCode);

        }
    }
    public void removeItem(int position) {
        adavancedTaskRecordsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, adavancedTaskRecordsList.size());
    }
}
