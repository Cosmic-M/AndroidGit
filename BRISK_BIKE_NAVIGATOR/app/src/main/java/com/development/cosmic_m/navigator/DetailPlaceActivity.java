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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;

import java.io.File;

/**
 * Created by Cosmic_M on 03.10.2017.
 */

public class DetailPlaceActivity extends AppCompatActivity {
    private static final String EXTRA_LATITUDE = "latitude";
    private static final String EXTRA_LONGITUDE = "longitude";
    private static final String EXTRA_DESCRIPTION = "text_description";
    private static final String EXTRA_FILE = "file";

    private ImageView mImage;
    private TextView mLatitude;
    private TextView mLongitude;
    private Button mRemoveBtn;
    private JustifiedTextView mJustifiedTextView;


    public static Intent newInstance(Context context, MemoryPlace memoryPlace){
        Intent intent = new Intent(context, DetailPlaceActivity.class);
        File file = PlaceLab.get(context).getPhotoFile(memoryPlace);
        double latitude = memoryPlace.getLatLng().latitude;
        double longitude = memoryPlace.getLatLng().longitude;
        String text = memoryPlace.getTextDescription();

        intent.putExtra(EXTRA_FILE, file);
        intent.putExtra(EXTRA_LATITUDE, latitude);
        intent.putExtra(EXTRA_LONGITUDE, longitude);
        intent.putExtra(EXTRA_DESCRIPTION, text);

        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_place_pager);
        mImage = (ImageView) findViewById(R.id.iv_picture);
        mLatitude = (TextView) findViewById(R.id.tv_latitude);
        mLongitude = (TextView) findViewById(R.id.tv_longitude);
        mRemoveBtn = (Button) findViewById(R.id.btn_remove_point);
        mRemoveBtn.setVisibility(View.INVISIBLE);
        mJustifiedTextView = (JustifiedTextView) findViewById(R.id.justified_text_view_id);

        File photoFile = (File) getIntent().getSerializableExtra(EXTRA_FILE);
        double latit = getIntent().getDoubleExtra(EXTRA_LATITUDE, 0);
        double longit = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0);
        String textDescription = getIntent().getStringExtra(EXTRA_DESCRIPTION);

        Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), this);
        mImage.setImageBitmap(bitmap);
        mLatitude.setText(String.valueOf(latit));
        mLongitude.setText(String.valueOf(longit));
        mJustifiedTextView.setText(textDescription);
    }
}
