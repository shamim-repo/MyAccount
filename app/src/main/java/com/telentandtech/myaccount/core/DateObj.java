package com.telentandtech.myaccount.core;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DateObj {

    public static long dateToLong(Timestamp timestamp){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String dateString= simpleDateFormat.format(timestamp.getTime());
        dateString = dateString.replace("-","");
        return Long.parseLong(dateString);
    }

    //long to date
    public static String longToDateString(long date){
        String dateString=String.valueOf(date);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(dateString.substring(0,4)));
        calendar.set(Calendar.MONTH,Integer.parseInt(dateString.substring(4,6)));
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateString.substring(6,8)));
        return new SimpleDateFormat("dd MMM yyyy").format(calendar.getTime());
    }

    public static Long monthYearToLong(Timestamp timestamp){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
        String dateString= simpleDateFormat.format(timestamp.getTime());
        dateString = dateString.replace("-","");
        return Long.parseLong(dateString);
    }

    public static String longToMonthYear(long date){
        String dateString=String.valueOf(date);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(dateString.substring(0,4)));
        calendar.set(Calendar.MONTH,Integer.parseInt(dateString.substring(4,6))-1);
        return new SimpleDateFormat("MMM yyyy").format(calendar.getTime());
    }
    public static String timestampToMonthYearString(Timestamp date){
        return new SimpleDateFormat("MMM yyyy").format(date);
    }

}
