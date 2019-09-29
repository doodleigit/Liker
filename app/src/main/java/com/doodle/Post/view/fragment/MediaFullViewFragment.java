package com.doodle.Post.view.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MediaFullViewFragment extends DialogFragment {

    View view;
    private ImageView close;
    private PhotoView photoView;
    private PlayerView videoView;

    private String postType, url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.media_full_view_fragment_layout, container, false);

        initialComponent();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initialComponent() {
        postType = getArguments().getString("post_type");
        url = getArguments().getString("url");

        close = view.findViewById(R.id.close);
        photoView = view.findViewById(R.id.photo_view);
        videoView = view.findViewById(R.id.video_view);

        if (postType.equals("2")) {
            photoView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            // Setup Exoplayer instance
            SimpleExoPlayer exoPlayerInstance = ExoPlayerFactory.newSimpleInstance(getContext());
// Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "simpleExoPlayer"));
            //Getting media from raw resource
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(AppConstants.POST_VIDEOS + url));
//Prepare the exoPlayerInstance with the source
            exoPlayerInstance.prepare(videoSource);
            exoPlayerInstance.setPlayWhenReady(true);
            videoView.setPlayer(exoPlayerInstance);
        } else {
            photoView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        }

        Glide.with(App.getAppContext())
                .load(url)
                .dontAnimate()
                .into(photoView);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView.getPlayer() != null) {
            videoView.getPlayer().setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView.getPlayer() != null) {
            videoView.getPlayer().release();
        }
    }

}
