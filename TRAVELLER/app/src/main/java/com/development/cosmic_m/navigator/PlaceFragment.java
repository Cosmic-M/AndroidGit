package com.development.cosmic_m.navigator;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;

/**
 * Created by Cosmic_M on 17.09.2017.
 */

public class PlaceFragment extends Fragment {
    private static final String TAG = "TAG";
    private static final String EXTRA_ARG = "com.development.cosmic_m.navigator.PlaceFragment";

    private MemoryPlace mPlace;
    private ImageView mImagePlace;
    private TextView mLatitude;
    private TextView mLongitude;
    private TextView mTextDescription;

    public static PlaceFragment newInstance(MemoryPlace mp){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ARG, mp);
        PlaceFragment fragment = new PlaceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlace = (MemoryPlace) getArguments().getSerializable(EXTRA_ARG);
        Log.i(TAG, "PlaceFragment / mPlace.getLatLng().latitude = " + mPlace.getLatLng().latitude);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        View view = inflater.inflate(R.layout.fragment_place_pager, container, false);
        mImagePlace = (ImageView) view.findViewById(R.id.iv_picture);
        mLatitude = (TextView) view.findViewById(R.id.tv_latitude);
        mLongitude = (TextView) view.findViewById(R.id.tv_longitude);
        mTextDescription = (TextView) view.findViewById(R.id.tv_description);
        Bitmap bitmap = PictureUtils.getScaledBitmap(PlaceLab.get(getActivity())
                .getPhotoFile(mPlace).getPath(), getActivity());
        mImagePlace.setImageBitmap(bitmap);
        Log.i(TAG, "mLatitude == null is " + (mLatitude == null));
        mLatitude.setText(String.valueOf(mPlace.getLatLng().latitude));
        mLongitude.setText(String.valueOf(mPlace.getLatLng().longitude));
        mTextDescription.setText(mPlace.getTextDescription());
        return  view;
    }
}
