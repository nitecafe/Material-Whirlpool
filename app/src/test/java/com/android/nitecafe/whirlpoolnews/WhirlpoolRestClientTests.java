package com.android.nitecafe.whirlpoolnews;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolService;
import com.android.nitecafe.whirlpoolnews.models.News;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.web.WhirlpoolRestClient;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.Observable;
import rx.observers.TestObserver;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class WhirlpoolRestClientTests {

    @Mock IWhirlpoolService whirlpoolServiceMock;
    @Mock SharedPreferences sharedPreferencesMock;
    @InjectMocks WhirlpoolRestClient whirlpoolRestClient;

    @Test
    public void GetNews_WhenCalled_ReturnResponse() throws Exception {

        //arrange
        final TestObserver<NewsList> newsListTestObserver = new TestObserver<>();
        final NewsList newsList = new NewsList();
        final News news = createNews();
        newsList.getNEWS().add(news);
        Mockito.when(whirlpoolServiceMock.GetNews()).thenReturn(Observable.just(newsList));

        //act
        whirlpoolRestClient.GetNews().subscribe(newsListTestObserver);
        final List<NewsList> onNextEvents = newsListTestObserver.getOnNextEvents();

        //assert
        Assert.assertEquals(onNextEvents.get(0), newsList);
    }

    @NonNull
    private News createNews() {
        final News news = new News();
        news.setTITLE("AwesomeTitle");
        news.setBLURB("AwesomeBlurb");
        return news;
    }
}