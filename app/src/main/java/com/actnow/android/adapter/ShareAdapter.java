package com.actnow.android.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.activeandroid.Cache.getContext;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder>  {
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
        viewHolder.shareId.setText(orgnUserRecordsCheckBox.getId());
        UserPrefUtils  session = new UserPrefUtils( getContext() );
        HashMap<String,String> userId = session.getUserDetails();
        String email = userId.get( UserPrefUtils.EMAIL);
        System.out.println( "emailReponse" + email + " " + viewHolder.mShareEmail.getText().toString());

       /* if (email.equals( viewHolder.mShareEmail.getText().toString())) {
            viewHolder.mImgDelete.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_logout_red));
        }
*/

    }

    @Override
    public int getItemCount() {
        return orgnUserRecordsCheckBoxList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mShareName,mShareEmail,shareId;
        ImageView  mImgDelete;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mShareName= (TextView)itemView.findViewById(R.id.tv_shareName );
            mShareEmail=(TextView)itemView.findViewById(R.id.tv_shareEmail);
            shareId = (TextView)itemView.findViewById(R.id.tv_shareId);
            mImgDelete = (ImageView)itemView.findViewById(R.id.img_delete);



        }

    }
}