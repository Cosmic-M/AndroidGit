package com.development.cosmic_m.navigator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.development.cosmic_m.navigator.Database.MemoryPlaceBaseHelper;
import com.development.cosmic_m.navigator.Database.MemoryPlaceCursorWrapper;
import com.development.cosmic_m.navigator.Database.SchemaDB;
import com.development.cosmic_m.navigator.Modules.MemoryPlace;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cosmic_M on 16.09.2017.
 */

public class PlaceLab {
    private static PlaceLab mPlaceLab;
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
        mBase = new MemoryPlaceBaseHelper(context).getWritableDatabase();
        boolean flag = SharedPreferences.getSharedPreferenceFlag(context);
        if (!flag) {
            MemoryPlace mp;
            mp = new MemoryPlace(new LatLng(49.784349,36.579804));
            mp.setTextDescription("Одно из красивейших мест Харьковщины – плотина на реке Северский " +
                    "Донец, расположенная возле пгт Эсхар вниз по течению. На левом берегу реки находится пойменный лес. " +
                    "Прогуливаясь по лесу, можно увидеть вековые дубы и бобровые норы.");
            saveImageToFileSystem(context, mp, R.raw.plotina_eshar);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.760011,36.670967));
            mp.setTextDescription("Дорога к пгт Малиновка. Интересные факты из Википедии: по версии В.Н. " +
                    "Миславского, изложенной в справочнике «Харьков и кино», изданном в 2004 " +
                    "году, некоторые натуральные съемки фильма \"Свадьба в Малиновке\" проводились в Малиновке в " +
                    "Чугуевском районе. Но по более распространенному мнению, это было в Малиновке, что в " +
                    "Глобинском районе Полтавской области, а сам фильм снят в нескольких селах Лубенского района " +
                    "Полтавской области. В Малиновке исторически все поперечные улицы называются «сотнями» по " +
                    "номерам казачьих подразделений военного поселения: 1-я сотня, 6-я сотня. Всего 13 сотен + 14 " +
                    "– кладбище. А в танковом симуляторе World Of Tanks есть карта Малиновка.");
            saveImageToFileSystem(context, mp, R.raw.malinovka_road);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.614979,36.321349));
            mp.setTextDescription("Детский лагерь «Романтик» был построен в начале 70-х и является базой " +
                    "отдыха Харьковского приборостроительного завода им.  Шевченко – одного из крупнейших " +
                    "заводов в СССР по производству военной электроники и бытовой техники. Последнюю смену " +
                    "отдыхающих лагерь принимал в далеком 2004. С тех пор база дрейфует по течению времени: " +
                    "территория заросла бурьяном, постройки обветшали и начали осыпаться, а монументы " +
                    "на территории частично разрушены. Все это делает неплохую заявочку на декорации к съемкам " +
                    "продолжения Безумного Макса. В целом же отличное атмосферное место, которое стоит посетить, " +
                    "если еще не успели.");
            saveImageToFileSystem(context, mp, R.raw.lager_romantic);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.851276,36.822684));
            mp.setTextDescription(
                    "Кицевская пустыня ‒ очень необычное и живописное место. Когда-то в этих краях " +
                    "проводились танковые учения, о чем свидетельствуют нередкие находки танковых " +
                    "снарядов. Ну а сейчас здесь можно наслаждаться тишиной и отсутствием городской суеты.");
            saveImageToFileSystem(context, mp, R.raw.dessert_in_kicevka);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.675567,36.289629));
            mp.setTextDescription("Станция изучения ионосферы, г. Змиев. Практически перед самым развалом Советского Союза " +
                    "под Харьковом была построена станция ионосферных исследований, которая являлась прямым аналогом " +
                    "американского проекта HAARP на Аляске, успешно функционирующего и сегодня. Комплекс станции состоял " +
                    "из нескольких антенных полей и гигантской параболической антенны диаметром 25 метров, способной " +
                    "излучать мощность порядка 25 МВт. Но молодому украинскому государству передовая и весьма дорогостоящая " +
                    "научная аппаратура оказалась ни к чему, и когда-то секретной станцией ныне интересуются лишь сталкеры и " +
                    "охотники за цветными металлами. Ну и конечно, туристы. (из блога Ланы Сатор о действующих и заброшенных " +
                    "заводах и свалках техники) - https://lana-sator.livejournal.com/165269.html ...надеюсь, Лана не обидится");
            saveImageToFileSystem(context, mp, R.raw.zmiyov_stancia_ionosferi);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.555767,36.311048));
            mp.setTextDescription("Заезд в лес стартует из окраины с. Велика Гомольша. Далее указатели " +
                    "предложат нам несколько вариантов развития событий. Мы же выбираем недетское испытание - " +
                    "выдвинуться после водосбора в направлении Гайдар. По ходу движения не раз будет " +
                    "возникать неловкое чувство, что дорога вот-вот упрется в  заросли, не рассчитанные " +
                    "под наш транспорт. Местами встречаются поваленные поперек дороги деревья, подкрепляя " +
                    "то самое неловкое чувство, но поворачивать обратно уже поздно – мы проехали слишком " +
                    "далеко. Сразу оговорюсь, что при таких раскладах передвигаться по лесу вы будете не " +
                    "одни: несколько восьмилапых, которых вы подберете с кустов и деревьев, станут верными " +
                    "спутниками на время поездки через лес.");
            saveImageToFileSystem(context, mp, R.raw.gomilshansk);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.750397,36.537137));
            mp.setTextDescription("Суворовский источник, или, как его еще называют \"Руда криниця\" (просто в воде повышенное " +
                    "содержание железа, и это видно невооруженным глазом), находится в Змиевском районе Харьковской области, " +
                    "на берегу реки Северский Донец.");
            saveImageToFileSystem(context, mp, R.raw.suvorov_origin);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.658841,36.350675));
            mp.setTextDescription("Змиевские Кручи находятся вблизи южной окраины г. Змиева. Подножье \"Змиевских гор\" " +
                    "располагается непосредственно у реки Северский Донец");
            saveImageToFileSystem(context, mp, R.raw.zmiiv);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.5391071,36.4051505));
            mp.setTextDescription("Памятник 18 бойцам и командирам 1153-го стрелкового полка 343-й стрелковой дивизии у дороги в с. Нижний Бишкин.");
            saveImageToFileSystem(context, mp, R.raw.lower_bishkin);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.439806,36.683824));
            mp.setTextDescription("Шебелинский меловой карьер, словно большой каньон, привлекает " +
                            "туристов для осмотра величественного техногенного массива. Для жителей " +
                            "«Первой» столицы путь неблизкий, однако посещение этого великолепного " +
                            "и необычного места доставит большое удовольствие всем любителям экскурсионных " +
                            "вело- или мототуров.");
            saveImageToFileSystem(context, mp, R.raw.career_of_chalk);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.787693, 36.528683));
            mp.setTextDescription("Бывшая ракетная база ПВО (п. Эсхар) на сегодняшний день является " +
                            "абсолютно заброшенной");
            saveImageToFileSystem(context, mp, R.raw.rocket_base);
            insertPlaceIntoDB(mp);

            mp = new MemoryPlace(new LatLng(49.178752,37.277947));
            mp.setTextDescription("Геологический памятник природы и историческое ядро города, место, где " +
                    "река Северский Донец огибает огромный выступ верхнемеловых и юрских пород, а также " +
                    "наивысшая точка Харьковской области, а именно 218 м над уровнем моря... Кажется, все " +
                    "и так уже догадались, что речь идет о горе Кремянец. На склоне горы установлены " +
                    "собранные в окрестностях каменные истуканы изваяния. Согласно древней " +
                    "легенде, жившие в степи племена поклонялись богу-солнцу, но прогневали его и были " +
                    "превращены в камень. Кто не верит – может приехать и посмотреть: датированные " +
                    "серединой XII в., они дают представление, хотя и смутное (время все же наложило " +
                    "свой отпечаток), о внешнем виде кочевников, их одежде, украшениях и вооружении. " +
                    "На возвышенности сохранился вал и ров Изюмской крепости, построенной в 1681 г. для " +
                    "защиты южных границ России от набегов крымских татар. Крепость была одним из самых " +
                    "мощных защитных сооружений того времени и стала основой для закладки города. " +
                    "Другие памятники напоминают о событиях XX в. - Гражданской и Великой Отечественной " +
                    "войнах. В 1985 г. сооружен величественный мемориальный комплекс. Выставлена боевая " +
                    "техника периода Второй мировой войны: легендарная \"Катюша\", танки Т-34 и ИС-2, " +
                    "самоходные артиллерийские установки и др. Стоит отметить замечательную панораму на " +
                    "долину реки Северский Донец, что открывается с вершины горы. Здесь обязательно нужно " +
                    "побывать хотя бы раз в жизни!");
            saveImageToFileSystem(context, mp, R.raw.the_kremyanec_mountain);
            insertPlaceIntoDB(mp);

            SharedPreferences.setSharedPreferenceFlag(context, true);
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

    public int removeRowDbById(int id){
        return mBase.delete(SchemaDB.TABLE_NAME,
                SchemaDB.Cols.ID + " =? ", new String[]{String.valueOf(id)});
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

    public File getPhotoFile(MemoryPlace place){
        File fileExternalDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (fileExternalDir == null){
            return null;
        }
        return new File(fileExternalDir, place.getPhotoName());
    }
}
