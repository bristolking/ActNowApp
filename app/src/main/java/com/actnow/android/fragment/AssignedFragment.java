package com.actnow.android.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actnow.android.R;


public class AssignedFragment extends Fragment {
    FloatingActionButton fabAssignedTask;

    public  AssignedFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.fragment_assigned, container, false);
        fabAssignedTask = view.findViewById(R.id.fab_assignedtask);
        fabAssignedTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Work in Progress!",Toast.LENGTH_LONG).show();
            }
        });
     return view;
    }


}
