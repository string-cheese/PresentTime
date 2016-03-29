package edu.byu.stringcheese.presenttime;

import android.os.AsyncTask;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.http.HttpResponse;

/**
 * Created by liukaichi on 3/25/2016.
 */
public class Utils {

    private static final String TAG = "Utils";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy");

    public static Calendar parseDate(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            String array[] = date.split(" ");
            array[1] = array[1].replaceAll("\\dst|nd|rd|th|,", "");

            Date calDate = dateFormat.parse(String.format("%s %s %s", array[0],array[1],array[2]));
            calendar.setTime(calDate);
            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Your date wasn't parsed correctly");
        return calendar;
    }

    public static String stringifyDate(Calendar calendar)
    {
        String dayNumberSuffix = getDayNumberSuffix(calendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d'" + dayNumberSuffix + "' yyyy");
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

    public static void searchItemAsync(String content, int max_items, ItemSearchListener itemSearchListener) {
        new ItemSearchAsync(itemSearchListener).execute(content, String.valueOf(max_items));
    }
}
