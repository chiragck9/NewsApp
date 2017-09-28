package com.chk.newsapp.data.db.converters;

import android.arch.persistence.room.TypeConverter;

import com.chk.newsapp.data.datamodel.ArticleEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by chira on 17-08-2017.
 */

public class ArticleConverter {

    @TypeConverter
    public static List<ArticleEntity> fromString(String value) {
        Type listType = new TypeToken<List<ArticleEntity>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<ArticleEntity> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
