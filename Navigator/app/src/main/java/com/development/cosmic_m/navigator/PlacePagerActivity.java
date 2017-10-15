package com.development.cosmic_m.navigator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class
PlacePagerActivity extends FragmentActivity {
    private static final String TAG = "TAG";
    private ViewPager mViewPager;
    private List<MemoryPlace> mPlaceList = null;

    public static Intent newIntent(Context context){
        return new Intent(context, PlacePagerActivity.class);
    }

    public void notifyPagerAdapter(){
        mPlaceList = PlaceLab.get(getApplicationContext()).getMemoryPlace();
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_pager);
        mViewPager = (ViewPager) findViewById(R.id.activity_place_pager_view_pager);
        mPlaceList = PlaceLab.get(this).getMemoryPlace();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = PlaceFragment.newInstance(mPlaceList.get(position));
                return fragment;
            }

            @Override
            public int getItemPosition(Object object){
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return mPlaceList.size();
            }
        });
    }
}
