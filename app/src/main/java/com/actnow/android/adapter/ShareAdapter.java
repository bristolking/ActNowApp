package com.actnow.android.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;

import java.util.ArrayList;
import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> implements Filterable {
    private List<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList;

    public ShareAdapter(ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList, int individual_check, Context applicationContext) {
        this.orgnUserRecordsCheckBoxList = orgnUserRecordsCheckBoxList;

    }

    @NonNull
    @Override
    public ShareAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.share_intivitaion_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareAdapter.ViewHolder viewHolder, int i) {
        final OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgnUserRecordsCheckBoxList.get(i);
        viewHolder.mShareName.setText(orgnUserRecordsCheckBox.getName());
        viewHolder.mShareEmail.setText(orgnUserRecordsCheckBox.getEmail());

    }

    @Override
    public int getItemCount() {
        return orgnUserRecordsCheckBoxList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mShareName,mShareEmail;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mShareName= (TextView)itemView.findViewById(R.id.tv_shareName );
            mShareEmail=(TextView)itemView.findViewById(R.id.tv_shareEmail);

        }
    }
}