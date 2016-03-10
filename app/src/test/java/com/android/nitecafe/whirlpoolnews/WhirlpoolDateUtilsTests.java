package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WhirlpoolDateUtilsTests {

    @Test
    public void isTimeWithinDuration_isWithinHour_ReturnsTrue() {

        //arrange
        long interval = 60 * 1000;
        DateTime dateTime = new DateTime();
        String s = dateTime.toString("yyyy-MM-dd'T'HH:mm:ssZ");

        //act
        boolean timeWithinDuration = WhirlpoolDateUtils.isTimeWithinDuration(s, interval);

        //assert
        Assert.assertTrue(timeWithinDuration);
    }

    @Test
    public void isTimeWithinDuration_isWithinMinute_ReturnsTrue2() {

        //arrange
        long interval = 30 * 1000;
        DateTime dateTime = new DateTime();
        String s = dateTime.toString("yyyy-MM-dd'T'HH:mm:ssZ");

        //act
        boolean timeWithinDuration = WhirlpoolDateUtils.isTimeWithinDuration(s, interval);

        //assert
        Assert.assertTrue(timeWithinDuration);
    }

    @Test
    public void isTimeWithinDuration_isNotWithinHour_ReturnsFalse() {

        //arrange
        long interval = 60 * 1000;
        DateTime dateTime = new DateTime();
        DateTime deductedTime = dateTime.minusHours(2);
        String s = deductedTime.toString("yyyy-MM-dd'T'HH:mm:ssZ");

        //act
        boolean timeWithinDuration = WhirlpoolDateUtils.isTimeWithinDuration(s, interval);

        //assert
        Assert.assertFalse(timeWithinDuration);
    }

    @Test
    public void isTimeWithinDuration_isNotWithinMinute_ReturnsFalse() {

        //arrange
        long interval = 30 * 1000;
        DateTime dateTime = new DateTime();
        DateTime deductedTime = dateTime.minusMinutes(45);
        String s = deductedTime.toString("yyyy-MM-dd'T'HH:mm:ssZ");

        //act
        boolean timeWithinDuration = WhirlpoolDateUtils.isTimeWithinDuration(s, interval);

        //assert
        Assert.assertFalse(timeWithinDuration);
    }

    @Test
    public void getLocalDateFromString_whenDifferentZone_ReturnDateWithDefaultTimeZone() {

        //arrange
        String date = "2014-10-02T12:21:02+1000";
        String whim = "2016-02-28T12:26:16+1000";

        //act
        DateTime localDateFromString = WhirlpoolDateUtils.getLocalDateFromString(date);
        DateTime localDateFromString2 = WhirlpoolDateUtils.getLocalDateFromString(whim);

        //assert
        Assert.assertEquals(DateTimeZone.getDefault(), localDateFromString.getZone());
        Assert.assertEquals(DateTimeZone.getDefault(), localDateFromString2.getZone());
    }

    @Test
    public void getTimeSince_whenOneHourAgo_ReturnCorrectMessage() {

        //arrange
        DateTime date = new DateTime();
        DateTime dateTime = date.minusHours(1);

        //act
        String timeSince = WhirlpoolDateUtils.getTimeSince(dateTime);

        //assert
        Assert.assertEquals("1 hour", timeSince);
    }

    @Test
    public void getTimeSince_when5MinutesAgo_ReturnCorrectMessage() {

        //arrange
        DateTime date = new DateTime();
        DateTime dateTime = date.minusMinutes(5);

        //act
        String timeSince = WhirlpoolDateUtils.getTimeSince(dateTime);

        //assert
        Assert.assertEquals("5 minutes", timeSince);
    }

    @Test
    public void getTimeSince_when5DaysAgo_ReturnCorrectMessage() {

        //arrange
        DateTime date = new DateTime();
        DateTime dateTime = date.minusDays(5);

        //act
        String timeSince = WhirlpoolDateUtils.getTimeSince(dateTime);

        //assert
        Assert.assertEquals("5 days", timeSince);
    }

    @Test
    public void getTimeSince_when5YearsAgo_ReturnCorrectMessage() {

        //arrange
        DateTime date = new DateTime();
        DateTime dateTime = date.minusYears(5);

        //act
        String timeSince = WhirlpoolDateUtils.getTimeSince(dateTime);

        //assert
        Assert.assertEquals("261 weeks", timeSince);
    }
}
