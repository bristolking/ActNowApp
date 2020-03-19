package com.actnow.android.fragment;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actnow.android.R;

import com.actnow.android.utils.UserPrefUtils;



public class RepetitiveTabedFragment extends Fragment {
    UserPrefUtils session;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new UserPrefUtils(getContext());
        View  view= inflater.inflate(R.layout.fragment_tabed_repetitive, container, false);
        //Toolbar toolbar = view.findViewById(R.id.repetitive_toolbar);
        viewPager = (ViewPager)view.findViewById(R.id.repetitive_viewpager);
        viewPagerAdapter = new RepetitiveTabedFragment.ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout)view.findViewById(R.id.retitve_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new RepetitiveFragment();
            } else if (position == 1) {
                fragment = new DailyFragment();
            } else if (position == 2) {
                fragment = new WeeklyFragment();
            } else if (position == 3) {
                fragment = new MonthlyFragment();
            } else if (position == 4) {
                fragment = new YearlyFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = "Repet";
            } else if (position == 1) {
                title = "Daily";
            } else if (position == 2) {
                title = "week";
            }else if (position == 3) {
                title = "month";
            }else if (position == 4) {
                title = "year";
            }

            return title;
        }

    }



}

