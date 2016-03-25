package edu.byu.stringcheese.presenttime;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liukaichi on 3/25/2016.
 */
public class Utils {

    private static final String TAG = "Utils";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy");

    public static Calendar parseDate(String date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date.replaceAll("st|nd|rd|th", "")));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Your date wasn't parsed correctly");
        return null;
    }

    public static String stringifyDate(Calendar calendar)
    {
        String dayNumberSuffix = getDayNumberSuffix(calendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d'" + dayNumberSuffix + "', yyyy");
        return dateFormat.format(calendar.getTime());
    }


    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}
