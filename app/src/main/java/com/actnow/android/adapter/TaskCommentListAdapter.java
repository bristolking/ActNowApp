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
import com.actnow.android.sdk.responses.TaskCommentListResponse;
import com.bumptech.glide.Glide;
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
        viewHolder.mCommentId.setText( taskCommentListResponse.getComment_id());
        String imgUrl = taskCommentListResponse.getFiles();
        if (imgUrl != null ){
            if (!imgUrl.isEmpty()) {
                if(imgUrl.contains(",")) {
                    String str = imgUrl.replaceAll( "\\[", "" ).replaceAll( "\\]", "" );
                    String[] oneImage = str.split( "," );
                    oneImage[0] = oneImage[0].replaceAll( "^\"|\"$", "" );
                    System.out.println( "image" + oneImage[0] );
                    Glide.with( context ).load( oneImage[0] )
                            .centerCrop()
                            .override( 300, 200 )
                            .placeholder( R.drawable.ic_launcher_background )
                            .error( R.drawable.ic_wrong_sign_red )
                            .into( viewHolder.imgComment );
                }else {
                    System.out.println( "imgUrl" + imgUrl );

                    if (imgUrl.contains("[]") || imgUrl.contains( "null" )) {
                    } else {
                        String str = imgUrl.replaceAll( "\\[", "" ).replaceAll( "\\]", "" );
                        str = str.replaceAll( "^\"|\"$", "" );
                        Glide.with( context ).load( str )
                                .centerCrop()
                                .override( 300, 200 )
                                .placeholder( R.drawable.ic_launcher_background )
                                .error( R.drawable.ic_wrong_sign_red )
                                .into( viewHolder.imgComment );
                    }

                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewHolder.mTaskComment.setText( Html.fromHtml( taskCommentListResponse.getComment(), Html.FROM_HTML_MODE_COMPACT ) );
        } else {
            viewHolder.mTaskComment.setText( Html.fromHtml( taskCommentListResponse.getComment() ) );
        }

    }

    @Override
    public int getItemCount() {
        return taskCommentListResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTaskComment, mTaskCommentDate, mTaskCommentUserName,mCommentId;
        ImageView imgComment;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mTaskComment = (TextView) itemView.findViewById( R.id.tv_commentText );
            imgComment = (ImageView) itemView.findViewById( R.id.img_attachamentComment );
            mTaskCommentUserName = (TextView) itemView.findViewById( R.id.tv_userNameComment );
            mTaskCommentDate = (TextView) itemView.findViewById( R.id.tv_commentDate );
            mCommentId =(TextView) itemView.findViewById(R.id.tv_commentId);
        }
    }
}
