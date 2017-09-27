package com.development.cosmic_m.navigator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.development.cosmic_m.navigator.Database.MemoryPlaceBaseHelper;
import com.development.cosmic_m.navigator.Database.MemoryPlaceCursorWrapper;
import com.development.cosmic_m.navigator.Database.SchemaDB;
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
    //private List<MemoryPlace> list;
    private Context mContext;
    private SQLiteDatabase mBase;

    public static PlaceLab get(Context context){
        if (mPlaceLab == null){
            mPlaceLab = new PlaceLab(context);
        }
        return mPlaceLab;
    }

    private PlaceLab(Context context){
        mContext = context;
        //list = new ArrayList<>();
        mBase = new MemoryPlaceBaseHelper(context).getWritableDatabase();
        boolean flag = SharedPreferences.getSharedPreferenceFlag(context);
        Log.i(TAG, "FLAG = " + flag);
        if (!flag) {
            MemoryPlace mp;
            mp = new MemoryPlace(new LatLng(49.784349, 36.579804));
            mp.setTextDescription("Одно из красивейших мест Харьковщины – плотина на реке Северский " +
                    "Донец, расположенная близь пгт Эсхар вниз по течению. Стоит упомянуть так же о " +
                    "еще одной достопримечательности – пойменный лес, который находится на левом берегу. " +
                    "Прогуливаясь по лесу можно увидеть вековые дубы или бобровые норы.");
            saveImageToFileSystem(context, mp, R.raw.plotina_eshar);
            //list.add(mp);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.760011, 36.670967));
            mp.setTextDescription("it's simply road through the wood. Just a nice place.");
            saveImageToFileSystem(context, mp, R.raw.malinovka_road);
            //list.add(mp);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.614979, 36.321349));
            mp.setTextDescription("Детский лагерь «Романтик» был построен в начале 70х и является базой " +
                    "отдыха Харьковского Приборостроительного завода им.  Шевченко – одного из крупнейших " +
                    "заводов в СССР по производству военной электроники и бытовой техники. Последнюю смену " +
                    "отдыхающих лагерь принимал в далеком 2004. С тех пор база дрейфует по течению времени: " +
                    "территория заросла бурьяном, постройки обветшали и начали осыпаться, а нередкие монументы " +
                    "на территории частично разрушены. Побродив по территории, неволей переносишься мыслями в " +
                    "постапокалиптический мир безумного Макса, короче советую к посещению всем заядлым вело и " +
                    "мото открывателям…");
            saveImageToFileSystem(context, mp, R.raw.lager_romantik);
            //list.add(mp);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.851276, 36.822684));
            mp.setTextDescription(
                    "Кицевская пустыня ‒ очень необычное и живописное место. Когда-то в этих местах " +
                            "проводились танковые учения, о чем свидетельствуют не редкие находки танковых " +
                            "снарядов. Ну а сейчас, в этом безмолвном песчаном царстве наслаждаешься тишиной " +
                            "и отсутствием привычной городской суеты. А еще здесь получаются замечательные " +
                            "фотографии!"
            );
            saveImageToFileSystem(context, mp, R.raw.dessert_in_kicevka);
            //list.add(mp);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.675567, 36.289629));
            mp.setTextDescription("the station of learning of ionosphere");
            saveImageToFileSystem(context, mp, R.raw.zmiyov_stancia_ionosferi);
            //list.add(mp);

            mp = new MemoryPlace(new LatLng(49.555767, 36.311048));
            mp.setTextDescription("Заезд в лес стартует из окраины с. Велика Гомольша. Далее указатели " +
                    "нам будут предлагать несколько вариантов развития событий: не детское испытание " +
                    "выдвинуться после водосбора в направлении Гайдар. По ходу движения не раз будет " +
                    "возникать неловкое чувство, что дорога вот-вот упрется в  заросли не рассчитанные " +
                    "под наш транспорт, местами, встречаются поваленные поперек дороги деревья, подкрепляя " +
                    "то самое неловкое чувство, но поворачивать обратно уже поздно – мы проехали слишком " +
                    "далеко. Сразу оговорюсь, что при таких раскладах передвигаться по лесу Вы будете не " +
                    "одни: несколько восьмилапых, которых Вы подберете с кустов и деревьев будут верными " +
                    "спутниками на время поездки через лес.");
            saveImageToFileSystem(context, mp, R.raw.gomilshansk);
            //list.add(mp);
            insertPlaceIntoDB(mp);

            SharedPreferences.setSharedPreferenceFlag(context, true);
            Log.i(TAG, "FLAG = " + flag);
        }
    }

    private MemoryPlaceCursorWrapper getCursorWrapper(String name, String clause, String[] args){
        Cursor cursor = mBase.query(
                name,
                null,
                clause,
                args,
                null,
                null,
                null
        );
        return new MemoryPlaceCursorWrapper(cursor);
    }

    private ContentValues getContentValues(MemoryPlace mp){
        ContentValues cv = new ContentValues();
        cv.put(SchemaDB.Cols.LATITUDE, mp.getLatLng().latitude);
        cv.put(SchemaDB.Cols.LONGITUDE, mp.getLatLng().longitude);
        cv.put(SchemaDB.Cols.FILE_IMAGE_NAME, mp.getPhotoFileName());
        cv.put(SchemaDB.Cols.DESCRIPTION, mp.getTextDescription());
        return cv;
    }

    public void insertPlaceIntoDB(MemoryPlace mp){
        ContentValues values = getContentValues(mp);
        mBase.insert(SchemaDB.TABLE_NAME, null, values);
    }

    public List<MemoryPlace> getMemoryPlace(){
        MemoryPlace memoryPlace;
        List<MemoryPlace> listPlaces = new ArrayList<>();
        MemoryPlaceCursorWrapper cursor = getCursorWrapper(SchemaDB.TABLE_NAME, null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.i(TAG, "while...add(memoryPlace)");
                memoryPlace = cursor.getPlace();
                listPlaces.add(memoryPlace);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return listPlaces;
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

//    public void addMemoryPlace(MemoryPlace place){
//        list.add(place);
//    }

//    public List<MemoryPlace> getMemoryPlacesList(){
//        return list;
//    }

    public File getPhotoFile(MemoryPlace place){
        File fileExternalDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (fileExternalDir == null){
            return null;
        }
        return new File(fileExternalDir, place.getPhotoName());
    }
}
