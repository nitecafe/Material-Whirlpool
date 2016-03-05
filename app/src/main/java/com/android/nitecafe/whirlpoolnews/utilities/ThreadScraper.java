package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPostList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;
import com.android.nitecafe.whirlpoolnews.models.User;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IThreadScraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;

public class ThreadScraper implements IThreadScraper {


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

    private static String buildSearchUrl(int forum_id, int group_id, String query) {
        URI uri;
        try {
            uri = new URI("http", "forums.whirlpool.net.au", "/forum/", "action=threads_search&f=" + forum_id + "&fg=" + group_id + "&q=" + query, null);
        } catch (URISyntaxException e) {
            return null;
        }
        return uri.toASCIIString();
    }

    @Override
    public Observable<ScrapedThreadList> scrapeThreadsFromForumObservable(int forum_id, int page_number, int group_id) {
        return Observable.create(subscriber -> {
            ScrapedThreadList scrapedThreads = null;
            try {
                scrapedThreads = scrapeThreadsFromForum(forum_id, page_number, group_id);
            } catch (Exception e) {
                subscriber.onError(e);
            }
            subscriber.onNext(scrapedThreads);
            subscriber.onCompleted();
        });
    }

    public ScrapedThreadList scrapeThreadsFromForum(int forum_id, int page_number, int group_id) {
        ArrayList<ScrapedThread> threads = new ArrayList<>();

        int page_count = -1;

        String forum_url = StringConstants.FORUM_URL + forum_id + "?p=" + page_number;

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
            groups = new LinkedHashMap<>();

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

        ScrapedThreadList threadList = new ScrapedThreadList(forum_id, forum_title);
        threadList.setPageCount(page_count);
        threadList.setGroups(groups);
        threadList.setThreads(threads);

        return threadList;
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

    @Override
    public Observable<ScrapedPostList> scrapePostsFromThreadObservable(int threadId, int page) {
        return Observable.create(subscriber -> {
            ScrapedPostList posts = null;
            try {
                posts = scrapePostsFromThread(threadId, "", page);
            } catch (IOException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onNext(posts);
            subscriber.onCompleted();
        });
    }

    public ScrapedPostList scrapePostsFromThread(int thread_id, String thread_title, int page) throws IOException {

        ArrayList<ScrapedPost> scrapedPosts = new ArrayList<>();

        String thread_url = StringConstants.THREAD_URL + thread_id;
        if (page != 1) {
            thread_url += "&p=" + page;
        }

        Document doc = downloadPage(thread_url);
        if (doc == null) {
            throw new IOException("Error downloading data");
        }

        // check for an error message
        Elements alert = doc.select("#alert");
        if (alert != null && alert.size() > 0) {
            throw new IOException("Private forum");
        }

        if (thread_title == null || thread_title == "") { // no thread title was passed
            // scrape the title from the page
            Elements breadcrumb_elements = doc.select("#breadcrumb li");
            thread_title = breadcrumb_elements.last().text();
        }

        // get page count
        Elements pagination_elements = doc.select("#top_pagination li");
        int page_count = pagination_elements.size() - 2; // list elements, subtract first date and last date elements
        if (page_count <= 0) page_count = 1;

        // get notebar (thread header that mods put there sometimes)
        String notebar = null;
        try {
            notebar = doc.select(".notebar").get(0).html();
        } catch (IndexOutOfBoundsException e) {
            // no notebar
        }

        Elements replies = doc.select("#replylist > div");

        for (Element reply : replies) {
            String id;
            String user_id;
            String user_name;
            String posted_time = "";
            String content;
            boolean edited = false;
            boolean op = false;
            boolean deleted = false;

            // get reply ID
            id = reply.attr("id").replace("r", "");

            // get author name
            Element user_name_element;
            try {
                user_name_element = reply.select(".bu_name").get(0);
            } catch (IndexOutOfBoundsException e) {
                // username not found, probably a deleted scrapedPost
                user_name_element = reply.select(".replyuser > a > b").get(0);
            }
            user_name = user_name_element.text();

            // get author ID
            Element user_id_element = reply.select(".userid").get(0);
            user_id = user_id_element.text().replace("User #", "");

            // check if this author is the OP
            Elements op_element = reply.select(".op");
            if (!op_element.isEmpty()) { // user is the OP
                op = true;
                op_element.get(0).remove(); // remove element so the text doesn't show up
            }

            // check if this scrapedPost has been edited
            Elements edited_elements = reply.select(".edited");
            if (!edited_elements.isEmpty()) {
                edited = true;

                // remove elements so text doesn't show up
                for (Element edited_element : edited_elements) {
                    edited_element.remove();
                }
            }

            // get the poster's user group
            String user_group = "";
            try {
                user_group = reply.select(".usergroup").get(0).text();
            } catch (IndexOutOfBoundsException e) {
                // no usergroup, probably a deleted scrapedPost
            }

            // get posted time
            try {
                Element date_element = reply.select(".date").get(0);
                posted_time = date_element.ownText();
            } catch (IndexOutOfBoundsException e) {
                // no scrapedPost date, probably a deleted scrapedPost
                deleted = true;
            }

            // get the reply content
            Element content_element = reply.select(".replytext").get(0);
            content = content_element.html();

            User user = new User(user_id, user_name);
            user.setGroup(user_group);

            ScrapedPost scrapedPost = new ScrapedPost(id, user, posted_time, content, edited, op);

            scrapedPost.setDeleted(deleted);

            scrapedPosts.add(scrapedPost);
        }

        ScrapedPostList scrapedPostList = new ScrapedPostList(thread_id, thread_title);
        scrapedPostList.setPageCount(page_count);
        scrapedPostList.setNotebar(notebar);
        scrapedPostList.setScrapedPosts(scrapedPosts);

        return scrapedPostList;
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

    @Override
    public Observable<ArrayList<ScrapedThread>> ScrapPopularThreadsObservable() {
        return Observable.create(subscriber -> {
            ArrayList<ScrapedThread> scrapedThreads = null;
            try {
                scrapedThreads = downloadPopularThreads();
            } catch (IOException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onNext(scrapedThreads);
            subscriber.onCompleted();
        });
    }

    private ArrayList<ScrapedThread> downloadPopularThreads() throws IOException {
        ArrayList<ScrapedThread> threads = new ArrayList<>();

        Document doc = downloadPage(StringConstants.POPULAR_URL);
        if (doc == null) {
            throw new IOException("Unable to download popular thread page");
        }

        Elements trs = doc.select("tr");

        String current_forum = null;
        int current_forum_id = 0;

        for (Element tr : trs) {
            Set<String> tr_classes = tr.classNames();

            // section - contains a forum name
            if (tr_classes.contains("section")) {
                current_forum = tr.text();

                // get the forum ID
                String forum_url = tr.select("a").attr("href");
                Pattern forum_id_regex = Pattern.compile("/forum/([0-9]+)");
                Matcher m = forum_id_regex.matcher(forum_url);
                while (m.find()) {
                    current_forum_id = Integer.parseInt(m.group(1));
                }
            }
            // thread
            else {
                if (current_forum == null) continue;

                ScrapedThread t = getThreadFromTableRow(tr, current_forum, current_forum_id);
                threads.add(t);
            }
        }

        return threads;
    }

    @Override
    public Observable<List<ScrapedThread>> searchThreadsObservable(int forum_id, int group_id, String query) {
        return Observable.create(subscriber -> {
            try {
                final List<ScrapedThread> scrapedThreads = searchThreads(forum_id, group_id, query);
                subscriber.onNext(scrapedThreads);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private List<ScrapedThread> searchThreads(int forum_id, int group_id, String query) {
        String search_url = buildSearchUrl(forum_id, group_id, query);

        if (search_url == null) {
            return null;
        }

        List<ScrapedThread> threads = new ArrayList<>();

        Document doc = downloadPage(search_url);
        if (doc == null) {
            return null;
        }

        Elements trs = doc.select("tr");

        String current_forum = null;
        int current_forum_id = 0;

        for (Element tr : trs) {
            Set<String> tr_classes = tr.classNames();

            // section - contains a forum name
            if (tr_classes.contains("section")) {
                current_forum = tr.text();

                // get the forum ID
                String forum_url = tr.select("a").attr("href");
                Pattern forum_id_regex = Pattern.compile("/forum/([0-9]+)");
                Matcher m = forum_id_regex.matcher(forum_url);
                while (m.find()) {
                    current_forum_id = Integer.parseInt(m.group(1));
                }
            }
            // thread
            else {
                if (current_forum == null) continue;

                ScrapedThread t = getThreadFromTableRow(tr, current_forum, current_forum_id);
                threads.add(t);
            }
        }

        return threads;
    }

}
