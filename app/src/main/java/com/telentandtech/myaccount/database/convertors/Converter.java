package com.telentandtech.myaccount.database.convertors;

import androidx.room.TypeConverter;

import java.sql.Timestamp;

public class Converter {
    @TypeConverter
    public static Timestamp fromTimestamp(Long value) {
        return value == null ? null : new Timestamp(value);
    }

    @TypeConverter
    public static Long lon(Timestamp date) {
        return date == null ? null : date.getTime();
    }
}
