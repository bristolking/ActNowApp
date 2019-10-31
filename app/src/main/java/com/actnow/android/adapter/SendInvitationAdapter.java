package com.actnow.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;

import java.util.ArrayList;
import java.util.List;

public class SendInvitationAdapter extends RecyclerView.Adapter<SendInvitationAdapter.ExampleViewHolder> {


    private List<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);

        }
    }

    public SendInvitationAdapter(ArrayList<OrgnUserRecordsCheckBox> exampleList) {
        orgnUserRecordsCheckBoxList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_custom_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        OrgnUserRecordsCheckBox currentItem = orgnUserRecordsCheckBoxList.get(position);

        holder.mTextView1.setText(currentItem.getName());
        holder.mTextView2.setText(currentItem.getEmail());
    }

    @Override
    public int getItemCount() {
        return orgnUserRecordsCheckBoxList.size();
    }

    public void filterList(ArrayList<OrgnUserRecordsCheckBox> filteredList) {
        orgnUserRecordsCheckBoxList = filteredList;
        notifyDataSetChanged();
    }
}