package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.ScrapedPostChildController;
import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.jakewharton.rxbinding.view.RxView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

public class ScrapedPostAdapter extends UltimateViewAdapter<ScrapedPostAdapter.ScrapedPostViewHolder> {

    public PublishSubject<ScrapedPost> OnReplyPostClickedObservable = PublishSubject.create();
    public PublishSubject<ScrapedPost> OnEditPostClickedObservable = PublishSubject.create();
    public PublishSubject<ScrapedPost> OnOpenCustomTabClickedObservable = PublishSubject.create();
    public PublishSubject<PostBookmark> OnAddToBookmarkClickedObservable = PublishSubject.create();
    public PublishSubject<Integer> OnRemoveFromBookmarkClickedObservable = PublishSubject.create();
    public PublishSubject<Integer> OnViewUserInfoClickedObservable = PublishSubject.create();
    public PublishSubject<String> OnSharePostClickedObservable = PublishSubject.create();
    private List<ScrapedPost> scrapedPosts = new ArrayList<>();
    private ScrapedPostChildController scrapedPostChildController;
    private IPreferencesGetter preferencesGetter;
    private ColorStateList userNameDefaultColor;

    public ScrapedPostAdapter(ScrapedPostChildController scrapedPostChildController, IPreferencesGetter preferencesGetter) {
        this.scrapedPostChildController = scrapedPostChildController;
        this.preferencesGetter = preferencesGetter;
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
        if (userNameDefaultColor == null)
            userNameDefaultColor = holder.postUser.getTextColors();

        final ScrapedPost scrapedPost = scrapedPosts.get(position);

        String userName = scrapedPost.getUser().getUserName();
        if (userName.equals(preferencesGetter.getUserName()))
            holder.postUser.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.orange));
        else
            holder.postUser.setTextColor(userNameDefaultColor);

        holder.postUser.setText(userName);
        holder.postPostedTime.setText(scrapedPost.getPosted_time());

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
        @Bind(R.id.post_user) Button postUser;
        @Bind(R.id.post_posted_time) TextView postPostedTime;
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

            RxView.clicks(postUser).map(aVoid -> {
                ScrapedPost scrapedPost = scrapedPosts.get(getAdapterPosition());
                return Integer.parseInt(scrapedPost.getUser().getUserId());
            })
                    .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "View user info", ""))
                    .subscribe(OnViewUserInfoClickedObservable);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.context_menu_title);

            ScrapedPost post = scrapedPosts.get(getAdapterPosition());
            if (post.getUser().getUserName().equals(preferencesGetter.getUserName())) {
                MenuItem edit = menu.add("Edit Post");
                RxMenuItem.clicks(edit).map(aVoid -> post)
                        .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Edit Post", ""))
                        .subscribe(OnEditPostClickedObservable);
            }

            MenuItem reply = menu.add(R.string.context_menu_reply_browser);
            RxMenuItem.clicks(reply).map(aVoid -> post)
                    .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Reply", ""))
                    .subscribe(OnReplyPostClickedObservable);

            MenuItem sharePost = menu.add(R.string.context_menu_share_post);
            RxMenuItem.clicks(sharePost).map(aVoid -> post.getShortCode())
                    .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Share post", ""))
                    .subscribe(OnSharePostClickedObservable);

            if (scrapedPostChildController.isABookmark(post.getIdInteger())) {
                MenuItem removeBookmark = menu.add(R.string.context_menu_remove_bookmarks);
                RxMenuItem.clicks(removeBookmark).map(aVoid -> post.getIdInteger()
                ).doOnNext(postId -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Remove from Bookmark", ""))
                        .subscribe(OnRemoveFromBookmarkClickedObservable);
            } else {
                MenuItem bookmark = menu.add(R.string.context_menu_add_bookmark);
                RxMenuItem.clicks(bookmark).map(aVoid -> {
                    final int i = Integer.parseInt(post.getId());
                    return new PostBookmark(i, getAdapterPosition() + 1);
                }).doOnNext(postBookmark -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Add to Bookmark", ""))
                        .subscribe(OnAddToBookmarkClickedObservable);
            }

            MenuItem openCustomTab = menu.add(R.string.context_menu_open_web_version);
            RxMenuItem.clicks(openCustomTab).map(aVoid -> post)
                    .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_CONTEXT_MENU, "Open in Custom Tab", ""))
                    .subscribe(OnOpenCustomTabClickedObservable);
        }
    }
}
