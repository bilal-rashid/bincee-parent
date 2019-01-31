package com.bincee.parent.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static String help(String fromDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = null;

        try {
            date = dateFormat.parse(fromDate);//You will get date object relative to server/client timezone wherever it is parsed
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            return null;
        } else {
            String dateStr = formatter.format(date);
            return dateStr;
        }
    }
}

