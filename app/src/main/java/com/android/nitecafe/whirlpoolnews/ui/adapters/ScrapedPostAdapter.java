package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
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
    private List<ScrapedPost> scrapedPosts = new ArrayList<>();

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
            holder.postUserTitle.setText("OP / " + scrapedPost.getUser().getGroup());
        else
            holder.postUserTitle.setText(scrapedPost.getUser().getGroup());

        holder.postContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private Spanned parseHtmlContent(String content) {
        final String parsedContent = WhirlpoolUtils.replaceAllWhirlpoolLinksWithInternalAppLinks(content);
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
        }

        @Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.context_menu_title);
            MenuItem reply = menu.add("Reply in Browser");
            RxMenuItem.clicks(reply).map(aVoid -> scrapedPosts.get(getAdapterPosition()))
                    .doOnNext(scrapedPost -> WhirlpoolApp.getInstance().trackEvent("Post Context Menu", "Reply", ""))
                    .subscribe(OnReplyPostClickedObservable);

        }
    }
}
