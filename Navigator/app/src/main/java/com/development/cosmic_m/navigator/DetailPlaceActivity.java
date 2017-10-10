package com.development.cosmic_m.navigator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;

/**
 * Created by Cosmic_M on 03.10.2017.
 */

public class DetailPlaceActivity extends AppCompatActivity {
    private static final String TAG = "DetailPlaceActivity";
    private static final String EXTRA = "com.development.cosmic_m.navigator.Modules.MemoryPlace";
    private static final String EXTRA_LATITUDE = "latitude";
    private static final String EXTRA_LONGITUDE = "longitude";
    private static final String EXTRA_DESCRIPTION = "text_description";
    private MemoryPlace mMemoryPlace;
    private ImageView mImage;
    private TextView mLatitude;
    private TextView mLongitude;
    private JustifiedTextView mJustifiedTextView;


    public static Intent newInstance(Context context, MemoryPlace memoryPlace){
        Intent intent = new Intent(context, DetailPlaceActivity.class);
//        intent.putExtra(EXTRA, memoryPlace);
        double latitude = memoryPlace.getLatLng().latitude;
        double longitude = memoryPlace.getLatLng().longitude;
        String text = memoryPlace.getTextDescription();
        Log.i(TAG, "latitude = " + latitude + ", longitude = " + longitude);
        intent.putExtra(EXTRA_LATITUDE, latitude);
        intent.putExtra(EXTRA_LONGITUDE, longitude);
        intent.putExtra(EXTRA_DESCRIPTION, text);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_place_pager);
//        mMemoryPlace = (MemoryPlace) getIntent().getSerializableExtra(EXTRA);
//        mImage = (ImageView) findViewById(R.id.iv_picture);
        mLatitude = (TextView) findViewById(R.id.tv_latitude);
        mLongitude = (TextView) findViewById(R.id.tv_longitude);
        //mJustifiedTextView = (JustifiedTextView) findViewById(R.id.justified_text_view_id);
//
//        String text = mMemoryPlace.getTextDescription();
//        mJustifiedTextView.setText(text);
//        Bitmap bitmap = PictureUtils.getScaledBitmap(PlaceLab.get(this)
//                .getPhotoFile(mMemoryPlace).getPath(), this);
//        mImage.setImageBitmap(bitmap);
        double latit = getIntent().getDoubleExtra(EXTRA_LATITUDE, 0);
        double longit = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0);
        Log.i(TAG, "latit = " + latit + ", longit = " + longit);
        mLatitude.setText(String.valueOf(latit));
        mLongitude.setText(String.valueOf(longit));
        String t = getIntent().getStringExtra(EXTRA_DESCRIPTION);
        Log.i(TAG, "text = " + t);
       // mJustifiedTextView.setText(t);
//        mLatitude.setText(String.valueOf(mMemoryPlace.getLatLng().latitude));
//        mLongitude.setText(String.valueOf(mMemoryPlace.getLatLng().longitude));
    }
}
