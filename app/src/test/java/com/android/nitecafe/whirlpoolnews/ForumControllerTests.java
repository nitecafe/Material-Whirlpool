package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.controllers.ForumController;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IForumFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ForumControllerTests {

    @Mock IWhirlpoolRestClient whirlpoolRestClientMock;
    @Mock IForumFragment forumFragmentMock;
    private ForumController _controllerToTest;
    private TestSchedulerManager testSchedulerManager;


    @Before
    public void setup() {
        testSchedulerManager = new TestSchedulerManager();
        _controllerToTest = new ForumController(whirlpoolRestClientMock, new TestSchedulerManager());
        _controllerToTest.attach(forumFragmentMock);
    }

    @Test
    public void GetForum_WhenCalled_CallServiceGetForum() {

        //arrange
        ForumList forumList = new ForumList();
        when(whirlpoolRestClientMock.GetForum()).thenReturn(Observable.just(forumList));

        //act
        _controllerToTest.getForum();

        //assert
        verify(whirlpoolRestClientMock).GetForum();
    }

//    @Test
//    public void GetForum_WhenCalled_ShowForumsOnView() {
//
//        //arrange
//        ForumList forumList = new ForumList();
//        when(whirlpoolRestClientMock.GetForum()).thenReturn(Observable.just(forumList));
//
//        //act
//        _controllerToTest.getForum();
//        testSchedulerManager.testScheduler.triggerActions();
//
//        //assert
//        verify(forumFragmentMock).DisplayForum(anyList());
//    }

}
