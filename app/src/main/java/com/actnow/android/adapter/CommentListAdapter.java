package com.actnow.android.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.sdk.responses.CommentModel;
import com.actnow.android.sdk.responses.ProjectCommentRecordsList;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter  extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private List<CommentModel>  commentModelList;
    private  List<ProjectCommentRecordsList> projectCommentRecordsLists;

   /* public CommentListAdapter(ArrayList<CommentModel> commentModelArrayList, int custom_comment_list, Context applicationContext) {
        this.commentModelList= commentModelArrayList;

    }*/

    public CommentListAdapter(ArrayList<ProjectCommentRecordsList> projectCommentRecordsListArrayList, int custom_comment_list, Context applicationContext) {
        this.projectCommentRecordsLists = projectCommentRecordsListArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_custom_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ProjectCommentRecordsList projectCommentRecordsList = projectCommentRecordsLists.get(i);
        viewHolder.mTextComment.setText(projectCommentRecordsList.getComment());
       // CommentModel commentModel = commentModelList.get(i);
        //viewHolder.mTextComment.setText(commentModel.getComment());
        //viewHolder.mImageComment.setImageResource(Integer.parseInt(commentModel.getImg()));

    }

    @Override
    public int getItemCount() {
        return projectCommentRecordsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextComment;
        ImageView mImageComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextComment =(TextView)itemView.findViewById(R.id.tv_commentText);
            //mImageComment =(ImageView)itemView.findViewById(R.id.img_userprofileComment);
        }
    }
}
