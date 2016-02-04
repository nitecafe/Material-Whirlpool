package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.controllers.ForumController;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolService;
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

    @Mock IWhirlpoolService whirlpoolServiceMock;
    @Mock IForumFragment forumFragmentMock;
    private ForumController _controllerToTest;


    @Before
    public void setup() {
        _controllerToTest = new ForumController(whirlpoolServiceMock, new TestSchedulerManager());
        _controllerToTest.attach(forumFragmentMock);
    }

    @Test
    public void GetForum_WhenCalled_CallServiceGetForum() {

        //act
        _controllerToTest.getForum();

        //assert
        verify(whirlpoolServiceMock).GetForum();
    }

    @Test
    public void GetForum_WhenCalled_ShowForumsOnView() {

        //arrange
        ForumList forumList = new ForumList();
        when(whirlpoolServiceMock.GetForum()).thenReturn(Observable.just(forumList));

        //act
        _controllerToTest.getForum();

        //assert
        verify(forumFragmentMock).DisplayForum(anyList());
    }

}
