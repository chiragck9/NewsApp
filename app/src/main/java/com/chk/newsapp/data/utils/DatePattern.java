package com.chk.newsapp.data.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chira on 03-09-2017.
 */

public enum DatePattern {

    PUBLISHED("yyyy-MM-dd'T'HH:mm:ss");

    private final SimpleDateFormat simpleDateFormat;

    DatePattern(String format) {
        simpleDateFormat = new SimpleDateFormat(format);
    }

    public Date format(String date) throws ParseException {
        return simpleDateFormat.parse(date);
    }
}
