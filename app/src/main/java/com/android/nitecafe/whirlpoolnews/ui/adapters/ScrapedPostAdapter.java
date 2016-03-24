package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.ScrapedPostChildController;
import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

public class ScrapedPostAdapter extends UltimateViewAdapter<ScrapedPostAdapter.ScrapedPostViewHolder> {

    public PublishSubject<ScrapedPost> OnReplyPostClickedObservable = PublishSubject.create();
    public PublishSubject<ScrapedPost> OnOpenCustomTabClickedObservable = PublishSubject.create();
    public PublishSubject<PostBookmark> OnAddToBookmarkClickedObservable = PublishSubject.create();
    public PublishSubject<Integer> OnRemoveFromBookmarkClickedObservable = PublishSubject.create();
    public PublishSubject<Integer> OnViewUserInfoClickedObservable = PublishSubject.create();
    public PublishSubject<String> OnSharePostClickedObservable = PublishSubject.create();
    private List<ScrapedPost> scrapedPosts = new ArrayList<>();
    private ScrapedPostChildController scrapedPostChildController;

    public ScrapedPostAdapter(ScrapedPostChildController scrapedPostChildController) {
        this.scrapedPostChildController = scrapedPostChildController;
    }

    public void SetPosts(List<ScrapedPost> posts) {
        scrapedPosts = posts;
        notifyDataSetChanged();
    }

    @Override
    public ScrapedPostViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public ScrapedPostViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ScrapedPostViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ScrapedPostViewHolder holder, int position) {
        final ScrapedPost scrapedPost = scrapedPosts.get(position);

        holder.postUser.setText(scrapedPost.getUser().getUserName());
        holder.postPostedtime.setText(scrapedPost.getPosted_time());

        if (scrapedPost.isEdited())
            holder.editedText.setText(R.string.edited);
        else
            holder.editedText.setText("");

        holder.postContent.setText(parseHtmlContent(scrapedPost.getContent()));

        if (scrapedPost.isOp())
            holder.postUserTitle.setText(String.format("OP / %s", scrapedPost.getUser().getGroup()));
        else
            holder.postUserTitle.setText(scrapedPost.getUser().getGroup());

        //stop users from replying when post is deleted since post Id does not exists anymore
        if (scrapedPost.isDeleted())
            holder.itemView.setLongClickable(false);
        else
            holder.itemView.setLongClickable(true);
    }

    private Spanned parseHtmlContent(String content) {
        final String parsedContent = WhirlpoolUtils.replaceAllWhirlpoolLinksWithInternalAppLinks(content,
                scrapedPostChildController.isDarkTheme());
        return Html.fromHtml(parsedContent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getAdapterItemCount() {
        return scrapedPosts.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    public class ScrapedPostViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public View itemView;
        @Bind(R.id.post_user) TextView postUser;
        @Bind(R.id.post_posted_time) TextView postPostedtime;
        @Bind(R.id.post_extra) TextView postExtra;
        @Bind(R.id.post_content) TextView postContent;
        @Bind(R.id.post_user_title) TextView postUserTitle;
        @Bind(R.id.post_user_hasEdited) TextView editedText;

        ScrapedPostViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnCreateContextMenuListener(this);
            ButterKnife.bind(this, itemView);
            WhirlpoolUtils.allowLinksInTextViewToBeClickable(postContent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.context_menu_title);
            MenuItem reply = menu.add(R.string.context_menu_reply_browser);
            RxMenuItem.clicks(reply).map(aVoid -> scrapedPosts.get(getAdapterPosition()))
                    .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Reply", ""))
                    .subscribe(OnReplyPostClickedObservable);

            MenuItem userInfo = menu.add(R.string.context_menu_view_user_info);
            RxMenuItem.clicks(userInfo).map(aVoid -> {
                ScrapedPost scrapedPost = scrapedPosts.get(getAdapterPosition());
                return Integer.parseInt(scrapedPost.getUser().getUserId());
            })
                    .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "View user info", ""))
                    .subscribe(OnViewUserInfoClickedObservable);

            MenuItem sharePost = menu.add(R.string.context_menu_share_post);
            RxMenuItem.clicks(sharePost).map(aVoid -> scrapedPosts.get(getAdapterPosition()).getShortCode())
                    .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Share post", ""))
                    .subscribe(OnSharePostClickedObservable);

            if (scrapedPostChildController.isABookmark(scrapedPosts.get(getAdapterPosition()).getIdInteger())) {
                MenuItem removeBookmark = menu.add(R.string.context_menu_remove_bookmarks);
                RxMenuItem.clicks(removeBookmark).map(aVoid -> scrapedPosts.get(getAdapterPosition()).getIdInteger()
                ).doOnNext(postId -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Remove from Bookmark", ""))
                        .subscribe(OnRemoveFromBookmarkClickedObservable);
            } else {
                MenuItem bookmark = menu.add(R.string.context_menu_add_bookmark);
                RxMenuItem.clicks(bookmark).map(aVoid -> {
                    final ScrapedPost scrapedPost = scrapedPosts.get(getAdapterPosition());
                    final int i = Integer.parseInt(scrapedPost.getId());
                    return new PostBookmark(i, getAdapterPosition() + 1);
                }).doOnNext(postBookmark -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Add to Bookmark", ""))
                        .subscribe(OnAddToBookmarkClickedObservable);
            }

            MenuItem openCustomTab = menu.add(R.string.context_menu_open_web_version);
            RxMenuItem.clicks(openCustomTab).map(aVoid -> scrapedPosts.get(getAdapterPosition()))
                    .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Open in Custom Tab", ""))
                    .subscribe(OnOpenCustomTabClickedObservable);
        }
    }
}
