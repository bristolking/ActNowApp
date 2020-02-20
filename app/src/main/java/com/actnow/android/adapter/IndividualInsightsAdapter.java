package com.actnow.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.IndividualMembersReponse;

import java.util.ArrayList;
import java.util.List;

public class IndividualInsightsAdapter extends RecyclerView.Adapter<IndividualInsightsAdapter.ViewHolder>{

    List<IndividualMembersReponse> individualMembersReponseList;

    public IndividualInsightsAdapter(ArrayList<IndividualMembersReponse> individualMembersReponseArrayList) {
        this.individualMembersReponseList = individualMembersReponseArrayList;
    }

    @NonNull
    @Override
    public IndividualInsightsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_individuval_insight,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IndividualInsightsAdapter.ViewHolder viewHolder, int i) {
        IndividualMembersReponse individualMembersReponse = individualMembersReponseList.get(i);
        viewHolder.mInsightsIndividualName.setText(individualMembersReponse.getName());
        viewHolder.mIndividualInsightsApproval.setText(individualMembersReponse.getApproval());
        viewHolder.mIndividualInsightscompleted.setText(individualMembersReponse.getCompleted());
        viewHolder.mIndividualInsightsOngoing.setText(individualMembersReponse.getOngoing());
        viewHolder.mIndividualInsightsPeniding.setText(individualMembersReponse.getPending());
    }
    @Override
    public int getItemCount() {
        return individualMembersReponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mInsightsIndividualName,mIndividualInsightsApproval,mIndividualInsightsPeniding,mIndividualInsightsOngoing,mIndividualInsightscompleted;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mInsightsIndividualName = itemView.findViewById(R.id.tv_individual_InsgihtsName);
            mIndividualInsightsApproval = itemView.findViewById(R.id.tv_individualInsightsApprovalTasks);
            mIndividualInsightsPeniding = itemView.findViewById(R.id.tv_individualInsightsPendingTasks);
            mIndividualInsightsOngoing = itemView.findViewById(R.id.tv_individualInsightsOngoingTasks);
            mIndividualInsightscompleted = itemView.findViewById(R.id.tv_individualInsightsCompleteTasks);
        }
    }
}
