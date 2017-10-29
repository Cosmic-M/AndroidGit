package com.development.cosmic_m.navigator;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;

import java.io.File;

/**
 * Created by Cosmic_M on 23.10.2017.
 */

public class TinyPictureFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TAG";
    private RouteComposeListener listener;
    private File file;
    private int tag;
    private static final String EXTRA_TAG_ARG = "tag";

    private ImageView mDetailedPointShow;
    private ImageView mAssignDestinationPoint;
    private ImageView mAssignTransitionPoint;
    private ImageView mExcludePoint;

    public static TinyPictureFragment newInstance(int tag) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TAG_ARG, tag);
        TinyPictureFragment fragment = new TinyPictureFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.i(TAG, "onCreate() / TinyPictureFragment called");
        tag = getArguments().getInt(EXTRA_TAG_ARG);
        MemoryPlace mp = PlaceLab.get(getActivity()).getMemoryPlace().get(tag);
        file = PlaceLab.get(getActivity()).getPhotoFile(mp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mini_image, container, false);
        listener = (RouteComposeListener) getActivity();
        mDetailedPointShow = (ImageView) view.findViewById(R.id.mini_image_id);
        mAssignDestinationPoint = (ImageView) view.findViewById(R.id.btn_target_point_id);
        mAssignTransitionPoint = (ImageView) view.findViewById(R.id.btn_transit_point_id);
        mExcludePoint = (ImageView) view.findViewById(R.id.btn_remove_point_id);
        Bitmap bitmap = PictureUtils.getScaledBitmap(file.getPath(), getActivity());
        mDetailedPointShow.setImageBitmap(bitmap);

        int resource = listener.getResourceForTransitionImage(tag);
        mAssignTransitionPoint.setImageResource(resource);

        mDetailedPointShow.setOnClickListener(this);
        mExcludePoint.setOnClickListener(this);
        mAssignDestinationPoint.setOnClickListener(this);
        mAssignTransitionPoint.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mini_image_id:
                Log.i(TAG, "click on mini picture");
                listener.onDetailedPointShow(tag);
                break;
            case R.id.btn_remove_point_id:
                Log.i(TAG, "click on remove btn");
                listener.onRemoveFragment(tag);
                break;
            case R.id.btn_target_point_id:
                Log.i(TAG, "click on assign target btn");
                int res = listener.onAssignDestinationPoint(tag);
                Log.i(TAG, "after click on assign target btn");
                if (res != 0) {
                    mAssignTransitionPoint.setImageResource(res);
                } else {
                    mAssignTransitionPoint.setImageResource(R.mipmap.transition_flag);
                }
                break;
            case R.id.btn_transit_point_id:
                Log.i(TAG, "click on assign transit btn");
                int resource = listener.onAssignTransitionPoint(tag);
                Log.i(TAG, "after click on assign target btn");
                mAssignTransitionPoint.setImageResource(resource);
                break;
            default:
                Log.i(TAG, "default");
                break;
        }
    }
}
