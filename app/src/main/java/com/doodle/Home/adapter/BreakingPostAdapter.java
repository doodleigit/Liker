package com.doodle.Home.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.ImageHolder;
import com.doodle.Home.service.LinkScriptHolder;
import com.doodle.Home.service.LinkScriptYoutubeHolder;
import com.doodle.Home.service.TextHolder;
import com.doodle.Home.service.TextMimHolder;
import com.doodle.Home.service.VideoHolder;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.DataProvider;
import com.doodle.R;

import java.util.List;

public class BreakingPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_TEXT = 1;
    final int VIEW_TYPE_TEXT_IMAGE = 2;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT = 3;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE = 4;
    final int VIEW_TYPE_VIDEO = 5;
    final int VIEW_TYPE_TEX_MIM = 6;

    private List<PostItem> postItems;
    List<Mim> viewColors = DataProvider.mimList;
    private Context mContext;
    Drawable mDrawable;
    public TextHolder.PostItemListener listener;
    public TextMimHolder.PostItemListener mimListener;
    public VideoHolder.PostItemListener videoListener;
    public LinkScriptYoutubeHolder.PostItemListener YoutubeListener;
    public LinkScriptHolder.PostItemListener LinkListener;
    public ImageHolder.PostItemListener imageListener;


    public BreakingPostAdapter(Context context, List<PostItem> postItems,
                               TextHolder.PostItemListener listener,
                               TextMimHolder.PostItemListener mimListener,
                               VideoHolder.PostItemListener videoListener,
                               LinkScriptYoutubeHolder.PostItemListener YoutubeListener,
                               LinkScriptHolder.PostItemListener LinkListener,
                               ImageHolder.PostItemListener imageListener
    ) {
        this.mContext = context;
        this.postItems = postItems;
        this.listener = listener;
        this.mimListener = mimListener;
        this.videoListener = videoListener;
        this.YoutubeListener = YoutubeListener;
        this.LinkListener = LinkListener;
        this.imageListener = imageListener;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_text, parent, false);
            return new TextHolder(view, mContext, listener);
        }

        if (viewType == VIEW_TYPE_TEX_MIM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_text_mim, parent, false);
            return new TextMimHolder(view, mContext, mimListener);
        }
        if (viewType == VIEW_TYPE_TEXT_IMAGE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_image, parent, false);
            return new ImageHolder(view, mContext, imageListener);
        }
        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_link_script, parent, false);
            return new LinkScriptHolder(view, mContext, LinkListener);
        }

        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_link_script_youtube, parent, false);
            return new LinkScriptYoutubeHolder(view, mContext, YoutubeListener);
        }
        if (viewType == VIEW_TYPE_VIDEO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_video, parent, false);
            return new VideoHolder(view, mContext, videoListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof TextHolder) {
            TextHolder vh = (TextHolder) viewHolder;
            vh.setItem(postItems.get(position), position);
        }
        if (viewHolder instanceof TextMimHolder) {
            TextMimHolder vh = (TextMimHolder) viewHolder;
            vh.setItem(postItems.get(position), position);
        }
        if (viewHolder instanceof LinkScriptHolder) {
            LinkScriptHolder vh = (LinkScriptHolder) viewHolder;
            vh.setItem(postItems.get(position), position);
        }
        if (viewHolder instanceof LinkScriptYoutubeHolder) {
            LinkScriptYoutubeHolder vh = (LinkScriptYoutubeHolder) viewHolder;
            vh.setItem(postItems.get(position), position);
        }
        if (viewHolder instanceof ImageHolder) {
            ImageHolder vh = (ImageHolder) viewHolder;
            vh.setItem(postItems.get(position), position);

        }
        if (viewHolder instanceof VideoHolder) {
            VideoHolder vh = (VideoHolder) viewHolder;
            vh.setItem(postItems.get(position), position);

        }
    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        String postType = postItems.get(position).getPostType();
        String hasMim = postItems.get(position).getHasMeme();
        int mimId = Integer.parseInt(hasMim);
        int viewType = Integer.parseInt(postType);
        switch (viewType) {
            case 1:
                if (mimId > 0) {

                    return VIEW_TYPE_TEX_MIM;
                } else {
                    return VIEW_TYPE_TEXT;
                }
            case 2:
                return VIEW_TYPE_TEXT_IMAGE;
            case 3:
                return VIEW_TYPE_TEXT_LINK_SCRIPT;
            case 4:
                return VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE;
            case 5:
                return VIEW_TYPE_VIDEO;
            default:
                return -1;
        }
    }


    public void addPagingData(List<PostItem> postItemList) {

        for (PostItem temp : postItemList
        ) {
            postItems.add(temp);
        }

        notifyDataSetChanged();
    }


    public void deleteItem(int position) {


        // comment_list.remove(position);
        // comment_list.remove(position);

        //   postItems.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();

    }

}
