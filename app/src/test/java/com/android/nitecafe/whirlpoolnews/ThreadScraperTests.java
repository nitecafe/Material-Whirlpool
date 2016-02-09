package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.utilities.ThreadScraper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class ThreadScraperTests {

    private ThreadScraper _threadScraper;

    @Before
    public void setUp() {
        _threadScraper = new ThreadScraper();
    }

    @Test
    public void scrapeThreadsFromForum_WhenCalled_ReturnResponse() {

        ArrayList<ScrapedThread> scrapedThreads = _threadScraper.scrapeThreadsFromForum(106, 1, 0);

        Assert.assertNotNull(scrapedThreads);
    }
}
