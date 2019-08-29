package com.actnow.android.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.ProjectCommentRecordsList;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.ArrayList;

public class ProjectCommentListAdapter  extends RecyclerView.Adapter<ProjectCommentListAdapter.ViewHolder> {
    private ArrayList<ProjectCommentRecordsList> mProjectCommentRecordsList;

    public ProjectCommentListAdapter(ArrayList<ProjectCommentRecordsList> projectCommentRecordsListArrayList, int custom_project_footer, Context applicationContext) {
        this.mProjectCommentRecordsList = projectCommentRecordsListArrayList;
    }

    @NonNull
    @Override
    public ProjectCommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_custom_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectCommentListAdapter.ViewHolder viewHolder, int i) {
        ProjectCommentRecordsList projectCommentRecordsList = mProjectCommentRecordsList.get(i);
        viewHolder.mProjectComment.setText(projectCommentRecordsList.getComment());

       /* String imgUrl = projectCommentRecordsList.getFiles();
        //   System.out.println("image" + imgUrl);
        Glide.with(context).load(imgUrl)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_wrong_sign_red)
                .into(viewHolder.imgComment);*/
        //System.out.println("cooomment"+ projectCommentRecordsList.getComment());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewHolder.mProjectComment.setText( Html.fromHtml(projectCommentRecordsList.getComment(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            viewHolder.mProjectComment.setText(Html.fromHtml(projectCommentRecordsList.getComment()));
        }
    }

    @Override
    public int getItemCount() {
        return mProjectCommentRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mProjectComment;
        TextView mProjectCode;
        ImageView imgComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProjectComment = (TextView)itemView.findViewById(R.id.tv_commentText);
            imgComment =(ImageView) itemView.findViewById(R.id.img_attachamentComment);

        }
    }
}
