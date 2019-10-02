package com.actnow.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.TaskCommentListResponse;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaskCommentListAdapter extends RecyclerView.Adapter<TaskCommentListAdapter.ViewHolder> {
    private List<TaskCommentListResponse> taskCommentListResponseList;
    private Context context;

    public TaskCommentListAdapter(ArrayList<TaskCommentListResponse> taskCommentListResponseArrayList, int comment_custom_list, Context applicationContext) {
        this.taskCommentListResponseList = taskCommentListResponseArrayList;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public TaskCommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext()).inflate( R.layout.comment_custom_list,viewGroup,false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCommentListAdapter.ViewHolder viewHolder, int i) {
        TaskCommentListResponse taskCommentListResponse = taskCommentListResponseList.get(i);
        viewHolder.mTaskComment.setText( taskCommentListResponse.getComment());
        viewHolder.mTaskCommentDate.setText( taskCommentListResponse.getCreated_date());
        viewHolder.mTaskCommentUserName.setText( taskCommentListResponse.getUser_name());
        String imgUrl = taskCommentListResponse.getFiles();
        System.out.println("image" + imgUrl);
        Glide.with(context).load(imgUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_wrong_sign_red)
                .into(viewHolder.imgComment);

    }

    @Override
    public int getItemCount() {
        return taskCommentListResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTaskComment, mTaskCommentDate, mTaskCommentUserName;
        TextView mTaskProjectCode;
        ImageView imgComment;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mTaskComment = (TextView) itemView.findViewById( R.id.tv_commentText );
            imgComment = (ImageView) itemView.findViewById( R.id.img_attachamentComment );
            mTaskCommentUserName = (TextView) itemView.findViewById( R.id.tv_userNameComment );
            mTaskCommentDate = (TextView) itemView.findViewById( R.id.tv_commentDate );
        }
    }
}
