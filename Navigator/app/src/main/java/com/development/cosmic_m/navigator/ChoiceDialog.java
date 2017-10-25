package com.development.cosmic_m.navigator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Cosmic_M on 18.10.2017.
 */

public class ChoiceDialog extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "TAG";
    private static final String REQUEST_TEXT_DIALOG = "package com.development.cosmic_m.navigator.request_text_dialog";
    private static final String EXTRA_LATLNG_ARG = "package com.development.cosmic_m.navigator.extra_latlng_arg";
    private static final int REQUEST_NEW_POINT_REMOVE_OLD_DESTINATION = 350;
    private View view = null;
    private TextView request;
    private Button positiveBtn;
    private Button negativeBtn;
    private String text;
    private LatLng mLatLng;

    public static ChoiceDialog newInstance(String textRequest, LatLng location){
        Log.i(TAG, "newInstance(String textRequest)");
        Bundle bundle = new Bundle();
        bundle.putString(REQUEST_TEXT_DIALOG, textRequest);
        bundle.putParcelable(EXTRA_LATLNG_ARG, location);
        ChoiceDialog dialog = new ChoiceDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        Log.i(TAG, "onCreate from ChoiceDialog called");
        this.setRetainInstance(true);
        text = getArguments().getString(REQUEST_TEXT_DIALOG);
        mLatLng = getArguments().getParcelable(EXTRA_LATLNG_ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        Log.i(TAG, "onCreateView from ChoiceDialog called");
        if (view == null) {
            Log.i(TAG, "view == null");
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            view = inflater.inflate(R.layout.dialog_container, container, false);
            request = (TextView) view.findViewById(R.id.text_dialog_id);
            positiveBtn = (Button) view.findViewById(R.id.positive_btn_id);
            negativeBtn = (Button) view.findViewById(R.id.negative_btn_id);

            positiveBtn.setOnClickListener(this);
            negativeBtn.setOnClickListener(this);

            Log.i(TAG, "before request.setText(text)");
            request.setText(text);
        }
        else{
            Log.i(TAG, "view != null");
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    @Override
    public void onDestroyView(){
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.positive_btn_id:
                Log.i(TAG, "positive button pressed");
                Intent intent = AddPointActivity.newIntent(getActivity());
                intent.putExtra("latlng", mLatLng);
                getActivity().startActivityForResult(intent, REQUEST_NEW_POINT_REMOVE_OLD_DESTINATION);
                dismiss();
                break;
            case R.id.negative_btn_id:
                Log.i(TAG, "negative button pressed");
                dismiss();
                break;
            default:
                break;
        }
    }
}
