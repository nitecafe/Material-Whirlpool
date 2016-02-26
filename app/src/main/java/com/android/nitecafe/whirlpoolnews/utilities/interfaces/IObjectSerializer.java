package com.android.nitecafe.whirlpoolnews.utilities.interfaces;

import com.android.nitecafe.whirlpoolnews.models.PostBookmark;

import java.util.HashMap;
import java.util.List;

public interface IObjectSerializer {
    String serializeObject(Object o);

    HashMap<Integer, String> deserializeHashMap(String content);

    List<PostBookmark> deserializePostBookmarkList(String bookmarkList);
}
