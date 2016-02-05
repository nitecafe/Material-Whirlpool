package com.android.nitecafe.whirlpoolnews;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolService;
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.News;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.Recent;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.web.WhirlpoolRestClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import retrofit.Retrofit;
import rx.Observable;
import rx.observers.TestObserver;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class WhirlpoolRestClientTests {

    @Mock SharedPreferences sharedPreferencesMock;
    TestableWhirlpoolRestClient whirlpoolRestClient;
    private Retrofit retrofit;

    @Before
    public void setup() {
        retrofit = new Retrofit.Builder().baseUrl("http://www.google.com").build();
        Mockito.when(sharedPreferencesMock.getString(StringConstants.API_PREFERENCE_KEY, "")).thenReturn("111-111");
        whirlpoolRestClient = new TestableWhirlpoolRestClient(retrofit, sharedPreferencesMock);
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
    public void HasApiKeyBeenSet_WhenKeyIsEmpty_ReturnFalse(){

        //arrange
        Mockito.when(sharedPreferencesMock.getString(StringConstants.API_PREFERENCE_KEY, "")).thenReturn("");

        //act
        whirlpoolRestClient = new TestableWhirlpoolRestClient(retrofit, sharedPreferencesMock);

        //assert
        Assert.assertFalse(whirlpoolRestClient.hasApiKeyBeenSet());
    }

    @Test
    public void HasApiKeyBeenSet_WhenKeyIsSet_ReturnTrue(){

        //arrange
        Mockito.when(sharedPreferencesMock.getString(StringConstants.API_PREFERENCE_KEY, "")).thenReturn("1111-1111");

        //act
        whirlpoolRestClient = new TestableWhirlpoolRestClient(retrofit, sharedPreferencesMock);

        //assert
        Assert.assertTrue(whirlpoolRestClient.hasApiKeyBeenSet());
    }
}

class TestableWhirlpoolRestClient extends WhirlpoolRestClient {

    public IWhirlpoolService mWhirlpoolServiceMock;

    public TestableWhirlpoolRestClient(Retrofit retrofit, SharedPreferences sharedPreferences) {
        super(retrofit, sharedPreferences);

        mWhirlpoolServiceMock = Mockito.mock(IWhirlpoolService.class);
    }

    @Override protected IWhirlpoolService getWhirlpoolService() {
        return mWhirlpoolServiceMock;
    }

}