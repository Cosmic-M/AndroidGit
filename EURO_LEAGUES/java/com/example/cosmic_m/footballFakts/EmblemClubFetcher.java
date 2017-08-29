package com.example.cosmic_m.footballFakts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.applantation.android.svg.SVG;
import com.applantation.android.svg.SVGParseException;
import com.applantation.android.svg.SVGParser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Cosmic_M on 30.05.2017.
 */

public class EmblemClubFetcher implements Callable<Map<String, Drawable>>{
    private static final String TAG = "TAG";
    private TeamStanding.Standing[] mStandings;

    EmblemClubFetcher(TeamStanding.Standing[] standings){
        mStandings = standings;
    }

    public Map<String, Drawable> call() throws IOException {
        Map<String, Drawable> emblems = new HashMap<>();
        label: for (int m = 0; m < mStandings.length; m++) {
            TeamStanding.Standing standing = mStandings[m];
            String link = standing.getResourceOfPicture();
            if (link.equals("null")){
                emblems.put(standing.getTeamId(), null);
                Log.i(TAG, "link is null");
                continue label;
            }
//            if (link.equals("http://en.wikipedia.org/wiki/Be%C5%9Fikta%C5%9F_J.K.#mediaviewer/File:Logo_of_Be%C5%9Fikta%C5%9F_JK.svg")){
//                link = "http://4.bp.blogspot.com/-EvuZhX8lIYo/U_KL3Y9PT3I/AAAAAAAADiw/XFvzs3SnfXo/s1600/Logo%2BBesiktas_JK.png";
//            }
//            if (link.equals("https://upload.wikimedia.org/wikipedia/commons/8/81/Borussia_M%C3%B6nchengladbach_logo.svg")){
//                link = "http://2.bp.blogspot.com/-a97xZ4elpg8/U_YOauGg09I/AAAAAAAADqo/PTlF6F7Drls/s1600/Logo%2BBorussia%2BMoenchengladbach.png";
//            }
            URL url = new URL(link);
            if (link.endsWith("svg")) {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                String redirect = connection.getHeaderField("Location");
                if (redirect != null) {
                    connection = (HttpURLConnection) new URL(redirect).openConnection();
                }

                try {
                    InputStream in = connection.getInputStream();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.i(TAG, "not valid link, -> " + link.toString());
                        Log.i(TAG, "message: " + new Exception(connection.getResponseMessage()));
                        throw new IOException(connection.getResponseMessage() + " with " + link);
                    }
                    SVG svg;
                    try {
                        svg = SVGParser.getSVGFromInputStream(in);
                    } catch (SVGParseException exc) {
                        Drawable drawable =  new OtherEmblemClubFetcher().getUrlBytes(link);
                        emblems.put(standing.getTeamId(), drawable);
                        continue label;
                    }
                    Drawable drawable = svg.createPictureDrawable();
                    emblems.put(standing.getTeamId(), drawable);
                    continue label;
                }
                catch(FileNotFoundException exc){
                    Log.i(TAG, exc.getMessage());
                    emblems.put(standing.getTeamId(), null);
                    exc.printStackTrace();
                }
                finally {
                    connection.disconnect();
                }
            } else {
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
                    int readBytes;
                    byte[] buffer = new byte[1024];
                    while ((readBytes = in.read(buffer)) > -1) {
                        out.write(buffer, 0, readBytes);
                    }
                    out.close();
                    byte[] bytesFromURL = out.toByteArray();
                    Bitmap bitmap = BitmapFactory
                            .decodeByteArray(bytesFromURL, 0, bytesFromURL.length);
                    Drawable drawable = new BitmapDrawable(bitmap);
                    emblems.put(standing.getTeamId(), drawable);
                    continue label;
                }
                catch(FileNotFoundException exc){
                    Log.i(TAG, exc.getMessage());
                    emblems.put(standing.getTeamId(), null);
                    exc.printStackTrace();
                }
                finally {
                    connection.disconnect();
                }
            }
        }
        return emblems;
    }
}
