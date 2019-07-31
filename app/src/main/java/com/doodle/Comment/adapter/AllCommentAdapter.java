package com.doodle.Comment.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.service.CommentHolder;
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

public class AllCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<Comment_> comment_list;
    private Context mContext;


    public AllCommentAdapter(Context context, List<Comment_> comment_list) {
        this.mContext = context;
        this.comment_list = comment_list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment_all , parent, false);
        return new CommentHolder(view, mContext);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CommentHolder) {
            CommentHolder vh = (CommentHolder) viewHolder;
            vh.setItem( comment_list.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return comment_list.size();
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


}
