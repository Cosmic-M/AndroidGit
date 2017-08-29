package com.project.cosmic_m.football_facts;

import android.graphics.drawable.Drawable;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Cosmic_M on 04.06.2017.
 * this class used library SVG other version
 */

class ExtraEmblemClubFetcher {
    SVG svg;
    //private static final String TAG = "TAG";

    public Drawable getUrlBytes(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String redirect = connection.getHeaderField("Location");
        if (redirect != null) {
            connection = (HttpURLConnection) new URL(redirect).openConnection();
        }

        try {
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                //Log.i(TAG, "message: " + new Exception(connection.getResponseMessage()));
                throw new IOException(connection.getResponseMessage() + " with " + link);
            }
            try {
                svg = SVGParser.getSVGFromInputStream(in);
            }
            catch (NumberFormatException exc){
                exc.printStackTrace();
            }
            catch (Exception exc){
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

