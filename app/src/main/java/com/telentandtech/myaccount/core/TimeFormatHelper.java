package com.telentandtech.myaccount.core;




import android.icu.text.SimpleDateFormat;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;


public class TimeFormatHelper {
    public static String getTimeForTextView(Timestamp startTime, Timestamp endTime){
        return getFormattedTime(startTime)+" - "+getFormattedTime(endTime);
    }
    //timestamp to string
    private static String getFormattedTime(Timestamp timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
        return formatter.format(new Date(timestamp.getTime()));
    }

    public static Timestamp hourMinuteToTimestamp(int hour,int minute){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR,hour);
        calendar.set(Calendar.MINUTE,minute);
        return new Timestamp(calendar.getTimeInMillis());
    }
    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("hh:mm").format(timestamp.getTime());
    }
    public static int getHour(Timestamp timestamp){
        return Integer.parseInt(new java.text.SimpleDateFormat("HH").format(timestamp.getTime()));
    }
    public static int getMinute(Timestamp timestamp){
        return Integer.parseInt(new java.text.SimpleDateFormat("mm").format(timestamp.getTime()));
    }
}
