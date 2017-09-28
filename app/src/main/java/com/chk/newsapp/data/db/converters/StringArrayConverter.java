package com.chk.newsapp.data.db.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chira on 17-08-2017.
 */

public class StringArrayConverter {

    public static String strSeparator = "__,__";

    @TypeConverter
    public static String convertArrayToString(List<String> array) {
        String str = "";
        if (array != null) {
            int size = array.size();
            for (int i = 0; i < size; i++) {
                str = str + array.get(i);
                if (i < size - 1) {
                    str = str + strSeparator;
                }
            }
        }
        return str;
    }

    @TypeConverter
    public static List<String> convertStringToArray(String str) {
        if (str != null && !str.isEmpty()) {
            String[] arr = str.split(strSeparator);
            return Arrays.asList(arr);
        }
        return null;
    }
}
