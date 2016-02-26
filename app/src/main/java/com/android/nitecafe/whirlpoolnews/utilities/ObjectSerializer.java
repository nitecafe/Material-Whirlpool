package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IObjectSerializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ObjectSerializer implements IObjectSerializer {

    private final Gson gson;

    @Inject
    @Singleton
    public ObjectSerializer() {
        gson = new Gson();
    }

    @Override
    public String serializeObject(Object o) {
        return gson.toJson(o);
    }

    @Override
    public HashMap<Integer, String> deserializeHashMap(String content) {
        Type listType = new TypeToken<HashMap<Integer, String>>() {
        }.getType();

        return gson.fromJson(content, listType);
    }

    @Override
    public List<PostBookmark> deserializePostBookmarkList(String bookmarkList) {
        Type listType = new TypeToken<ArrayList<PostBookmark>>() {
        }.getType();
        return gson.fromJson(bookmarkList, listType);
    }
}
