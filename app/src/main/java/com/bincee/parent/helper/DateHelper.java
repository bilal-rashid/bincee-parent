package com.bincee.parent.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static String help(String fromDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = null;

        try {
            date = dateFormat.parse(fromDate);//You will get date object relative to server/client timezone wherever it is parsed
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("MMM-dd");
        if (date == null) {
            return null;
        } else {
            String dateStr = formatter.format(date);
            return dateStr;
        }
    }

    public static String helpYear(String fromDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = null;

        try {
            date = dateFormat.parse(fromDate);//You will get date object relative to server/client timezone wherever it is parsed
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("yyyy");
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

        return hours + ":" + minutes + " Minutes";
    }
}

