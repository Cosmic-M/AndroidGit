package com.development.cosmic_m.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;

/**
 * Created by Cosmic_M on 15.09.2017.
 */

public class AddPointActivity extends AppCompatActivity{
    private static final String TAG = "TAG";

    private static final int REQUEST_PHOTO = 100;

    private TextView mLatitude;
    private TextView mLongitude;
    private Button mAddPoint;
    private ImageButton mPhoto;
    private EditText mNotationText;
    private File mPhotoFile;
    private String et;
    private MemoryPlace mp;


    public static Intent newIntent(Context context){
        return new Intent(context, AddPointActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        mLatitude = (TextView) findViewById(R.id.tvLatitude);
        mLongitude = (TextView) findViewById(R.id.tvLongitude);
        mAddPoint = (Button) findViewById(R.id.btn_add_point);
        mPhoto = (ImageButton) findViewById(R.id.camera_image_id);
        mNotationText = (EditText) findViewById(R.id.et_notations);

        mLatitude.setText(String.valueOf(getIntent().getDoubleExtra("latitude", 0)));
        mLongitude.setText(String.valueOf(getIntent().getDoubleExtra("longitude", 0)));

        LatLng myLatLng = new LatLng(getIntent()
                .getDoubleExtra("latitude", 0) + 0.3, getIntent().getDoubleExtra("longitude", 0) + 0.3);

        PackageManager packageManager = getApplicationContext().getPackageManager();
        mp = new MemoryPlace(myLatLng);
        //PlaceLab.get(getApplicationContext()).addMemoryPlace(mp);

        mPhotoFile = PlaceLab.get(getApplicationContext()).getPhotoFile(mp);

        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && cameraIntent.resolveActivity(packageManager) != null;
        mPhoto.setEnabled(canTakePhoto);
        //PlaceLab.get(getApplicationContext()).insertPlaceIntoDB(mp);
        if (canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "startActivityForResult");
                startActivityForResult(cameraIntent, REQUEST_PHOTO);
            }
        });

        mAddPoint.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "activity finish");
                mp.setTextDescription(et);
                PlaceLab.get(getApplicationContext()).insertPlaceIntoDB(mp);
                finish();
            }
        });

        mNotationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et = s.toString();
            }
        });

        updatePhotoView();
    }

    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhoto.setImageResource(R.mipmap.ic_camera_icon);
        }
        else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), this);
            Log.i(TAG, "mPhotoFile.getPath() = " + mPhotoFile.getPath());
            mPhoto.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }
    }
}