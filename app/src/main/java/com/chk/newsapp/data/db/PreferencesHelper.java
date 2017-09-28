package com.chk.newsapp.data.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.chk.newsapp.R;

/**
 * Created by chira on 04-09-2017.
 */

public class PreferencesHelper {

    public static final String SELECTED_SOURCE = "selected_source";

    private static PreferencesHelper ourInstance = null;
    private final SharedPreferences sharedPref;

    public static PreferencesHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new PreferencesHelper(context);
        }
        return ourInstance;
    }

    private PreferencesHelper(Context context) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public void write(String key, String source) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, source);
        editor.commit();
    }

    public String read(String key) {
        return sharedPref.getString(key, null);
    }
}
