package com.development.cosmic_m.navigator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Cosmic_M on 16.09.2017.
 */

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcWidth > destWidth || srcHeight > destHeight){
            if (srcWidth > srcHeight){
                inSampleSize = Math.round(srcHeight / destHeight);
            }
            if (srcHeight > srcWidth){
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path, options);
    }
}
