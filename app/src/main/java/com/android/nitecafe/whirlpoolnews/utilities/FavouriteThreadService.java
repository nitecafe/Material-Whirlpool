package com.android.nitecafe.whirlpoolnews.utilities;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IFavouriteThreadService;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IObjectSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FavouriteThreadService implements IFavouriteThreadService {

    Map<Integer, String> favouriteForums = new HashMap<>();
    private SharedPreferences sharedPreferences;
    private IObjectSerializer objectSerializer;

    @Inject
    public FavouriteThreadService(SharedPreferences sharedPreferences, IObjectSerializer objectSerializer) {
        this.sharedPreferences = sharedPreferences;
        this.objectSerializer = objectSerializer;
        restoreFavouriteThreads();
    }

    private void restoreFavouriteThreads() {
        String s = sharedPreferences.getString(StringConstants.FAVOURITE_THREAD_KEY, "");

        if (!s.isEmpty()) {
            favouriteForums = objectSerializer.deserializeHashMap(s);
        }

    }

    @Override public void addThreadToFavourite(Integer id, String title) {
        if (!favouriteForums.containsKey(id)) {
            favouriteForums.put(id, title);
            saveForumsIntoSharedPreferences();
        } else
            throw new IllegalArgumentException("Forum already exist in favourites");
    }

    @Override
    public void removeThreadFromFavourite(Integer id) {
        if (!favouriteForums.containsKey(id)) {
            throw new IllegalArgumentException("Forum does not exist in favourites");
        }

        favouriteForums.remove(id);
        saveForumsIntoSharedPreferences();
    }


    @Override public boolean isAFavouriteThread(int id) {
        return favouriteForums.containsKey(id);
    }

    private void saveForumsIntoSharedPreferences() {
        String s = objectSerializer.serializeObject(favouriteForums);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(StringConstants.FAVOURITE_THREAD_KEY, s);
        edit.apply();
    }

    @Override
    public List<Forum> getListOfFavouritesThreadIds() {
        List<Forum> favForums = new ArrayList<>();

        for (int id : favouriteForums.keySet()) {
            Forum forum = new Forum();
            forum.setID(id);
            forum.setTITLE(favouriteForums.get(id));
            forum.setSECTION("My Favourites");
            favForums.add(forum);
        }

        return favForums;
    }
}
