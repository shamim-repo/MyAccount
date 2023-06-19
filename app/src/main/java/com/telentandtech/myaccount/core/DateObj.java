package com.telentandtech.myaccount.core;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DateObj {

    //Timestamp to long yyyyMMdd
    public static long dateToLong(Timestamp timestamp){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String dateString= simpleDateFormat.format(timestamp.getTime());
        dateString = dateString.replace("-","");
        return Long.parseLong(dateString);
    }

    //long yyyyMMdd to date string dd MMM yyyy
    public static String longToDateString(long date){
        String dateString=String.valueOf(date);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(dateString.substring(0,4)));
        calendar.set(Calendar.MONTH,Integer.parseInt(dateString.substring(4,6)));
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateString.substring(6,8)));
        return new SimpleDateFormat("dd MMM yyyy").format(calendar.getTime());
    }

    //Timestamp to long yyyyMMdd
    public static Long monthYearToLong(Timestamp timestamp){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
        String dateString= simpleDateFormat.format(timestamp.getTime());
        dateString = dateString.replace("-","");
        return Long.parseLong(dateString);
    }

    //long yyyyMM to month year string MMM yyyy
    public static String longToMonthYear(long date){
        String dateString=String.valueOf(date);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(dateString.substring(0,4)));
        calendar.set(Calendar.MONTH,Integer.parseInt(dateString.substring(4,6))-1);
        return new SimpleDateFormat("MMM yyyy").format(calendar.getTime());
    }

    //long to month year short string MMM yy
    public static String longToMonthYearShort(long date){
        String dateString=String.valueOf(date);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(dateString.substring(0,4)));
        calendar.set(Calendar.MONTH,Integer.parseInt(dateString.substring(4,6))-1);
        return new SimpleDateFormat("MMM yy").format(calendar.getTime());
    }

    //Timestamp to month year string MMM yyyy
    public static String timestampToMonthYearString(Timestamp date){
        return new SimpleDateFormat("MMM yyyy").format(date);
    }

    /*Timestamp to date string dd MMM yyyy
     */
    public static String timestampToDateString(Timestamp date){
        return new SimpleDateFormat("dd MMM yyyy").format(date);
    }

    /*long to mm
     */
    public static int longToMonth(long date) {
        String dateString = String.valueOf(date);
        return Integer.parseInt(dateString.substring(4, 6));
    }

    /*date range to list of month year long
     */
    public static long[] dateRangeToMonthYearList(long startDate, long endDate) {
        int startYear = Integer.parseInt(String.valueOf(startDate).substring(0, 4));
        int startMonth = Integer.parseInt(String.valueOf(startDate).substring(4, 6));
        int endYear = Integer.parseInt(String.valueOf(endDate).substring(0, 4));
        int endMonth = Integer.parseInt(String.valueOf(endDate).substring(4, 6));
        int totalMonth = (endYear - startYear) * 12 + (endMonth - startMonth) + 1;
        long[] monthYearList = new long[totalMonth];
        int i = 0;
        while (i < totalMonth) {
            monthYearList[i] = Long.parseLong(String.valueOf(startYear) + String.format("%02d", startMonth));
            if (startMonth == 12) {
                startMonth = 1;
                startYear++;
            } else {
                startMonth++;
            }
            i++;
        }
        return monthYearList;
    }
}
