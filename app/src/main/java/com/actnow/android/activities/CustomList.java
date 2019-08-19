package com.actnow.android.activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.R;

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public CustomList(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.custom_comment_list, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_comment_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.tv_commentText);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_userprofileComment);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}