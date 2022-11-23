package com.latenightchauffeurs.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;

import java.util.List;

/**
 * Created by narayana on 3/6/2018.
 */

public class Rides extends android.support.v4.app.Fragment {
    private FragmentActivity myContext;
    public static Rides Instance;
    public static String TAG = Rides.class.getName();
    public static Context mcontext;
    public RelativeLayout container;
    public static int id = 0;

    TabLayout tabLayout;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_rides, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        container = (RelativeLayout) v.findViewById(R.id.container);
        // viewPager = (ViewPager) findViewById(R.id.viewPager);

        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));

        Instance = this;
        mcontext = getActivity();

        //  viewPagerAdapter = new ViewPagerAdapter(mContext,getSupportFragmentManager());
        //viewPager.setAdapter(viewPagerAdapter);
        //  viewPager.setCurrentItem(1);
        //tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                new Utils(mcontext);
                //  Utils.toastTxt(String.valueOf(tab.getPosition()),Utils.context);

                if (tab.getPosition() == 0) {
                    tab = tabLayout.getTabAt(0);
                    tab.select();
                    replaceFragment(new DashBorad());
                } else if (tab.getPosition() == 1) {
                    tab = tabLayout.getTabAt(1);
                    tab.select();
                    replaceFragment(new RideHistory());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (id == 0) {
            Log.e("tabs====123", "456");

            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            replaceFragment(new DashBorad());
        } else if (id == 1) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            replaceFragment(new RideHistory());
            Log.e("tabs====123", "4567");
        }
        super.onViewCreated(v, savedInstanceState);
    }

    public void replaceFragment(android.support.v4.app.Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager fragmentManager = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionhaystack(int page);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final FragmentManager childFragmentManager = getChildFragmentManager();

        if (childFragmentManager != null) {
            final List<Fragment> nestedFragments = childFragmentManager.getFragments();

            if (nestedFragments == null || nestedFragments.size() == 0) return;

            for (Fragment childFragment : nestedFragments) {
                if (childFragment != null && !childFragment.isDetached() && !childFragment.isRemoving()) {
                    childFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }




//        if (childFragmentManager != null) {
//            RideHistory page = (RideHistory) childFragmentManager.getPrimaryNavigationFragment();
//            if (page != null){
//                page.onActivityResult(requestCode, resultCode, data);
//            }
//        }
       // Toast.makeText(mcontext , "sdcsfAAAA " +requestCode, Toast.LENGTH_SHORT).show();
    }


}
