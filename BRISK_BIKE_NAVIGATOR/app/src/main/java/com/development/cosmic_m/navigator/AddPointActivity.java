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
import android.widget.ImageView;
import android.widget.TextView;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;

/**
 * Created by Cosmic_M on 15.09.2017.
 */

public class AddPointActivity extends AppCompatActivity{

    private static final int REQUEST_PHOTO = 100;
    private static final String PHOTO_FILE = "photo_file";

    private TextView mLatitude;
    private TextView mLongitude;
    private ImageButton mCameraButton;
    private Button mAddPoint;
    private ImageView mPhoto;
    private EditText mNotationText;
    private File mPhotoFile;
    private String et;
    private MemoryPlace mp;


    public static Intent newIntent(Context context){
        return new Intent(context, AddPointActivity.class);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putSerializable(PHOTO_FILE, mPhotoFile);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        mLatitude = (TextView) findViewById(R.id.tvLatitude);
        mLongitude = (TextView) findViewById(R.id.tvLongitude);
        mAddPoint = (Button) findViewById(R.id.btn_add_point);
        mPhoto = (ImageView) findViewById(R.id.image_id);
        mNotationText = (EditText) findViewById(R.id.et_notations);
        mCameraButton = (ImageButton) findViewById(R.id.camera_id);
        LatLng latLng = getIntent().getParcelableExtra("latlng");
        mLatitude.setText(String.valueOf(latLng.latitude));
        mLongitude.setText(String.valueOf(latLng.longitude));
        PackageManager packageManager = getApplicationContext().getPackageManager();
        mp = new MemoryPlace(latLng);
        if (savedInstanceState != null){
            mPhotoFile = (File) savedInstanceState.getSerializable(PHOTO_FILE);
        }
        else {
            mPhotoFile = PlaceLab.get(getApplicationContext()).getPhotoFile(mp);
        }

        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && cameraIntent.resolveActivity(packageManager) != null;
        mPhoto.setEnabled(canTakePhoto);
        if (canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(cameraIntent, REQUEST_PHOTO);
            }
        });

        mAddPoint.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp.setTextDescription(et);
                PlaceLab.get(getApplicationContext()).insertPlaceIntoDB(mp);
                setResult(RESULT_OK);
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
        }
        else{
            mPhoto.setVisibility(View.VISIBLE);
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), this);
            mPhoto.setMaxHeight(100);
            mPhoto.setMaxWidth(100);
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