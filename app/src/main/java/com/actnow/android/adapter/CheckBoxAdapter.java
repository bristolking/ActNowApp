package com.actnow.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.ViewHolder> {
    private List<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList;



    public CheckBoxAdapter(ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxArrayList, int individual_check, Context applicationContext) {
        this.orgnUserRecordsCheckBoxList = orgnUserRecordsCheckBoxArrayList;
    }
    @NonNull
    @Override
    public CheckBoxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_check,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckBoxAdapter.ViewHolder viewHolder, int i) {
        final OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgnUserRecordsCheckBoxList.get(i);
        viewHolder.mIndividualName.setText(orgnUserRecordsCheckBox.getName());
        viewHolder.mindivdualEmail.setText(orgnUserRecordsCheckBox.getEmail());

    }

    @Override
    public int getItemCount() {
        return orgnUserRecordsCheckBoxList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mIndividualName,mindivdualEmail;
       // EditText mEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIndividualName= (TextView)itemView.findViewById(R.id.tv_individualName );
            mindivdualEmail=(TextView)itemView.findViewById(R.id.tv_individualEmail);

        }

    }
    /*public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    orgnUserRecordsCheckBoxList = orgnUserRecordsCheckBoxList;
                } else {
                    List<OrgnUserRecordsCheckBox> filteredList = new ArrayList<>();
                    for (OrgnUserRecordsCheckBox row : orgnUserRecordsCheckBoxList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    orgnUserRecordsCheckBoxList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = orgnUserRecordsCheckBoxList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                orgnUserRecordsCheckBoxList = (ArrayList<OrgnUserRecordsCheckBox>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(OrgnUserRecordsCheckBox orgnUserRecordsCheckBox);
    }*/
}
