package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.models.Forum;

import java.util.List;

public interface IFavouriteThreadService {

    void addThreadToFavourite(Integer id, String title);

    void removeThreadFromFavourite(Integer id);

    boolean isAFavouriteThread(int id);

    List<Forum> getListOfFavouritesThreadIds();
}
