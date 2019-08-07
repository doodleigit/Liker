package com.doodle.Comment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.service.CommentImageHolder;
import com.doodle.Comment.service.CommentLinkScriptHolder;
import com.doodle.Comment.service.CommentTextHolder;
import com.doodle.Comment.service.CommentYoutubeHolder;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.ImageHolder;
import com.doodle.Home.service.LinkScriptHolder;
import com.doodle.Home.service.LinkScriptYoutubeHolder;
import com.doodle.Home.service.TextHolder;
import com.doodle.Home.service.TextMimHolder;
import com.doodle.Home.service.VideoHolder;
import com.doodle.R;

import java.util.List;

public class AllCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int VIEW_TYPE_TEXT = 1;
    final int VIEW_TYPE_TEXT_IMAGE = 2;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT = 3;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE = 4;


    private List<Comment_> comment_list;
    private Context mContext;
    PostItem postItem;
    private int size = 0;

    private CallBack mCallBack;
    private CommentTextHolder.ExcellentAdventureListener adventureListener;

    public AllCommentAdapter(Context context, List<Comment_> comment_list, PostItem postItem,CommentTextHolder.ExcellentAdventureListener adventureListener) {
        this.mContext = context;
        this.comment_list = comment_list;
        this.postItem = postItem;
        this.adventureListener = adventureListener;
        if (comment_list != null && !comment_list.isEmpty()) {
            size = comment_list.size();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment_text, parent, false);
            return new CommentTextHolder(view, mContext,adventureListener);
        }


        if (viewType == VIEW_TYPE_TEXT_IMAGE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment_image, parent, false);
            return new CommentImageHolder(view, mContext);
        }
        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment_linkscript, parent, false);
            return new CommentLinkScriptHolder(view, mContext);
        }

        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment_youtube, parent, false);
            return new CommentYoutubeHolder(view, mContext);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CommentTextHolder) {
            CommentTextHolder vh = (CommentTextHolder) viewHolder;
            vh.setItem(comment_list.get(position), postItem);
        }


        if (viewHolder instanceof CommentLinkScriptHolder) {
            CommentLinkScriptHolder vh = (CommentLinkScriptHolder) viewHolder;
            vh.setItem(comment_list.get(position),postItem);
        }
        if (viewHolder instanceof CommentYoutubeHolder) {
            CommentYoutubeHolder vh = (CommentYoutubeHolder) viewHolder;
            vh.setItem(comment_list.get(position),postItem);
        }
        if (viewHolder instanceof CommentImageHolder) {
            CommentImageHolder vh = (CommentImageHolder) viewHolder;
//            vh.setItem(postItems.get(position), comments.get(position));
            vh.setItem(comment_list.get(position),postItem);
        }

    }

    @Override
    public int getItemCount() {
        // return comment_list.size();
        return size;
    }

    @Override
    public int getItemViewType(int position) {

        String commentType = comment_list.get(position).getCommentType();
        int viewType = Integer.parseInt(commentType);
        switch (viewType) {
            case 1:
                return VIEW_TYPE_TEXT;
            case 2:
                return VIEW_TYPE_TEXT_IMAGE;
            case 3:
                return VIEW_TYPE_TEXT_LINK_SCRIPT;
            case 4:
                return VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE;
            default:
                return -1;
        }
    }

/*    public void addPagingData(List<PostItem> postItemList,List<Comment> commentList) {

        for (PostItem temp : postItemList
        ) {
            comments.add(temp);
        }
        for (Comment temp : commentList
        ) {
            comments.add(temp);
        }
        notifyDataSetChanged();
    }*/

    public void addPagingData(List<Comment_> commentList) {

        for (Comment_ temp : commentList
        ) {
            comment_list.add(temp);
        }

        notifyDataSetChanged();
    }

    public void refreshData(Comment_ commentItem) {

        if(comment_list.size()>0){
            comment_list.add(0,commentItem);
            size = comment_list.size();
            notifyDataSetChanged();
        } else {
            comment_list.add(commentItem);
            size = comment_list.size();
            notifyDataSetChanged();
        }
    }

    public interface CallBack {
        void myCommentCallBack();

    }

}
