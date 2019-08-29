package com.actnow.android.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actnow.android.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {


    private ArrayList<String> mFileList;
    private Context mContext;
    private AssetManager assetManager;
    public FileAdapter(Context context, ArrayList<String> mFileList) {
        this.mContext = context;
        mFileList = new ArrayList<String>();
        this.mFileList = mFileList;
        assetManager = context.getAssets();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.imge,viewGroup,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String imageUrl = mFileList.get(i);
        Glide.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(viewHolder.imgeView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.imgeView.setClipToOutline(true);
        }
    }

    @Override
    public int getItemCount() {
        return  mFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgeView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgeView = (ImageView)itemView.findViewById(R.id.imageView1);
        }
    }
}
