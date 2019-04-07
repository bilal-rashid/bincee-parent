package com.bincee.parent.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {

    public static String help(String fromDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Baghdad"));
        Date date = null;

        try {
            date = dateFormat.parse(fromDate);//You will get date object relative to server/client timezone wherever it is parsed
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format(date);
    }

    public static String helpYear(String fromDate) {

        Date date = parse(fromDate);


        DateFormat formatter = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Baghdad"));

        if (date == null) {
            return null;
        } else {
            String dateStr = formatter.format(date);
            return dateStr;
        }
    }

    public static String format(Date date) {
        DateFormat formatter = new SimpleDateFormat("MMM-dd", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Baghdad"));
        if (date == null) {
            return null;
        } else {
            String dateStr = formatter.format(date);
            return dateStr;
        }

    }

    public static String toTime(Double duration) {
        double hours = duration / 60; //since both are ints, you get an int
        double minutes = duration % 60;

        return (int) hours + ":" + Math.round(minutes) + " Minutes";
    }

    public static Date parse(String toDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Baghdad"));

        Date date = null;

        try {
            date = dateFormat.parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }
}

