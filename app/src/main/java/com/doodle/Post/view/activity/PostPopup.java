package com.doodle.Post.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.doodle.App;
import com.doodle.Home.adapter.PostAdapter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.holder.ImageHolder;
import com.doodle.Home.holder.LinkScriptHolder;
import com.doodle.Home.holder.LinkScriptYoutubeHolder;
import com.doodle.Home.holder.TextHolder;
import com.doodle.Home.holder.TextMimHolder;
import com.doodle.Home.holder.VideoHolder;
import com.doodle.Post.view.fragment.MultipleMediaPopUpFragment;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;
import java.util.List;

public class PostPopup extends AppCompatActivity {

    PostItem postItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_popup);

        postItem = getIntent().getExtras().getParcelable(AppConstants.ITEM_KEY);

        initialFragment(new MultipleMediaPopUpFragment());
    }

    private void initialFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.ITEM_KEY, postItem);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
    }
}
