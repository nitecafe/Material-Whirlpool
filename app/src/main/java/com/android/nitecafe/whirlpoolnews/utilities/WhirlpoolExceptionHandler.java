package com.android.nitecafe.whirlpoolnews.utilities;

public class WhirlpoolExceptionHandler {

    public static boolean isPrivateForumException(Throwable e) {
        return "Private forum".equals(e.getMessage());
    }
}
