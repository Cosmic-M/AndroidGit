package com.development.cosmic_m.navigator;

import android.content.Context;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Cosmic_M on 16.09.2017.
 */

public class PlaceLab {

    private static final String TAG = "TAG";

    private static PlaceLab mPlaceLab;
    private List<MemoryPlace> list;
    private Context mContext;

    public static PlaceLab get(Context context){
        if (mPlaceLab == null){
            mPlaceLab = new PlaceLab(context);
        }
        return mPlaceLab;
    }

    private PlaceLab(Context context){
        mContext = context;
        list = new ArrayList<>();
        MemoryPlace mp;

        mp = new MemoryPlace(new LatLng(49.784349,36.579804));
        mp.setTextDescription("dike in Eschar");
        saveImageToFileSystem(context, mp, R.raw.plotina_eshar);
        list.add(mp);

        mp = new MemoryPlace(new LatLng(49.760011,36.670967));
        mp.setTextDescription("it's simply road through the wood. Just a nice place.");
        saveImageToFileSystem(context, mp, R.raw.malinovka_road);
        list.add(mp);

        mp = new MemoryPlace(new LatLng(49.614979,36.321349));
        mp.setTextDescription("abandoned children camp");
        saveImageToFileSystem(context, mp, R.raw.lager_romantik);
        list.add(mp);

        mp = new MemoryPlace(new LatLng(49.851276,36.822684));
        mp.setTextDescription("desert nearside the highway to Pechenegy lake");
        saveImageToFileSystem(context, mp, R.raw.dessert_in_kicevka);
        list.add(mp);

        mp = new MemoryPlace(new LatLng(49.675567,36.289629));
        mp.setTextDescription("the station of learning of ionosphere");
        saveImageToFileSystem(context, mp, R.raw.zmiyov_stancia_ionosferi);
        list.add(mp);
    }

    private void saveImageToFileSystem(Context context, MemoryPlace memoryPlace, int resource){
        InputStream in = context.getResources().openRawResource(resource);
        try {
            byte buffer[] = new byte[in.available()];
            in.read(buffer);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(buffer);

            File file = getPhotoFile(memoryPlace);
            FileOutputStream fos = new FileOutputStream(file);

            baos.writeTo(fos);
        }
        catch (FileNotFoundException exc){
            exc.getMessage();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMemoryPlace(MemoryPlace place){
        list.add(place);
    }

    public List<MemoryPlace> getMemoryPlacesList(){
        return list;
    }

    public File getPhotoFile(MemoryPlace place){
        File fileExternalDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (fileExternalDir == null){
            return null;
        }
        return new File(fileExternalDir, place.getPhotoFileName());
    }
}
