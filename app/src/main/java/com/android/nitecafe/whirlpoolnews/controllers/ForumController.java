package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IForumFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IFavouriteThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ForumController {

    public IFavouriteThreadService favouriteThreadService;
    public List<Forum> originalForums = new ArrayList<>();
    private IForumFragment forumFragment;
    private IWhirlpoolRestService whirlpoolRestService;

    @Inject
    public ForumController(IWhirlpoolRestService whirlpoolRestClient, IFavouriteThreadService favouriteThreadService) {
        this.whirlpoolRestService = whirlpoolRestClient;
        this.favouriteThreadService = favouriteThreadService;
    }

    public void getForum() {
        whirlpoolRestService.GetForum()
                .subscribe(forumList -> {
                    if (forumFragment != null) {
                        originalForums = forumList.getFORUM();
                        forumFragment.DisplayForum(getCombinedFavouriteSection());
                        hideAllLoader();
                    }
                }, throwable -> {
                    if (forumFragment != null) {
                        forumFragment.DisplayErrorMessage();
                        hideAllLoader();
                    }
                });
    }


    public List<Forum> getCombinedFavouriteSection() {
        List<Forum> totalList = new ArrayList<>();
        totalList.addAll(getFavouriteSection());
        totalList.addAll(originalForums);
        return totalList;
    }

    private List<Forum> getFavouriteSection() {
        return favouriteThreadService.getListOfFavouritesThreadIds();
    }

    public void AddToFavouriteList(int id, String title) {
        favouriteThreadService.addThreadToFavourite(id, title);
        if (forumFragment != null) {
            forumFragment.UpdateFavouriteSection();
            forumFragment.DisplayAddToFavouriteForumMessage();
        }
    }

    public void RemoveFromFavouriteList(int id) {
        favouriteThreadService.removeThreadFromFavourite(id);
        if (forumFragment != null) {
            forumFragment.UpdateFavouriteSection();
            forumFragment.DisplayRemoveFromFavouriteForumMessage();
        }
    }

    private void hideAllLoader() {
        forumFragment.HideCenterProgressBar();
        forumFragment.HideRefreshLoader();
    }

    public void attach(IForumFragment forumFragment) {
        this.forumFragment = forumFragment;
    }
}
