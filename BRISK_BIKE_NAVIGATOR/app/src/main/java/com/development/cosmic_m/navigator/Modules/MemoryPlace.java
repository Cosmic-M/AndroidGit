package com.development.cosmic_m.navigator.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Cosmic_M on 14.09.2017.
 */

public class MemoryPlace implements Serializable, Parcelable{
    private int _id;
    private UUID mId;
    private LatLng mLatLng;
    private String mFileName;
    private String mTextDescription;

    public MemoryPlace(int id, LatLng latLng, String fileName, String description){
        _id = id;
        mLatLng = latLng;
        mFileName = fileName;
        mTextDescription = description;
    }

    public MemoryPlace(LatLng latLng){
        this(UUID.randomUUID());
        mLatLng = latLng;
        mFileName = "IMG" + getId().toString() + ".jpg";
    }

    private MemoryPlace(UUID id){
        mId = id;
    }

    protected MemoryPlace(Parcel in) {
        _id = in.readInt();
        mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        mFileName = in.readString();
        mTextDescription = in.readString();
    }

    public static final Creator<MemoryPlace> CREATOR = new Creator<MemoryPlace>() {
        @Override
        public MemoryPlace createFromParcel(Parcel in) {
            return new MemoryPlace(in);
        }

        @Override
        public MemoryPlace[] newArray(int size) {
            return new MemoryPlace[size];
        }
    };

    public LatLng getLatLng(){
        return mLatLng;
    }

    public UUID getId(){
        return mId;
    }

    public int getIdRowDb() {
        return _id;
    }

    public String getPhotoFileName(){
        return "IMG" + getId().toString() + ".jpg";
    }

    public String getPhotoName(){
        return mFileName;
    }

    public String getTextDescription(){
        return mTextDescription;
    }

    public void setTextDescription(String text){
        mTextDescription = text;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeParcelable(mLatLng, flags);
        dest.writeString(mFileName);
        dest.writeString(mTextDescription);
    }
}
