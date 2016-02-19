package com.android.nitecafe.whirlpoolnews;


import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.utilities.ICachingUtils;
import com.android.nitecafe.whirlpoolnews.web.WhirlpoolRestService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WhirlpoolRestServiceTests {

    @Mock IWhirlpoolRestClient whirlpoolRestClientMock;
    @Mock ICachingUtils cachingUtilsMock;
    private WhirlpoolRestService whirlpoolRestService;
    private TestSchedulerManager testSchedulerManager;

    @Before
    public void Setup() {
        testSchedulerManager = new TestSchedulerManager();
        whirlpoolRestService = new WhirlpoolRestService(whirlpoolRestClientMock, testSchedulerManager, cachingUtilsMock);
    }

    @Test
    public void GetNews_WhenCalled_CallRestClient() {

        //arrange
        when(whirlpoolRestClientMock.GetNews()).thenReturn(Observable.<NewsList>empty());

        //act
        whirlpoolRestService.GetNews();

        //assert
        verify(whirlpoolRestClientMock).GetNews();
    }

    @Test
    public void GetNews_CacheExist_ReturnCache() {

        //arrange
        TestSubscriber subscriber = new TestSubscriber();
        NewsList newsList = new NewsList();
        when(cachingUtilsMock.getNewsCache()).thenReturn(newsList);
        when(whirlpoolRestClientMock.GetNews()).thenReturn(Observable.<NewsList>empty());

        //act
        whirlpoolRestService.GetNews().subscribe(subscriber);

        //assert
        subscriber.assertReceivedOnNext(Arrays.asList(newsList));
    }

    @Test
    public void GetNews_WhenCalled_SavesCache() {

        //arrange
        NewsList newsList = new NewsList();
        when(whirlpoolRestClientMock.GetNews()).thenReturn(Observable.just(newsList));

        //act
        whirlpoolRestService.GetNews();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        verify(cachingUtilsMock).cacheNews(newsList);
    }

    @Test
    public void GetPopularThreads_WhenCalled_CallRestClient() {

        //arrange
        when(whirlpoolRestClientMock.GetPopularThreads()).thenReturn(Observable.<ArrayList<ScrapedThread>>empty());

        //act
        whirlpoolRestService.GetPopularThreads();

        //assert
        verify(whirlpoolRestClientMock).GetPopularThreads();
    }

    @Test
    public void GetPopularThreads_CacheExist_ReturnCache() {

        //arrange
        TestSubscriber subscriber = new TestSubscriber();
        ArrayList<ScrapedThread> scrapedThreads = new ArrayList<>();
        when(cachingUtilsMock.getPopularThreadsCache()).thenReturn(scrapedThreads);
        when(whirlpoolRestClientMock.GetPopularThreads()).thenReturn(Observable.<ArrayList<ScrapedThread>>empty());

        //act
        whirlpoolRestService.GetPopularThreads().subscribe(subscriber);

        //assert
        subscriber.assertReceivedOnNext(Arrays.asList(scrapedThreads));
    }

    @Test
    public void GetPopularThreads_WhenCalled_SavesCache() {

        //arrange
        ArrayList<ScrapedThread> scrapedThreads = new ArrayList<>();
        when(whirlpoolRestClientMock.GetPopularThreads()).thenReturn(Observable.just(scrapedThreads));

        //act
        whirlpoolRestService.GetPopularThreads();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        verify(cachingUtilsMock).cachePopularThreads(scrapedThreads);
    }

    @Test
    public void GetForum_WhenCalled_CallRestClient() {

        //arrange
        when(whirlpoolRestClientMock.GetForum()).thenReturn(Observable.<ForumList>empty());

        //act
        whirlpoolRestService.GetForum();

        //assert
        verify(whirlpoolRestClientMock).GetForum();
    }

    @Test
    public void GetForum_CacheExist_ReturnCache() {

        //arrange
        TestSubscriber subscriber = new TestSubscriber();
        ForumList forumList = new ForumList();
        when(cachingUtilsMock.getForumCache()).thenReturn(forumList);
        when(whirlpoolRestClientMock.GetForum()).thenReturn(Observable.<ForumList>empty());

        //act
        whirlpoolRestService.GetForum().subscribe(subscriber);

        //assert
        subscriber.assertReceivedOnNext(Arrays.asList(forumList));
    }

    @Test
    public void GetForum_WhenCalled_SavesCache() {

        //arrange
        ForumList forumList = new ForumList();
        when(whirlpoolRestClientMock.GetForum()).thenReturn(Observable.just(forumList));

        //act
        whirlpoolRestService.GetForum();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        verify(cachingUtilsMock).cacheForum(forumList);
    }

    @Test
    public void GetRecent_WhenCalled_CallRestClient() {

        //arrange
        when(whirlpoolRestClientMock.GetRecent()).thenReturn(Observable.<RecentList>empty());

        //act
        whirlpoolRestService.GetRecent();

        //assert
        verify(whirlpoolRestClientMock).GetRecent();
    }

    @Test
    public void GetRecent_CacheExist_ReturnCache() {

        //arrange
        TestSubscriber subscriber = new TestSubscriber();
        RecentList recentList = new RecentList();
        when(cachingUtilsMock.getRecentThreadsCache()).thenReturn(recentList);
        when(whirlpoolRestClientMock.GetRecent()).thenReturn(Observable.<RecentList>empty());

        //act
        whirlpoolRestService.GetRecent().subscribe(subscriber);

        //assert
        subscriber.assertReceivedOnNext(Arrays.asList(recentList));
    }

    @Test
    public void GetRecent_WhenCalled_SavesCache() {

        //arrange
        RecentList recentList = new RecentList();
        when(whirlpoolRestClientMock.GetRecent()).thenReturn(Observable.just(recentList));

        //act
        whirlpoolRestService.GetRecent();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        verify(cachingUtilsMock).cacheRecentThreads(recentList);
    }

    @Test
    public void GetUnread_WhenCalled_CallRestClient() {

        //arrange
        when(whirlpoolRestClientMock.GetUnreadWatched()).thenReturn(Observable.<WatchedList>empty());

        //act
        whirlpoolRestService.GetUnread();

        //assert
        verify(whirlpoolRestClientMock).GetUnreadWatched();
    }

    @Test
    public void GetUnread_CacheExist_ReturnCache() {

        //arrange
        TestSubscriber subscriber = new TestSubscriber();
        WatchedList watchedList = new WatchedList();
        when(cachingUtilsMock.getUnreadWatchedThreadsCache()).thenReturn(watchedList);
        when(whirlpoolRestClientMock.GetUnreadWatched()).thenReturn(Observable.<WatchedList>empty());

        //act
        whirlpoolRestService.GetUnread().subscribe(subscriber);

        //assert
        subscriber.assertReceivedOnNext(Arrays.asList(watchedList));
    }

    @Test
    public void GetUnread_WhenCalled_SavesCache() {

        //arrange
        WatchedList watchedList = new WatchedList();
        when(whirlpoolRestClientMock.GetUnreadWatched()).thenReturn(Observable.just(watchedList));

        //act
        whirlpoolRestService.GetUnread();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        verify(cachingUtilsMock).cacheUnreadWatchedThreads(watchedList);
    }

    //all
    @Test
    public void GetAllWatched_WhenCalled_CallRestClient() {

        //arrange
        when(whirlpoolRestClientMock.GetAllWatched()).thenReturn(Observable.<WatchedList>empty());

        //act
        whirlpoolRestService.GetAllWatched();

        //assert
        verify(whirlpoolRestClientMock).GetAllWatched();
    }

    @Test
    public void GetAllWatched_CacheExist_ReturnCache() {

        //arrange
        TestSubscriber subscriber = new TestSubscriber();
        WatchedList watchedList = new WatchedList();
        when(cachingUtilsMock.getAllWatchedThreadsCache()).thenReturn(watchedList);
        when(whirlpoolRestClientMock.GetAllWatched()).thenReturn(Observable.<WatchedList>empty());

        //act
        whirlpoolRestService.GetAllWatched().subscribe(subscriber);

        //assert
        subscriber.assertReceivedOnNext(Arrays.asList(watchedList));
    }

    @Test
    public void GetAllWatched_WhenCalled_SavesCache() {

        //arrange
        WatchedList watchedList = new WatchedList();
        when(whirlpoolRestClientMock.GetAllWatched()).thenReturn(Observable.just(watchedList));

        //act
        whirlpoolRestService.GetAllWatched();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        verify(cachingUtilsMock).cacheAllWatchedThreads(watchedList);
    }

    //whim
    @Test
    public void GetWhims_WhenCalled_CallRestClient() {

        //arrange
        when(whirlpoolRestClientMock.GetWhims()).thenReturn(Observable.<WhimsList>empty());

        //act
        whirlpoolRestService.GetWhims();

        //assert
        verify(whirlpoolRestClientMock).GetWhims();
    }

    @Test
    public void GetWhims_CacheExist_ReturnCache() {

        //arrange
        TestSubscriber subscriber = new TestSubscriber();
        WhimsList whimsList = new WhimsList();
        when(cachingUtilsMock.getWhimsCache()).thenReturn(whimsList);
        when(whirlpoolRestClientMock.GetWhims()).thenReturn(Observable.<WhimsList>empty());

        //act
        whirlpoolRestService.GetWhims().subscribe(subscriber);

        //assert
        subscriber.assertReceivedOnNext(Arrays.asList(whimsList));
    }

    @Test
    public void GetWhims_WhenCalled_SavesCache() {

        //arrange
        WhimsList whimsList = new WhimsList();
        when(whirlpoolRestClientMock.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        whirlpoolRestService.GetWhims();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        verify(cachingUtilsMock).cacheWhims(whimsList);
    }
}
