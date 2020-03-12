package com.actnow.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.insights.ProjectInsightsActivity;
import com.actnow.android.activities.insights.ProjectsInsightsGraphActivity;
import com.actnow.android.adapter.ProjectInsightsAdapter;
import com.actnow.android.sdk.responses.ProjectsInsights;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProjectInsightsFragment extends Fragment {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    ProjectInsightsAdapter mProjectInsightsAdapter;
    RecyclerView mRecyclerViewProjectInsights;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ProjectsInsights> projectsInsightsArrayList = new ArrayList<ProjectsInsights>();
    final ProjectInsightsFragment context = this;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils(getContext());
        View view= inflater.inflate(R.layout.fragment_project_insights, container, false);
        apiCallProjectInsights();
        mProgressView = view.findViewById(R.id.progress_bar);
        mContentLayout = view.findViewById(R.id.content_layout);
        mRecyclerViewProjectInsights = (RecyclerView) view.findViewById(R.id.projectInsights_recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewProjectInsights.setLayoutManager(mLayoutManager);
        mRecyclerViewProjectInsights.setItemAnimator(new DefaultItemAnimator());
        mProjectInsightsAdapter = new ProjectInsightsAdapter(projectsInsightsArrayList);
        mRecyclerViewProjectInsights.setAdapter(mProjectInsightsAdapter);
        return view;

    }
    private void apiCallProjectInsights() {
        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(UserPrefUtils.ID);
        String orngcode = user.get(UserPrefUtils.ORGANIZATIONNAME);
        Call<ResponseBody> responseBodyCall = ANApplications.getANApi().projectInsightsReponse(id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                System.out.println("SeverReponse" + response.raw());
                if (response.isSuccessful()) {
                    System.out.println("SeverReponse1" + response.raw());
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getString("success").equals("true")) {
                                System.out.println("SeverReponse2" + response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("projects");
                                setProjectInsightsList(jsonArray);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void setProjectInsightsList(JSONArray jsonArray) {
        for (int i = 0; jsonArray.length() > i; i++) {
            ProjectsInsights projectsInsights = new ProjectsInsights();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String project_id = jsonObject.getString("project_id");
                String name = jsonObject.getString("name");
                String project_code = jsonObject.getString("project_code");
                String color = jsonObject.getString("color");
                String priority = jsonObject.getString("priority");
                String due_date = jsonObject.getString("due_date");
                String project_members = jsonObject.getString("project_members");
                String created_by = jsonObject.getString("created_by");
                String created_date = jsonObject.getString("created_date");
                String updated_by = jsonObject.getString("updated_by");
                String updated_date = jsonObject.getString("updated_date");

                String orgn_code = jsonObject.getString("orgn_code");
                String parent_project_code = jsonObject.getString("parent_project_code");
                String completed = jsonObject.getString("completed");
                String approval = jsonObject.getString("approval");
                String ongoing = jsonObject.getString("ongoing");
                String pending = jsonObject.getString("pending");
                // String[] pendingspilt = completed.split(" ");

                projectsInsights.setProject_id(project_id);
                projectsInsights.setName(name);
                projectsInsights.setProject_code(project_code);
                projectsInsights.setColor(color);
                projectsInsights.setPriority(priority);
                projectsInsights.setDue_date(due_date);
                projectsInsights.setProject_members(project_members);
                projectsInsights.setCreated_by(created_by);
                projectsInsights.setCreated_date(created_date);
                projectsInsights.setUpdated_by(updated_by);
                projectsInsights.setUpdated_date(updated_date);
                projectsInsights.setOrgn_code(orgn_code);
                projectsInsights.setParent_project_code(parent_project_code);
                projectsInsights.setCompleted(completed);
                projectsInsights.setApproval(approval);
                projectsInsights.setOngoing(ongoing);
                projectsInsights.setPending(pending);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            projectsInsightsArrayList.add(projectsInsights);
            mRecyclerViewProjectInsights.setAdapter(mProjectInsightsAdapter);
            mRecyclerViewProjectInsights.addOnItemTouchListener(new ProjectInsightsFragment.RecyclerTouchListener(this, mRecyclerViewProjectInsights, new ProjectInsightsFragment.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    View viewLinerProject =(View)view.findViewById(R.id.view_linerProject);
                    final TextView mInsightsProjectName = view.findViewById(R.id.tv_projects_InsgihtsName);
                    final TextView mProjectInsightsColor = view.findViewById(R.id.tv_projects_InsgihtsColor);
                    ImageView imageViewProjectInsights = (ImageView) view.findViewById(R.id.image_insightsProjects);
                    viewLinerProject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String nameInsights = mInsightsProjectName.getText().toString();
                            String colorInsights = mProjectInsightsColor.getText().toString();
                            Intent i = new Intent(getActivity(), ProjectsInsightsGraphActivity.class);
                            i.putExtra("nameInsights", nameInsights);
                            i.putExtra("colorInsights", colorInsights);
                            startActivity(i);
                        }
                    });

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ProjectInsightsFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(ProjectInsightsFragment context, final RecyclerView mRecylerViewSingleSub, ProjectInsightsFragment.ClickListener clickListener) {
            this.clicklistener = clickListener;
            gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                public void onLongPress(MotionEvent e) {
                    View child = mRecylerViewSingleSub.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, mRecylerViewSingleSub.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        projectsInsightsArrayList.clear();
    }

}
