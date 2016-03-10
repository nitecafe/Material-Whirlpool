package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WhirlpoolDateUtilsTests {

    @Test
    public void isTimeWithinDuration_isWithin_ReturnsTrue() {

        //arrange
        long interval = 60 * 1000;
        DateTime dateTime = new DateTime();
        String s = dateTime.toString("yyyy-MM-dd'T'HH:mm:ssZ");

        //act
        boolean timeWithinDuration = WhirlpoolDateUtils.isTimeWithinDurationJoda(s, interval);

        //assert
        Assert.assertTrue(timeWithinDuration);
    }

    @Test
    public void isTimeWithinDuration_isNotWithin_ReturnsFalse() {

        //arrange
        long interval = 60 * 1000;
        DateTime dateTime = new DateTime();
        DateTime deductedTime = dateTime.minusHours(2);
        String s = deductedTime.toString("yyyy-MM-dd'T'HH:mm:ssZ");

        //act
        boolean timeWithinDuration = WhirlpoolDateUtils.isTimeWithinDurationJoda(s, interval);

        //assert
        Assert.assertFalse(timeWithinDuration);
    }
}
