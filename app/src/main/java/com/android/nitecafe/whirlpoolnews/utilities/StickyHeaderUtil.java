package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IStickyHeaderUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * For helping to keep track of which sticky header string has been
 * used and if so return existing id, else return new id
 */
public class StickyHeaderUtil implements IStickyHeaderUtil {

    private Map<String, Integer> headerMap = new HashMap<>();
    private int headerId = 0;

    @Override
    public long generateHeaderId(String headerName) {
        if (headerMap.containsKey(headerName))
            return headerMap.get(headerName);
        else {
            headerMap.put(headerName, ++headerId);
            return headerId;
        }
    }

    @Override
    public void resetHeader() {
        headerMap.clear();
        headerId = 0;
    }
}
