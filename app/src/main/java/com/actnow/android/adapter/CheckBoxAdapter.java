package com.actnow.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

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
        viewHolder.mCheckBox.setText(orgnUserRecordsCheckBox.getName());

    }

    @Override
    public int getItemCount() {
        return orgnUserRecordsCheckBoxList.size();
    }

    public void filterList(ArrayList<OrgnUserRecordsCheckBox> filteredList) {
        orgnUserRecordsCheckBoxList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
       // EditText mEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox= (CheckBox)itemView.findViewById(R.id.ownerOne);
           // mEditText=(EditText)itemView.findViewById(R.id.ed_individualName);

        }

    }
}
