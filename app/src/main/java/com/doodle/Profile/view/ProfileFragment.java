package com.doodle.Profile.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doodle.R;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private View view;
    private ImageView ivProfileImage, ivImage;
    private TextView tvUserName, tvTotalInfoCount;
    private LinearLayout followLayout, moreLayout;
    private CardView addPostLayout;
    private RecyclerView recyclerView;

    private String profileUserId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileUserId = Objects.requireNonNull(getArguments()).getString("user_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        ivProfileImage = view.findViewById(R.id.profile_image);
        ivImage = view.findViewById(R.id.image);
        tvUserName = view.findViewById(R.id.user_name);
        tvTotalInfoCount = view.findViewById(R.id.total_info_count);
        followLayout = view.findViewById(R.id.follow_layout);
        moreLayout = view.findViewById(R.id.more_layout);
        addPostLayout = view.findViewById(R.id.add_post_layout);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

}
