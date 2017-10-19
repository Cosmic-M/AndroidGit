package com.development.cosmic_m.navigator;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Cosmic_M on 18.10.2017.
 */

public class ChoiceDialog extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "TAG";
    private static final String REQUEST_TEXT_DIALOG = "package com.development.cosmic_m.navigator.request_text_dialog";
    private View view = null;
    private TextView request;
    private Button positiveBtn;
    private Button negativeBtn;
    private String text;

    public static ChoiceDialog newInstance(String textRequest){
        Log.i(TAG, "newInstance(String textRequest)");
        Bundle bundle = new Bundle();
        bundle.putString(REQUEST_TEXT_DIALOG, textRequest);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        Log.i(TAG, "onCreateView from ChoiceDialog called");
        if (view == null) {
            Log.i(TAG, "view == null");
            getDialog().setTitle("saved location is alongside");
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
    public void onClick(View view){
        switch (view.getId()){
            case R.id.positive_btn_id:
                Log.i(TAG, "positive button pressed");
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
