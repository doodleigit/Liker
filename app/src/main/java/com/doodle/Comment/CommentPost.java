package com.doodle.Comment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.App;
import com.doodle.Comment.adapter.AllCommentAdapter;
import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.service.CommentService;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.TextHolder;
import com.doodle.Home.view.activity.PostShare;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.PrefManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Home.service.TextHolder.COMMENT_ITEM_KEY;
import static com.doodle.Home.service.TextHolder.ITEM_KEY;

public class CommentPost extends AppCompatActivity implements View.OnClickListener {

    private List<Comment> commentList;
    private List<Comment_> comment_list;
    private RecyclerView recyclerView;
    private TextView userName;
    private Drawable mDrawable;
    private ImageView imageSendComment;
    private EditText etComment;

    public CommentService commentService;
    public PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private Context mContext;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        toolbar.setNavigationIcon(R.drawable.ic_people_black_24dp);
//        toolbar.setNavigationIcon(mDrawable);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(R.id.imageSendComment).setOnClickListener(this);
        userName = findViewById(R.id.user_name);
        etComment = findViewById(R.id.etComment);
        commentList = new ArrayList<Comment>();
        comment_list = new ArrayList<Comment_>();
        recyclerView = findViewById(R.id.recyclerView);
        CommentItem commentItem = getIntent().getExtras().getParcelable(COMMENT_ITEM_KEY);
        PostItem postItem = getIntent().getExtras().getParcelable(ITEM_KEY);
        if (commentItem == null) {
            throw new AssertionError("Null data item received!");
        }
        if (postItem == null) {
            throw new AssertionError("Null data item received!");
        }
        commentList = commentItem.getComments();
        for (Comment temp : commentList) {
            comment_list = temp.getComments();
        }
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        commentService = CommentService.mRetrofit.create(CommentService.class);

      //  Picasso.with(App.getInstance()).load(imageUrl).into(target);
        AllCommentAdapter adapter = new AllCommentAdapter(this, comment_list);
        recyclerView.setAdapter(adapter);
         postId = postItem.getSharedPostId();
        userName.setText(String.format("%s %s", postItem.getUserFirstName(), postItem.getUserLastName()));
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            mDrawable = new BitmapDrawable(App.getInstance().getResources(), bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.imageSendComment:


                Call<Comment_> call = commentService.addedComment(deviceId, profileId, token, "", "text info..",1,"0","","",postId,userIds);
                sendShareItemRequest(call);

                break;
        }
    }

    private void sendShareItemRequest(Call<Comment_> call) {

        call.enqueue(new Callback<Comment_>() {

            @Override
            public void onResponse(Call<Comment_> call, Response<Comment_> response) {

                Comment_ postShareItem = response.body();
                Log.d("Data", postShareItem.toString());
                if (postShareItem != null) {

                }

            }

            @Override
            public void onFailure(Call<Comment_> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }
}
