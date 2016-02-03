package com.android.nitecafe.whirlpoolnews;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolService;
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.News;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.web.WhirlpoolRestClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

    @Mock IWhirlpoolService whirlpoolServiceMock;
    @Mock SharedPreferences sharedPreferencesMock;
    TestableWhirlpoolRestClient whirlpoolRestClient;

    @Before
    public void setup() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.google.com").build();
        Mockito.when(sharedPreferencesMock.getString(StringConstants.API_PREFERENCE_KEY, "")).thenReturn("111-111");
        whirlpoolRestClient = new TestableWhirlpoolRestClient(retrofit, whirlpoolServiceMock, sharedPreferencesMock);
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
}

class TestableWhirlpoolRestClient extends WhirlpoolRestClient {

    public IWhirlpoolService mWhirlpoolServiceMock;

    public TestableWhirlpoolRestClient(Retrofit retrofit, IWhirlpoolService whirlpoolService, SharedPreferences sharedPreferences) {
        super(retrofit, whirlpoolService, sharedPreferences);

        mWhirlpoolServiceMock = Mockito.mock(IWhirlpoolService.class);
    }

    @Override protected IWhirlpoolService getWhirlpoolService() {
        return mWhirlpoolServiceMock;
    }

}