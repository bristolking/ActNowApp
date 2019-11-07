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

public class ProjectCommentListAdapter extends RecyclerView.Adapter<ProjectCommentListAdapter.ViewHolder> {
    private ArrayList<ProjectCommentRecordsList> mProjectCommentRecordsList;
    private Context context;

    public ProjectCommentListAdapter(ArrayList<ProjectCommentRecordsList> projectCommentRecordsListArrayList, int custom_project_footer, Context applicationContext) {
        this.mProjectCommentRecordsList = projectCommentRecordsListArrayList;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public ProjectCommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.project_comment_list, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectCommentListAdapter.ViewHolder viewHolder, int i) {
        ProjectCommentRecordsList projectCommentRecordsList = mProjectCommentRecordsList.get( i );
        viewHolder.mProjectComment.setText( projectCommentRecordsList.getComment() );
        viewHolder.mProjectCommentDate.setText( projectCommentRecordsList.getCreated_date());
        viewHolder.mProjectommentUserName.setText( projectCommentRecordsList.getUser_name());
        viewHolder.mProjectCommentId.setText( projectCommentRecordsList.getComment_id());
        String imgUrl = projectCommentRecordsList.getFiles();
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
            viewHolder.mProjectComment.setText( Html.fromHtml( projectCommentRecordsList.getComment(), Html.FROM_HTML_MODE_COMPACT ) );
        } else {
            viewHolder.mProjectComment.setText( Html.fromHtml( projectCommentRecordsList.getComment() ) );
        }
    }

    @Override
    public int getItemCount() {
        return mProjectCommentRecordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mProjectComment, mProjectCommentDate, mProjectommentUserName,mProjectCommentId;
        ImageView imgComment;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mProjectComment = (TextView) itemView.findViewById( R.id.tv_projectcommentText );
            mProjectommentUserName = (TextView) itemView.findViewById( R.id.tv_projectuserNameComment );
            mProjectCommentDate = (TextView) itemView.findViewById( R.id.tv_projectcommentDate );
            mProjectCommentId = (TextView)itemView.findViewById(R.id.tv_projectcommentId);
            imgComment = (ImageView) itemView.findViewById( R.id.img_projectattachamentComment );

        }
    }
}
