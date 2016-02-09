package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;

public class ThreadScraper implements IThreadScraper {

    public static final String FORUM_URL = "http://forums.whirlpool.net.au/forum/";
    // these forum IDs are public, and we can scrape the data from them
    private static int[] PUBLIC_FORUMS = {92, 100, 142, 82, 9, 107, 135, 80, 136, 125, 116, 63,
            127, 139, 7, 129, 130, 131, 10, 38, 39, 91, 87, 112, 132, 8, 83, 138, 143, 133, 58, 106,
            126, 71, 118, 137, 114, 123, 128, 141, 140, 144, 18, 14, 15, 68, 72, 94, 90, 102, 105,
            109, 108, 147, 31, 67, 5, 148, 149, 150};

    /**
     * Checks if a forum is public (ie. it can be scraped)
     *
     * @param forum_id forumId
     * @return True if forum is public
     */
    public static boolean isPublicForum(int forum_id) {
        for (int PUBLIC_FORUM : PUBLIC_FORUMS) {
            if (PUBLIC_FORUM == forum_id) {
                return true;
            }
        }
        return false;
    }

    @Override public Observable<List<ScrapedThread>> scrapeThreadsFromForumObservable(int forum_id, int page_number, int group_id) {
        return Observable.create(subscriber -> {
            ArrayList<ScrapedThread> scrapedThreads = null;
            try {
                scrapedThreads = scrapeThreadsFromForum(forum_id, page_number, group_id);
            } catch (Exception e) {
                subscriber.onError(e);
            }
            subscriber.onNext(scrapedThreads);
            subscriber.onCompleted();
        });
    }

    public ArrayList<ScrapedThread> scrapeThreadsFromForum(int forum_id, int page_number, int group_id) {
        ArrayList<ScrapedThread> threads = new ArrayList<>();

        int page_count = -1;

        String forum_url = FORUM_URL + forum_id + "?p=" + page_number;

        if (group_id != 0) {
            forum_url += "&g=" + group_id;
        }

        Document doc = downloadPage(forum_url);
        if (doc == null) {
            return null;
        }

        // get the number of pages in this forum
        // see if there's a select box with page options
        try {
            Element p = doc.select("select[name=p] option").last(); // get the last option
            page_count = Integer.parseInt(p.text());
        } catch (NullPointerException e) {
        }

        if (page_count == -1) {
            // no select box present, get pagination list elements
            try {
                Element p = doc.select("ul.pagination li").last();
                page_count = Integer.parseInt(p.text().replace("\u00a0", "").trim());
            } catch (NullPointerException e) {
            }
        }

        // get the forum title
        String forum_title;

        try {
            forum_title = doc.select("ul#breadcrumb li").last().text();
        } catch (NullPointerException e) {
            forum_title = "";
        }

        // get the groups in this forum
        Map<String, Integer> groups = null;
        try {
            groups = new LinkedHashMap<String, Integer>();

            Elements group_options = doc.select("select[name=g] option");

            for (Element group_option : group_options) {
                if (!group_option.attr("value").equals("0")) {
                    groups.put(group_option.text(), Integer.parseInt(group_option.attr("value")));
                }
            }
        } catch (NullPointerException e) {
            // no groups in this forum, do nothing
        }

        Elements trs = doc.select("tr");

        for (Element tr : trs) {
            Set<String> tr_classes = tr.classNames();

            ScrapedThread t = getThreadFromTableRow(tr, null, forum_id);

            if (tr_classes.contains("sticky")) {
                t.setSticky(true);
            }
            if (tr_classes.contains("closed")) {
                t.setClosed(true);
            }
            if (tr_classes.contains("deleted")) {
                t.setDeleted(true);
            }
            if (tr_classes.contains("pointer")) {
                t.setMoved(true);
            }

            if (t != null) {
                threads.add(t);
            }
        }

//        Forum forum = new Forum(forum_id, forum_title, 0, null);
//        forum.setPageCount(page_count);
//        forum.setGroups(groups);
//        forum.setThreads(threads);

//        return forum;
        return threads;
    }

    private ScrapedThread getThreadFromTableRow(Element tr, String forum, int forum_id) {
        int id = 0;
        String title = "";
        String last_poster = "";
        String last_post_date = "";
        String first_poster = "";
        String first_post_date = "";
        int page_count = 1;

        Elements tds = tr.children();
        // title reps reads oldest newest
        for (Element td : tds) {
            Set<String> td_classes = td.classNames();

            if (td_classes.contains("title")) {

                String url = "";

                try {
                    for (Element child : td.children()) {
                        if (child.hasClass("title")) {
                            title = child.text();
                            url = child.attr("href");
                        }
                    }

                } catch (Exception e) {
                    title = td.text();
                    url = td.select("a").get(0).attr("href");
                }

                Pattern thread_id_regex = Pattern.compile("(t=([0-9]+))|(/archive/([0-9]+))");
                Matcher m = thread_id_regex.matcher(url);
                while (m.find()) {
                    try {
                        id = Integer.parseInt(m.group(2));
                    } catch (NumberFormatException e) {
                        id = Integer.parseInt(m.group(4));
                    }
                }

                // get thread page count
                try {
                    Element page_element = td.select("script").get(0);

                    Pattern page_count_regex = Pattern.compile("([0-9]+),([0-9]+)");
                    Matcher page_matcher = page_count_regex.matcher(page_element.html());
                    while (page_matcher.find()) {
                        page_count = Integer.parseInt(page_matcher.group(2));
                    }

                    //page_count = Integer.parseInt(page_element.text().replace(" ", ""));
                } catch (NullPointerException e) {
                    // no page list, must only be 1 page
                } catch (IndexOutOfBoundsException e) {
                    // no page list
                } catch (NumberFormatException e) {
                    // not a number, probably a deleted thread; ignore
                }
            } else if (td_classes.contains("newest")) {
                try {
                    last_poster = td.child(0).text();
                } catch (Exception e) {
                }

                last_post_date = td.ownText();
            } else if (td_classes.contains("oldest")) {
                try {
                    first_poster = td.child(0).text();
                } catch (Exception e) {
                }

                first_post_date = td.ownText();
            }
        }

        if (last_poster.equals("")) { // no replies yet
            // set the first poster as the last poster (since there's only 1 post)
            last_poster = first_poster;
            last_post_date = first_post_date;
        }

        ScrapedThread t = new ScrapedThread(id, title, null, last_poster, forum, forum_id);
        t.setLastDateText(last_post_date);
        t.setPageCount(page_count);

        if (title == "") {
            return null;
        }

        return t;
    }

    private Document downloadPage(String url) {
        int connection_attempts = 3;

        while (connection_attempts > 0) {
            try {
                return Jsoup.connect(url).timeout(7000).get();
            } catch (IOException e) {
                // error connecting, try again
            }

            connection_attempts--;
        }

        return null;
    }
}
