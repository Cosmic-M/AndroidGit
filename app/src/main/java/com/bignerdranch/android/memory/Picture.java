/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

public class Picture {
    private int mImageId;
    private String mCountry;
    private boolean mDecided;
    private boolean mOpen;

    public Picture(int imageId, String country){
        mCountry = country;
        mImageId = imageId;
    }

    public int getImageId() {
        return mImageId;
    }

    public boolean isDecided() {
        return mDecided;
    }

    public void setDecided(boolean decided) {
        mDecided = decided;
    }

    public boolean isOpen() {
        return mOpen;
    }

    public void setOpen(boolean open) {
        mOpen = open;
    }

    public String getCountry() {
        return mCountry;
    }
}
