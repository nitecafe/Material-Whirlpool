package com.android.nitecafe.whirlpoolnews.utilities;

import java.util.HashMap;

/**
 * Created by grahamgoh on 23/02/16.
 */
public interface IObjectSerializer {
    String serializeObject(Object o);

    HashMap<Integer, String> deserializeHashMap(String content);
}
