package com.chk.newsapp.data.utils;

import com.chk.newsapp.data.datamodel.SourceEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by chira on 19-08-2017.
 */

public class CollectionsUtils {
    public static boolean isEmpty(Collection list) {
        return (list == null || list.isEmpty());
    }
}
