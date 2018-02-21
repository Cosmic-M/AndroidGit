package com.development.cosmic_m.navigator;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Cosmic_M on 25.09.2017.
 */

public class SharedPreferences {
    private static final String SWITCHER_SINGLE_EXECUTION = "com.development.cosmic_m.navigator";

    public static boolean getSharedPreferenceFlag(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SWITCHER_SINGLE_EXECUTION, false);
    }

    public static void setSharedPreferenceFlag(Context context, boolean flag){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(SWITCHER_SINGLE_EXECUTION, flag)
                .apply();
    }
}
