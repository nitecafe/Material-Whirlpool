package com.android.nitecafe.whirlpoolnews.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Credit to WhirlDroid for calculating time methods
 */
public class WhirlpoolDateUtils {

    /**
     * Calculates the date from a timestamp string
     * From http://stackoverflow.com/questions/8735214
     *
     * @param long_date_time Datetime string
     * @return Local date representation
     */
    public static Date getLocalDateFromString(String long_date_time) {
        // date format for the Whirlpool API
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS", Locale.US);

        long when = 0;
        try {
            // time, adjusting for AEST (Whirlpool default timezone)
            when = date_format.parse(long_date_time).getTime() - 10 * 60 * 60 * 1000;
        } catch (Exception e) {
            return null;
        }

        // adjust for daylight savings time (this sure looks confusing)
        return new Date(when + TimeZone.getDefault().getRawOffset() + (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0));
    }

    /**
     * Gets the time difference between now and a timestamp in seconds
     *
     * @param seconds Timestamp to get difference of
     * @return Difference, formatted in minutes, hours, days, etc.
     */
    public static String getTimeSince(long seconds) {
        long time = seconds;
        String time_text = "second";

        if (time >= 60) { // 1 minute or more ago
            time = time / 60;
            time_text = "minute";
        }
        if (time >= 60) { // 1 hour or more ago
            time = time / 60;
            time_text = "hour";
        }
        if (time >= 24 && time_text.equals("hour")) { // 1 day or more ago
            time = time / 24;
            time_text = "day";
        }
        if (time >= 7 && time_text.equals("day")) { // 1 week or more ago
            time = time / 7;
            time_text = "week";
        }

        if (time != 1) { // pluralise time text if time isn't 1
            time_text = time_text + "s";
        }

        return time + " " + time_text;
    }

    /**
     * Gets the time difference between now and a Date object
     *
     * @param date
     * @return Time difference
     */
    public static String getTimeSince(Date date) {
        long time = (System.currentTimeMillis() - date.getTime()) / 1000;
        return getTimeSince(time);
    }

}
