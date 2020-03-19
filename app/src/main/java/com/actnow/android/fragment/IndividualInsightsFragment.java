package com.actnow.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.insights.IndividualInsighsGrapghActivity;
import com.actnow.android.adapter.IndividualInsightsAdapter;
import com.actnow.android.sdk.responses.IndividualMembersReponse;
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


public class IndividualInsightsFragment extends Fragment {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    IndividualInsightsAdapter mIndividualInsightsAdapter;
    RecyclerView mRecyclerViewIndividualInsights;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<IndividualMembersReponse> individualMembersReponseArrayList = new ArrayList<IndividualMembersReponse>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils(getActivity());
        View view =  inflater.inflate(R.layout.fragment_individual_insights, container, false);
        mProgressView =view.findViewById(R.id.progress_bar);
        mContentLayout = view.findViewById(R.id.content_layout);
        mRecyclerViewIndividualInsights = (RecyclerView)view. findViewById(R.id.idividualInsights_recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewIndividualInsights.setLayoutManager(mLayoutManager);
        mRecyclerViewIndividualInsights.setItemAnimator(new DefaultItemAnimator());
        mIndividualInsightsAdapter = new IndividualInsightsAdapter(individualMembersReponseArrayList);
        mRecyclerViewIndividualInsights.setAdapter(mIndividualInsightsAdapter);
        apiCallIndividual();
        return view;

    }
    private void apiCallIndividual() {
        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(UserPrefUtils.ID);
        Call<ResponseBody> responseBodyCall = ANApplications.getANApi().individualInsights(id);
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
                                JSONArray jsonArray = jsonObject.getJSONArray("members");
                                System.out.println("SeverReponse2" + jsonArray);
                                setIndividualInsightsList(jsonArray);
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
    private void setIndividualInsightsList(JSONArray jsonArray) {
        for (int i = 0; jsonArray.length() > i; i++) {
            IndividualMembersReponse individualMembersReponse = new IndividualMembersReponse();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String email = jsonObject.getString("email");
                String mobile_number = jsonObject.getString("mobile_number");
                String provider_id = jsonObject.getString("provider_id");
                String provider_name = jsonObject.getString("provider_name");
                String orgn_code = jsonObject.getString("orgn_code");
                String password = jsonObject.getString("password");
                String image_path = jsonObject.getString("image_path");
                String user_type = jsonObject.getString("user_type");
                String otp = jsonObject.getString("otp");
                String status = jsonObject.getString("status");
                String email_verified_at = jsonObject.getString("email_verified_at");
                String verified = jsonObject.getString("verified");
                String remember_token = jsonObject.getString("remember_token");
                String refresh_token = jsonObject.getString("refresh_token");
                String created_at = jsonObject.getString("created_at");
                String updated_at = jsonObject.getString("updated_at");
                String other_orgns = jsonObject.getString("other_orgns");
                String timezone = jsonObject.getString("timezone");
                String completed = jsonObject.getString("completed");
                String approval = jsonObject.getString("approval");
                String ongoing = jsonObject.getString("ongoing");
                String pending = jsonObject.getString("pending");

                individualMembersReponse.setId(id);
                individualMembersReponse.setName(name);
                individualMembersReponse.setEmail(email);
                individualMembersReponse.setMobile_number(mobile_number);
                individualMembersReponse.setProvider_id(provider_id);
                individualMembersReponse.setProvider_name(provider_name);
                individualMembersReponse.setOrgn_code(orgn_code);
                individualMembersReponse.setPassword(password);
                individualMembersReponse.setImage_path(image_path);
                individualMembersReponse.setUser_type(user_type);
                individualMembersReponse.setOtp(otp);
                individualMembersReponse.setStatus(status);
                individualMembersReponse.setEmail_verified_at(email_verified_at);
                individualMembersReponse.setVerified(verified);
                individualMembersReponse.setRemember_token(remember_token);
                individualMembersReponse.setRefresh_token(refresh_token);
                individualMembersReponse.setCreated_at(created_at);
                individualMembersReponse.setUpdated_at(updated_at);
                individualMembersReponse.setOther_orgns(other_orgns);
                individualMembersReponse.setTimezone(timezone);
                individualMembersReponse.setCompleted(completed);
                individualMembersReponse.setApproval(approval);
                individualMembersReponse.setOngoing(ongoing);
                individualMembersReponse.setPending(pending);
            }catch (JSONException e){
                e.printStackTrace();
            }
            individualMembersReponseArrayList.add(individualMembersReponse);
            mRecyclerViewIndividualInsights.setAdapter(mIndividualInsightsAdapter);
            mRecyclerViewIndividualInsights.addOnItemTouchListener(new IndividualInsightsFragment.RecyclerTouchListener(this, mRecyclerViewIndividualInsights, new IndividualInsightsFragment.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    View viewLiner = view.findViewById(R.id.view_liner);
                    final TextView mInsightsIndividualName = view.findViewById(R.id.tv_individual_InsgihtsName);
                    final TextView mIndividualInsightsColor = view.findViewById(R.id.tv_individual_InsgihtsColor);
                    ImageView imageViewIndividualInsights = (ImageView) view.findViewById(R.id.image_insightsIndividual);
                    viewLiner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String nameInsights = mInsightsIndividualName.getText().toString();
                            String colorInsights = mIndividualInsightsColor.getText().toString();
                            Intent i = new Intent(getActivity(), IndividualInsighsGrapghActivity.class);
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

        private IndividualInsightsFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(IndividualInsightsFragment context, final RecyclerView mRecylerViewSingleSub, IndividualInsightsFragment.ClickListener clickListener) {
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
        individualMembersReponseArrayList.clear();
    }

}
