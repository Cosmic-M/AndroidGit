package com.development.cosmic_m.navigator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;

import org.xmlpull.v1.XmlPullParser;

import java.util.StringTokenizer;

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
    private Button mRemoveBtn;
    private JustifiedTextView mJustifiedTextView;

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
        mRemoveBtn = (Button) view.findViewById(R.id.btn_remove_point);
        mImagePlace = (ImageView) view.findViewById(R.id.iv_picture);
        mLatitude = (TextView) view.findViewById(R.id.tv_latitude);
        mLongitude = (TextView) view.findViewById(R.id.tv_longitude);
        mJustifiedTextView = (JustifiedTextView) view.findViewById(R.id.justified_text_view_id);

        mRemoveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getActivity(), "POINT REMOVED", Toast.LENGTH_SHORT).show();
            }
        });
        String text = mPlace.getTextDescription();
        mJustifiedTextView.setText(text);
        Bitmap bitmap = PictureUtils.getScaledBitmap(PlaceLab.get(getActivity())
                .getPhotoFile(mPlace).getPath(), getActivity());
        mImagePlace.setImageBitmap(bitmap);

        mLatitude.setText(String.valueOf(mPlace.getLatLng().latitude));
        mLongitude.setText(String.valueOf(mPlace.getLatLng().longitude));
        return  view;
    }
}
