package com.development.cosmic_m.navigator;

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

import com.development.cosmic_m.navigator.Modules.MemoryPlace;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Cosmic_M on 28.10.2017.
 */

public class RemoveOrCancelDialog extends DialogFragment implements View.OnClickListener{
    private static final String REQUEST_TEXT_DIALOG = "package com.development.cosmic_m.navigator.text_dialog";
    private static final String ROW_DB_MUST_DELETE = "package com.development.cosmic_m.navigator.row_must_delete";
    private View view = null;
    private TextView request;
    private Button positiveBtn;
    private Button negativeBtn;
    private String text;
    private int rowDBMustDelete;

    public static RemoveOrCancelDialog newInstance(String textRequest, int rowDBWhichMustRemove){
        Bundle bundle = new Bundle();
        bundle.putString(REQUEST_TEXT_DIALOG, textRequest);
        bundle.putInt(ROW_DB_MUST_DELETE, rowDBWhichMustRemove);
        RemoveOrCancelDialog dialog = new RemoveOrCancelDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        this.setRetainInstance(true);
        text = getArguments().getString(REQUEST_TEXT_DIALOG);
        rowDBMustDelete = getArguments().getInt(ROW_DB_MUST_DELETE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        if (view == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            view = inflater.inflate(R.layout.dialog_container, container, false);
            request = (TextView) view.findViewById(R.id.text_dialog_id);
            positiveBtn = (Button) view.findViewById(R.id.positive_btn_id);
            negativeBtn = (Button) view.findViewById(R.id.negative_btn_id);

            positiveBtn.setOnClickListener(this);
            negativeBtn.setOnClickListener(this);

            request.setText(text);
        }
        else{
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
                PlaceLab.get(getActivity()).removeRowDbById(rowDBMustDelete);
                ((PlacePagerActivity) getActivity()).notifyPagerAdapter();
                dismiss();
                break;
            case R.id.negative_btn_id:
                dismiss();
                break;
            default:
                break;
        }
    }
}
