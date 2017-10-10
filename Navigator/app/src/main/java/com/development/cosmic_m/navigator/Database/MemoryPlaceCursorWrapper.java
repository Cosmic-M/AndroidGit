package com.development.cosmic_m.navigator.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.development.cosmic_m.navigator.Modules.MemoryPlace;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Cosmic_M on 24.09.2017.
 */

public class MemoryPlaceCursorWrapper extends CursorWrapper {

    public MemoryPlaceCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public MemoryPlace getPlace(){
        Double latitude = getDouble(getColumnIndex(SchemaDB.Cols.LATITUDE));
        Double longitude = getDouble(getColumnIndex(SchemaDB.Cols.LONGITUDE));
        String file_name = getString(getColumnIndex(SchemaDB.Cols.FILE_IMAGE_NAME));
        String description = getString(getColumnIndex(SchemaDB.Cols.DESCRIPTION));
        LatLng latLng = new LatLng(latitude, longitude);
        MemoryPlace mp = new MemoryPlace(latLng, file_name, description);
        return mp;
    }
}