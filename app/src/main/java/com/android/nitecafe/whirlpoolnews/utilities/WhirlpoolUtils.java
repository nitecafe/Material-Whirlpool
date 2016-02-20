package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;

public class WhirlpoolUtils {

    public static int getNumberOfPage(int numberOfReplies) {
        return (int) Math.ceil((double) numberOfReplies / StringConstants.POST_PER_PAGE);
    }

    public static String replaceAllWhirlpoolLinksWithInternalAppLinks(String content) {
        //        String content = scrapedPost.getContent().replace("\n", "").replace("\r", "");
        String user_quote_colour = "#4455aa";

        // user quote name
        content = content.replaceAll("<p class=\"reference\">(.*?)</p>", "<p><font color='" + user_quote_colour + "'><b>$1</b></font></p>");

        // user quote text
        content = content.replaceAll("<span class=\"wcrep1\">(.*?)</span>", "<font color='" + user_quote_colour + "'>$1</font>");

        // other quote text
        content = content.replaceAll("<span class=\"wcrep2\">(.*?)</span>", "<font color='#9F6E19'>$1</font>");

        // lists
        content = content.replace("<ul><li>", "<ul><li> • ");
        content = content.replace("<li>", "<br><li> • ");

        // links to other threads
        String url_replace = "com.nitecafe.whirlpool://whirlpool?threadid=";

        // wiki links
        content = content.replace("href=\"/wiki/", "href=\"http://forums.whirlpool.net.au/wiki/");
        content = content.replace("href=\"//", "href=\"http://");

        content = content.replace("http://forums.whirlpool.net.au/forum-replies.cfm?t=", url_replace);
        content = content.replace("https://forums.whirlpool.net.au/forum-replies.cfm?t=", url_replace);
        content = content.replace("href=\"/forum-replies.cfm?t=", "href=\"" + url_replace);
        content = content.replace("href=\"forum-replies.cfm?t=", "href=\"" + url_replace);

        return content;
    }
}
