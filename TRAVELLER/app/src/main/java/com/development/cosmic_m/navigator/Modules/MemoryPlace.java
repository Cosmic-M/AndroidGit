package com.development.cosmic_m.navigator.Modules;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Cosmic_M on 14.09.2017.
 */

public class MemoryPlace implements Serializable{
    private UUID mId;
    private LatLng mLatLng;
    private Date mDate;
    private String mPath;
    private String mTextDescription;

    public MemoryPlace(LatLng latLng){
        this(UUID.randomUUID());
        mLatLng = latLng;
        mDate = new Date();
        mPath = "IMG" + getId().toString() + ".jpg";
    }

    private MemoryPlace(UUID id){
        mId = id;
    }

    public LatLng getLatLng(){
        return mLatLng;
    }

    public UUID getId(){
        return mId;
    }

    public Date getDate(){
        return mDate;
    }

    public String getPhotoFileName(){
        return "IMG" + getId().toString() + ".jpg";
    }

    public String getPhotoName(){
        return mPath;
    }

    public String getTextDescription(){
        return mTextDescription;
    }

    public void setTextDescription(String text){
        mTextDescription = text;
    }
}
