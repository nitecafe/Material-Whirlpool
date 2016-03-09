package com.android.nitecafe.whirlpoolnews;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.Contact;
import com.android.nitecafe.whirlpoolnews.models.ContactList;
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.ForumThread;
import com.android.nitecafe.whirlpoolnews.models.ForumThreadList;
import com.android.nitecafe.whirlpoolnews.models.News;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.Poster;
import com.android.nitecafe.whirlpoolnews.models.Recent;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IThreadScraper;
import com.android.nitecafe.whirlpoolnews.web.WhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import retrofit.Retrofit;
import rx.Observable;
import rx.observers.TestObserver;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class WhirlpoolRestClientTests {

    @Mock SharedPreferences sharedPreferencesMock;
    @Mock IThreadScraper threadScraper;
    @Mock IPreferencesGetter preferencesGetterMock;
    TestableWhirlpoolRestClient whirlpoolRestClient;
    private Retrofit retrofit;
    private SharedPreferences.Editor editorMock;

    @Before
    public void setup() {
        retrofit = new Retrofit.Builder().baseUrl("http://www.google.com").build();
        editorMock = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(sharedPreferencesMock.getString(StringConstants.API_PREFERENCE_KEY, "")).thenReturn("111-111");
        Mockito.when(sharedPreferencesMock.getString(StringConstants.USERNAME, "")).thenReturn("Hello User");
        Mockito.when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        whirlpoolRestClient = new TestableWhirlpoolRestClient(retrofit, sharedPreferencesMock, threadScraper, preferencesGetterMock);
    }

    @Test
    public void GetNews_WhenCalled_ReturnResponse() throws Exception {

        //arrange
        final TestObserver<NewsList> newsListTestObserver = new TestObserver<>();
        final NewsList newsList = new NewsList();
        final News news = new News();
        news.setTITLE("AwesomeTitle");
        news.setBLURB("AwesomeBlurb");
        newsList.getNEWS().add(news);
        Mockito.when(whirlpoolRestClient.mWhirlpoolServiceMock.GetNews()).thenReturn(Observable.just(newsList));

        //act
        whirlpoolRestClient.GetNews().subscribe(newsListTestObserver);

        //assert
        final List<NewsList> onNextEvents = newsListTestObserver.getOnNextEvents();
        Assert.assertEquals(newsList, onNextEvents.get(0));
    }

    @Test
    public void GetForum_WhenCalled_ReturnResponse() {
        //arrange
        final TestObserver<ForumList> testObserver = new TestObserver<>();
        final ForumList forumList = new ForumList();
        final Forum forum = new Forum();
        forum.setTITLE("Broadband");
        forumList.getFORUM().add(forum);
        Mockito.when(whirlpoolRestClient.mWhirlpoolServiceMock.GetForum()).thenReturn(Observable.just(forumList));

        //act
        whirlpoolRestClient.GetForum().subscribe(testObserver);

        //assert
        List<ForumList> onNextEvents = testObserver.getOnNextEvents();
        Assert.assertEquals(forumList, onNextEvents.get(0));
    }

    @Test
    public void GetRecent_WhenCalled_ReturnResponse() {
        //arrange
        final TestObserver<RecentList> testObserver = new TestObserver<>();
        final RecentList recentList = new RecentList();
        final Recent recent = new Recent();
        recent.setTITLE("Big pond thread");
        recentList.getRECENT().add(recent);
        Mockito.when(whirlpoolRestClient.mWhirlpoolServiceMock.GetRecent()).thenReturn(Observable.just(recentList));

        //act
        whirlpoolRestClient.GetRecent().subscribe(testObserver);

        //assert
        List<RecentList> onNextEvents = testObserver.getOnNextEvents();
        Assert.assertEquals(recentList, onNextEvents.get(0));
    }

    @Test
    public void HasApiKeyBeenSet_WhenKeyIsEmpty_ReturnFalse() {

        //arrange
        Mockito.when(sharedPreferencesMock.getString(StringConstants.API_PREFERENCE_KEY, "")).thenReturn("");

        //act
        whirlpoolRestClient = new TestableWhirlpoolRestClient(retrofit, sharedPreferencesMock, threadScraper, preferencesGetterMock);

        //assert
        Assert.assertFalse(whirlpoolRestClient.hasApiKeyBeenSet());
    }

    @Test
    public void HasApiKeyBeenSet_WhenKeyIsSet_ReturnTrue() {

        //arrange
        Mockito.when(sharedPreferencesMock.getString(StringConstants.API_PREFERENCE_KEY, "")).thenReturn("1111-1111");

        //act
        whirlpoolRestClient = new TestableWhirlpoolRestClient(retrofit, sharedPreferencesMock, threadScraper, preferencesGetterMock);

        //assert
        Assert.assertTrue(whirlpoolRestClient.hasApiKeyBeenSet());
    }

    @Test
    public void GetThreads_WhenCalled_ReturnResponse() {
        //arrange
        final TestObserver<ForumThreadList> testObserver = new TestObserver<>();
        final ForumThreadList threadList = new ForumThreadList();
        final ForumThread thread = new ForumThread();
        thread.setTITLE("Big pond thread");
        threadList.getTHREADS().add(thread);
        Mockito.when(whirlpoolRestClient.mWhirlpoolServiceMock.GetThreads(1, 30)).thenReturn(Observable.just(threadList));

        //act
        whirlpoolRestClient.GetThreads(1, 30).subscribe(testObserver);

        //assert
        List<ForumThreadList> onNextEvents = testObserver.getOnNextEvents();
        Assert.assertEquals(threadList, onNextEvents.get(0));
    }

    @Test
    public void GetWatchedThreads_WhenCalled_ReturnResponse() {
        //arrange
        final TestObserver<WatchedList> testObserver = new TestObserver<>();
        final WatchedList watchedList = new WatchedList();
        final Watched watched = new Watched();
        watched.setTITLE("Watched thread");
        watchedList.getWATCHED().add(watched);
        Mockito.when(whirlpoolRestClient.mWhirlpoolServiceMock.GetWatched(0)).thenReturn(Observable.just(watchedList));

        //act
        whirlpoolRestClient.GetUnreadWatched().subscribe(testObserver);

        //assert
        List<WatchedList> onNextEvents = testObserver.getOnNextEvents();
        Assert.assertEquals(watchedList, onNextEvents.get(0));

    }

    @Test
    public void GetWhims_WhenCalled_ReturnResponse() {

        //arrange
        TestSubscriber<WhimsList> testSubscriber = new TestSubscriber<>();
        WhimsList whimsList = new WhimsList();
        whimsList.setWHIMS(Arrays.asList(new Whim()));
        when(whirlpoolRestClient.mWhirlpoolServiceMock.GetWhims()).thenReturn(Observable.just(whimsList));
        when(preferencesGetterMock.isHideMessageFromIgnoredContactsOn()).thenReturn(false);

        //act
        whirlpoolRestClient.GetWhims().subscribe(testSubscriber);

        //assert
        List<WhimsList> onNextEvents = testSubscriber.getOnNextEvents();
        Assert.assertEquals(whimsList, onNextEvents.get(0));
    }

    @Test
    public void MarkWhimAsRead_WhenCalled_RetrofitServiceCalled() {

        //arrange
        int whimId = 111;

        //act
        whirlpoolRestClient.MarkWhimAsRead(whimId);

        //assert
        Mockito.verify(whirlpoolRestClient.mWhirlpoolServiceMock).MarkWhimAsRead(whimId);
    }

    @Test
    public void GetPopularThreads_WhenCalled_ThreadScraperCalled() {

        //act
        whirlpoolRestClient.GetPopularThreads();

        //assert
        Mockito.verify(threadScraper).ScrapPopularThreadsObservable();
    }

    @Test
    public void GetUserDetails_WhenCalled_RetrofitServiceCalled() {
        //act
        whirlpoolRestClient.GetUserDetails();

        //assert
        Mockito.verify(whirlpoolRestClient.mWhirlpoolServiceMock).GetUserDetails();
    }

    @Test
    public void GetWhims_WhenFilterIsOff_ReturnAllWhims() {

        //arrange
        TestSubscriber<WhimsList> testSubscriber = new TestSubscriber<>();
        WhimsList whimsList = new WhimsList();
        Whim whim1 = new Whim();
        Poster allowedPoster = new Poster();
        allowedPoster.setID(1);
        allowedPoster.setNAME("Allowed");
        whim1.setFROM(allowedPoster);
        Whim whim2 = new Whim();
        Poster ignoredPoster = new Poster();
        ignoredPoster.setID(2);
        ignoredPoster.setNAME("Ignored");
        whim2.setFROM(ignoredPoster);
        whimsList.setWHIMS(Arrays.asList(whim1, whim2));
        ContactList contactLists = createContactLists();

        when(whirlpoolRestClient.mWhirlpoolServiceMock.GetWhims()).thenReturn(Observable.just(whimsList));
        when(preferencesGetterMock.isHideMessageFromIgnoredContactsOn()).thenReturn(false);
        when(whirlpoolRestClient.mWhirlpoolServiceMock.GetContacts()).thenReturn(Observable.just(contactLists));

        //act
        whirlpoolRestClient.GetWhims().subscribe(testSubscriber);

        //assert
        List<WhimsList> onNextEvents = testSubscriber.getOnNextEvents();
        Assert.assertEquals(2, onNextEvents.get(0).getWHIMS().size());
        Assert.assertEquals(whim1, onNextEvents.get(0).getWHIMS().get(0));
        Assert.assertEquals(whim2, onNextEvents.get(0).getWHIMS().get(1));
    }


    @Test
    public void GetWhims_WhenFilterIsOn_ReturnANonIgnoredMessages() {

        //arrange
        TestSubscriber<WhimsList> testSubscriber = new TestSubscriber<>();
        WhimsList whimsList = new WhimsList();
        Whim whim1 = new Whim();
        Poster allowedPoster = new Poster();
        allowedPoster.setID(1);
        allowedPoster.setNAME("Allowed");
        whim1.setFROM(allowedPoster);
        Whim whim2 = new Whim();
        Poster ignoredPoster = new Poster();
        ignoredPoster.setID(2);
        ignoredPoster.setNAME("Ignored");
        whim2.setFROM(ignoredPoster);
        whimsList.setWHIMS(Arrays.asList(whim1, whim2));
        ContactList contactLists = createContactLists();

        when(whirlpoolRestClient.mWhirlpoolServiceMock.GetWhims()).thenReturn(Observable.just(whimsList));
        when(preferencesGetterMock.isHideMessageFromIgnoredContactsOn()).thenReturn(true);
        when(whirlpoolRestClient.mWhirlpoolServiceMock.GetContacts()).thenReturn(Observable.just(contactLists));

        //act
        whirlpoolRestClient.GetWhims().subscribe(testSubscriber);

        //assert
        List<WhimsList> onNextEvents = testSubscriber.getOnNextEvents();
        Assert.assertEquals(1, onNextEvents.get(0).getWHIMS().size());
        Assert.assertEquals(whim1, onNextEvents.get(0).getWHIMS().get(0));
    }

    private ContactList createContactLists() {
        ContactList contactList = new ContactList();
        Contact contact1 = new Contact();
        contact1.setID(1);
        contact1.setBLOCKED(0);
        Contact contact2 = new Contact();
        contact2.setID(2);
        contact2.setBLOCKED(1);
        contactList.setCONTACTS(Arrays.asList(contact1, contact2));

        return contactList;
    }

}

class TestableWhirlpoolRestClient extends WhirlpoolRestClient {

    public IWhirlpoolService mWhirlpoolServiceMock;

    public TestableWhirlpoolRestClient(Retrofit retrofit, SharedPreferences sharedPreferences, IThreadScraper threadScraper, IPreferencesGetter preferencesGetter) {
        super(retrofit, sharedPreferences, threadScraper, preferencesGetter);

        mWhirlpoolServiceMock = Mockito.mock(IWhirlpoolService.class);
    }

    @Override
    protected IWhirlpoolService getWhirlpoolService() {
        return mWhirlpoolServiceMock;
    }

}