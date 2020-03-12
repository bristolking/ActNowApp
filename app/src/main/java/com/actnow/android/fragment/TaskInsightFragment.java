package com.actnow.android.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actnow.android.R;
import com.actnow.android.utils.UserPrefUtils;


public class TaskInsightFragment extends Fragment {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_insight, container, false);
        Toolbar toolbar =view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        //setSupportActionBar(toolbar);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setFocusable(true);
        tabLayout.setMinimumWidth(10);
        tabLayout.setupWithViewPager(viewPager);
        return view;

    }
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new DailyInsightsFragment();
            } else if (position == 1) {
                fragment = new WeeklyInsightsFragment();
            } else if (position == 2) {
                fragment = new MonthlyInsightsFragment();
            } else if (position == 3) {
                fragment = new YearlyInsightsFragment();
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return 4;
        }

        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = "DAILY";
            } else if (position == 1) {
                title = "WEEKLY";
            } else if (position == 2) {
                title = "MONTHLY";
            }else if (position == 3) {
                title = "YEARLY";
            }
            return title;
        }
    }
}
