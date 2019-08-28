package com.doodle.Post.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.doodle.App;
import com.doodle.Home.adapter.BreakingPostAdapter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.ImageHolder;
import com.doodle.Home.service.LinkScriptHolder;
import com.doodle.Home.service.LinkScriptYoutubeHolder;
import com.doodle.Home.service.TextHolder;
import com.doodle.Home.service.TextMimHolder;
import com.doodle.Home.service.VideoHolder;
import com.doodle.Home.view.fragment.BreakingPost;
import com.doodle.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;
import java.util.List;

public class PostPopup extends AppCompatActivity
        implements TextHolder.PostItemListener,
        TextMimHolder.PostItemListener,
        VideoHolder.PostItemListener,
        LinkScriptYoutubeHolder.PostItemListener,
        LinkScriptHolder.PostItemListener,
        ImageHolder.PostItemListener {

    private SlidrInterface slidr;
    private PostItem popupPostItem;
    public List<PostItem> postItemList;
 private RecyclerView rvPopupPost;


    private BreakingPostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_popup);
        postItemList = new ArrayList<>();
        rvPopupPost=findViewById(R.id.rvPopupPost);
        slidr = Slidr.attach(this);
        popupPostItem = new PostItem();
        popupPostItem = getIntent().getExtras().getParcelable(TextHolder.ITEM_KEY);
        postItemList.add(popupPostItem);
        int pos = App.getPosition();

        if (popupPostItem == null) {
            throw new AssertionError("Null data item received!");
        }

        adapter = new BreakingPostAdapter(this, postItemList, this, this, this, this, this, this);
        rvPopupPost.setAdapter(adapter);

    }

    @Override
    public void deletePost(PostItem postItem, int position) {

    }
}
