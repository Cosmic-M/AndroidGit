package com.development.cosmic_m.navigator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;

import java.util.List;

/**
 * Created by Cosmic_M on 17.09.2017.
 */

public class PlacePagerActivity extends FragmentActivity{
    private static final String TAG = "TAG";
    private ViewPager mViewPager;
    private List<MemoryPlace> mPlaceList;

    public static Intent newIntent(Context context){
        return new Intent(context, PlacePagerActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_pager);
        mViewPager = (ViewPager) findViewById(R.id.activity_place_pager_view_pager);
        mPlaceList = PlaceLab.get(this).getMemoryPlacesList();
        Log.i(TAG, "PlacePagerActivity/list.size() = " + mPlaceList.size());
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Log.i(TAG, "PlacePagerActivity/getItem => mPlaceList.get(position) = " + mPlaceList.get(position));
                Fragment fragment = PlaceFragment.newInstance(mPlaceList.get(position));
                return fragment;
            }

            @Override
            public int getCount() {
                return mPlaceList.size();
            }
        });
    }
}
