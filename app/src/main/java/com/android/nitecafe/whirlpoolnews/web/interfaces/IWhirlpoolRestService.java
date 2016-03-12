package com.android.nitecafe.whirlpoolnews.web.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ContactList;
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

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public interface IWhirlpoolRestService {
    Observable<NewsList> GetNews();

    Observable<ArrayList<ScrapedThread>> GetPopularThreads();

    Observable<ForumList> GetForum();

    Observable<RecentList> GetRecent();

    Observable<WatchedList> GetUnreadWatched();

    Observable<WatchedList> GetAllWatched();

    Observable<WhimsList> GetWhims();

    Observable<ForumThreadList> GetThreads(int forumIds, int threadCount);

    Observable<ScrapedThreadList> GetScrapedThreads(int forumIds, int pageCount, int groupId);

    Observable<ScrapedPostList> GetScrapedPosts(int threadId, int page);

    Observable<Void> SetThreadAsWatch(int threadId);

    Observable<Void> SetThreadAsUnwatch(int threadId);

    Observable<Void> MarkThreadAsRead(int threadId);

    Observable<Void> MarkWhimAsRead(int whimId);

    Observable<List<ScrapedThread>> SearchThreads(int forumId, int groupId, String query);

    Observable<UserDetailsList> GetUserDetails();

    Observable<ContactList> GetContacts();
}
