package com.android.nitecafe.whirlpoolnews.utilities;

import android.text.Layout;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

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

    public static void allowLinksInTextViewToBeClickable(TextView textView){
        textView.setOnTouchListener((v, event) -> {
            boolean ret = false;
            CharSequence text = ((TextView) v).getText();
            Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
            TextView widget = (TextView) v;
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    }
                    ret = true;
                }
            }
            return ret;
        });
    }
}
