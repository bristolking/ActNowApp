package com.actnow.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.R;
import com.actnow.android.activities.tasks.ViewTasksActivity;
import com.actnow.android.adapter.TaskListAdapter;
import com.actnow.android.sdk.responses.TaskListRecords;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;


public class PriorityFragment extends Fragment {

    RecyclerView mTaskRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fabTask;
    TaskListAdapter mTaskListAdapter;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    private ArrayList<TaskListRecords> taskListRecordsArrayList = new ArrayList<TaskListRecords>();


    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();
    String id;
    MultiSelectDialog mIndividuvalDialogtime, mProjectDialogtime;
    public  PriorityFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_priority, container, false);

        mProgressView = view.findViewById(R.id.progress_bar);
        mContentLayout = view.findViewById(R.id.content_layout);
        fabTask = view.findViewById(R.id.fab_prioritytask);
        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Work in Progress!",Toast.LENGTH_LONG).show();
            }
        });
    return  view;
    }



}
