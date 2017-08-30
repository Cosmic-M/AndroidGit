package com.project.cosmic_m.football_facts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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
                continue label;
            }
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
                        throw new IOException(connection.getResponseMessage() + " with " + link);
                    }
                    SVG svg;
                    try {
                        svg = SVGParser.getSVGFromInputStream(in);
                    } catch (SVGParseException exc) {
                        Drawable drawable =  new ExtraEmblemClubFetcher().getUrlBytes(link);
                        emblems.put(standing.getTeamId(), drawable);
                        continue label;
                    }
                    Drawable drawable = svg.createPictureDrawable();
                    emblems.put(standing.getTeamId(), drawable);
                    continue label;
                }
                catch(FileNotFoundException exc){
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
