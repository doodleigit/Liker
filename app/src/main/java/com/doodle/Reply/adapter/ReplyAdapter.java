package com.doodle.Reply.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doodle.Comment.holder.CommentImageHolder;
import com.doodle.Comment.holder.CommentLinkScriptHolder;
import com.doodle.Comment.holder.CommentTextHolder;
import com.doodle.Comment.holder.CommentYoutubeHolder;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.model.Reply;
import com.doodle.Home.model.PostItem;
import com.doodle.R;
import com.doodle.Reply.holder.ReplyImageHolder;
import com.doodle.Reply.holder.ReplyLinkScriptHolder;
import com.doodle.Reply.holder.ReplyTextHolder;
import com.doodle.Reply.holder.ReplyYoutubeHolder;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int VIEW_TYPE_TEXT = 1;
    final int VIEW_TYPE_TEXT_IMAGE = 2;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT = 3;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE = 4;



    private List<Reply> replyList;
    private Context mContext;
    PostItem postItem;
    Comment_ comment_;
    private int size = 0;


    private ReplyLinkScriptHolder.ReplyListener replyLinkListener;
    private ReplyTextHolder.ReplyListener replyTextListener;
    private ReplyYoutubeHolder.ReplyListener replyYoutubeListener;
    private ReplyImageHolder.ReplyListener replyImageListener;

    public ReplyAdapter(Context context, List<Reply> replyList, PostItem postItem,
                        ReplyTextHolder.ReplyListener replyTextListener,
                        ReplyLinkScriptHolder.ReplyListener replyLinkListener,
                        ReplyYoutubeHolder.ReplyListener replyYoutubeListener,
                        ReplyImageHolder.ReplyListener replyImageListener

    ) {
        this.mContext = context;
        this.replyList = replyList;
        this.postItem = postItem;
        this.comment_ = comment_;
        this.replyTextListener = replyTextListener;
        this.replyLinkListener = replyLinkListener;
        this.replyYoutubeListener = replyYoutubeListener;
        this.replyImageListener = replyImageListener;
            size = replyList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;


        if (viewType == VIEW_TYPE_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reply_text, parent, false);
            return new ReplyTextHolder(view, mContext, replyTextListener);
        }


        if (viewType == VIEW_TYPE_TEXT_IMAGE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reply_image, parent, false);
            return new ReplyImageHolder(view, mContext, replyImageListener);
        }
        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reply_linkscript, parent, false);
            return new ReplyLinkScriptHolder(view, mContext, replyLinkListener);
        }

        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reply_youtube, parent, false);
            return new ReplyYoutubeHolder(view, mContext, replyYoutubeListener);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ReplyTextHolder) {
            ReplyTextHolder vh = (ReplyTextHolder) viewHolder;
            vh.setItem(replyList.get(position), postItem, position);
        }


        if (viewHolder instanceof ReplyLinkScriptHolder) {
            ReplyLinkScriptHolder vh = (ReplyLinkScriptHolder) viewHolder;
            vh.setItem(replyList.get(position), postItem, position);
        }
        if (viewHolder instanceof ReplyYoutubeHolder) {
            ReplyYoutubeHolder vh = (ReplyYoutubeHolder) viewHolder;
            vh.setItem(replyList.get(position), postItem, position);
        }
        if (viewHolder instanceof ReplyImageHolder) {
            ReplyImageHolder vh = (ReplyImageHolder) viewHolder;
//            vh.setItem(postItems.get(position), comments.get(position));
            vh.setItem(replyList.get(position), postItem, position);
        }

    }

    @Override
    public int getItemCount() {
        // return comment_list.size();
        return replyList.size();
    }

    String commentType;

    @Override
    public int getItemViewType(int position) {

        Log.d("size", replyList.size() + "");
        Log.d("index", replyList.indexOf(getItemCount()) + "");

        String commentType = replyList.get(position).getCommentType();
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

    public void addPagingData(List<Reply> replyLists) {

        replyList.clear();
        replyList.addAll(replyLists);
        notifyDataSetChanged();
    }

    public void refreshData(Reply reply) {

        if (replyList.size() > 0) {
            replyList.add(0, reply);
            size = replyList.size();
            notifyDataSetChanged();
        } else {
            replyList.add(reply);
            size = replyList.size();
            notifyDataSetChanged();
        }
    }

    public void updateData(Reply reply, int position) {

        if (replyList.size() > 0) {
            replyList.set(position, reply);
            size = replyList.size();
            notifyItemChanged(position);
        } else {
            replyList.set(position, reply);
            size = replyList.size();
            notifyItemChanged(position);
        }
    }

    public interface CallBack {
        void myCommentCallBack();
    }

    public void deleteItem(int position) {


        // comment_list.remove(position);
        // comment_list.remove(position);

        //   comment_list.remove(position);
        size = replyList.size();
        //   notifyItemRemoved(position);
        notifyDataSetChanged();

    }


}
