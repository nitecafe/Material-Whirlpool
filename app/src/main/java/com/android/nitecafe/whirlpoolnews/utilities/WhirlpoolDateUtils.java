package com.android.nitecafe.whirlpoolnews.utilities;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Seconds;

public class WhirlpoolDateUtils {

    public static DateTime getLocalDateFromString(String long_date_time) {
        DateTime parse = DateTime.parse(long_date_time);
        return parse.withZone(DateTimeZone.getDefault());
    }

    /**
     * Gets the time difference between now and a timestamp in seconds
     *
     * @param seconds Timestamp to get difference of
     * @return Difference, formatted in minutes, hours, days, etc.
     */
    private static String getTimeSinceWithSeconds(long seconds) {
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

    public static String getTimeSince(DateTime date) {
        DateTime now = new DateTime();
        long time = Seconds.secondsBetween(date, now).getSeconds();
        return getTimeSinceWithSeconds(time);
    }

    public static boolean isTimeWithinDuration(String date, long duration) {
        DateTime localDateFromString = getLocalDateFromString(date);
        DateTime dateTime = new DateTime();
        long l = dateTime.getMillis() - localDateFromString.getMillis();
        return l < duration;
    }
}
