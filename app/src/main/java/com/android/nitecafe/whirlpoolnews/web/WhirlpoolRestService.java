package com.android.nitecafe.whirlpoolnews.web;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.ForumThreadList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPostList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;
import com.android.nitecafe.whirlpoolnews.models.UserDetailsList;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.utilities.ICachingUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class WhirlpoolRestService implements IWhirlpoolRestService {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private ICachingUtils cachingUtils;

    @Inject
    @Singleton
    public WhirlpoolRestService(IWhirlpoolRestClient whirlpoolRestClient,
                                ISchedulerManager schedulerManager,
                                ICachingUtils cachingUtils) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
        this.cachingUtils = cachingUtils;
    }

    @Override
    public Observable<NewsList> GetNews() {
        BehaviorSubject<NewsList> publishSubject = BehaviorSubject.create();
        NewsList newsCache = cachingUtils.getNewsCache();

        if (newsCache != null)
            publishSubject.onNext(newsCache);

        whirlpoolRestClient.GetNews()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .doOnNext(newsList -> cachingUtils.cacheNews(newsList))
                .subscribe(publishSubject);

        return publishSubject;
    }

    @Override
    public Observable<ArrayList<ScrapedThread>> GetPopularThreads() {
        BehaviorSubject<ArrayList<ScrapedThread>> publishSubject = BehaviorSubject.create();
        ArrayList<ScrapedThread> scrapedThreads = cachingUtils.getPopularThreadsCache();

        if (scrapedThreads != null)
            publishSubject.onNext(scrapedThreads);

        whirlpoolRestClient.GetPopularThreads()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .doOnNext(scrapedThreadArrayList -> cachingUtils.cachePopularThreads(scrapedThreadArrayList))
                .subscribe(publishSubject);

        return publishSubject;
    }

    @Override
    public Observable<ForumList> GetForum() {
        BehaviorSubject<ForumList> publishSubject = BehaviorSubject.create();
        ForumList forumList = cachingUtils.getForumCache();

        if (forumList != null)
            publishSubject.onNext(forumList);

        whirlpoolRestClient.GetForum()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .doOnNext(forums -> cachingUtils.cacheForum(forums))
                .subscribe(publishSubject);

        return publishSubject;
    }

    @Override
    public Observable<RecentList> GetRecent() {
        BehaviorSubject<RecentList> publishSubject = BehaviorSubject.create();
        RecentList recentList = cachingUtils.getRecentThreadsCache();

        if (recentList != null)
            publishSubject.onNext(recentList);

        whirlpoolRestClient.GetRecent()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .doOnNext(recents -> cachingUtils.cacheRecentThreads(recents))
                .subscribe(publishSubject);

        return publishSubject;
    }

    @Override
    public Observable<WatchedList> GetUnreadWatched() {
        BehaviorSubject<WatchedList> publishSubject = BehaviorSubject.create();
        WatchedList watchedList = cachingUtils.getUnreadWatchedThreadsCache();

        if (watchedList != null)
            publishSubject.onNext(watchedList);

        whirlpoolRestClient.GetUnreadWatched()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .doOnNext(unread -> cachingUtils.cacheUnreadWatchedThreads(unread))
                .subscribe(publishSubject);

        return publishSubject;
    }

    @Override
    public Observable<WatchedList> GetAllWatched() {
        BehaviorSubject<WatchedList> publishSubject = BehaviorSubject.create();
        WatchedList watchedList = cachingUtils.getAllWatchedThreadsCache();

        if (watchedList != null)
            publishSubject.onNext(watchedList);

        whirlpoolRestClient.GetAllWatched()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .doOnNext(all -> cachingUtils.cacheAllWatchedThreads(all))
                .subscribe(publishSubject);

        return publishSubject;
    }

    @Override
    public Observable<WhimsList> GetWhims() {
        BehaviorSubject<WhimsList> publishSubject = BehaviorSubject.create();
        WhimsList whimsList = cachingUtils.getWhimsCache();

        if (whimsList != null)
            publishSubject.onNext(whimsList);

        whirlpoolRestClient.GetWhims()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .doOnNext(whim -> cachingUtils.cacheWhims(whim))
                .subscribe(publishSubject);

        return publishSubject;
    }

    @Override
    public Observable<ForumThreadList> GetThreads(int forumIds, int threadCount) {
        return whirlpoolRestClient.GetThreads(forumIds, threadCount)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler());
    }

    @Override
    public Observable<ScrapedThreadList> GetScrapedThreads(int forumIds, int pageCount, int groupId) {
        return whirlpoolRestClient.GetScrapedThreads(forumIds, pageCount, groupId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler());
    }

    @Override
    public Observable<ScrapedPostList> GetScrapedPosts(int threadId, int page) {
        return whirlpoolRestClient.GetScrapedPosts(threadId, page)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler());
    }

    @Override
    public Observable<Void> SetThreadAsWatch(int threadId) {
        return whirlpoolRestClient.SetThreadAsWatch(threadId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler());
    }

    @Override
    public Observable<Void> SetThreadAsUnwatch(int threadId) {
        return whirlpoolRestClient.SetThreadAsUnwatch(threadId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler());
    }

    @Override
    public Observable<Void> MarkThreadAsRead(int threadId) {
        return whirlpoolRestClient.MarkThreadAsRead(threadId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler());
    }

    @Override
    public Observable<Void> MarkWhimAsRead(int whimId) {
        return whirlpoolRestClient.MarkWhimAsRead(whimId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler());
    }

    @Override
    public Observable<List<ScrapedThread>> SearchThreads(int forumId, int groupId, String query) {
        return whirlpoolRestClient.SearchThreads(forumId, groupId, query)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler());
    }

    @Override
    public Observable<UserDetailsList> GetUserDetails() {
        return whirlpoolRestClient.GetUserDetails()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler());
    }
}
