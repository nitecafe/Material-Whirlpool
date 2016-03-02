package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.models.ScrapedPostList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;
import com.android.nitecafe.whirlpoolnews.utilities.ThreadScraper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import rx.observers.TestSubscriber;

@Ignore //need internet connection to run
@RunWith(MockitoJUnitRunner.class)
public class ThreadScraperTests {

    private ThreadScraper _threadScraper;

    @Before
    public void setUp() {
        _threadScraper = new ThreadScraper();
    }

    @Test
    public void scrapeThreadsFromForum_WhenCalled_ReturnResponse() {

        ScrapedThreadList scrapedThreads = _threadScraper.scrapeThreadsFromForum(106, 1, 0);

        Assert.assertNotNull(scrapedThreads.getThreads());
    }

    @Test
    public void scrapePostsFromThread_WhenCalled_ReturnResponse() throws IOException {

        ScrapedPostList scrapedPostList = _threadScraper.scrapePostsFromThread(2459226, "", 1);

        Assert.assertNotNull(scrapedPostList.getScrapedPosts());
    }

    @Test
    public void ScrapPopularThreadsObservable_WhenCalled_ReturnResponse() {

        //arrange
        TestSubscriber testSubscriber = new TestSubscriber();

        //act
        _threadScraper.ScrapPopularThreadsObservable().subscribe(testSubscriber);

        //assert
        testSubscriber.assertNoErrors();
        testSubscriber.onCompleted();
    }
}
