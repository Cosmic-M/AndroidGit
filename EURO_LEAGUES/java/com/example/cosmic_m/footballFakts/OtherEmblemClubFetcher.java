package com.example.cosmic_m.footballFakts;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.util.Log;

import com.applantation.android.svg.SVGParseException;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Cosmic_M on 04.06.2017.
 */

class OtherEmblemClubFetcher {
    SVG svg;
    private static final String TAG = "TAG";

    public Drawable getUrlBytes(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String redirect = connection.getHeaderField("Location");
        if (redirect != null) {
            connection = (HttpURLConnection) new URL(redirect).openConnection();
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "not valid link, -> " + link.toString());
                Log.i(TAG, "message: " + new Exception(connection.getResponseMessage()));
                throw new IOException(connection.getResponseMessage() + " with " + link);
            }
            try {
                svg = SVGParser.getSVGFromInputStream(in);
            }
            catch (NumberFormatException exc){
                Log.i(TAG, exc.getMessage());
                exc.printStackTrace();
            }
            catch (Exception exc){
                Log.i(TAG, exc.getMessage());
                exc.printStackTrace();
            }
            Drawable drawable;
            if (svg != null) {
                drawable = svg.createPictureDrawable();
            }
            else{
                drawable = null;
            }
            return drawable;
        }
        finally {
            connection.disconnect();
        }
    }
}

