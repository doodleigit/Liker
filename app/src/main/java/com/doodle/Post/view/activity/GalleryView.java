package com.doodle.Post.view.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.doodle.R;

public class GalleryView extends AppCompatActivity implements View.OnClickListener {


    private VideoView videoPlay;
    private Toolbar toolbar;
    private ImageView imagePlayVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galleryview);
        imagePlayVideo = findViewById(R.id.imagePlayVideo);
        imagePlayVideo.setOnClickListener(this);
        findViewById(R.id.imageCancelVideoPage).setOnClickListener(this);

        videoPlay = findViewById(R.id.videoPlay);
        String videoPath = getIntent().getExtras().getString("video");
        videoPlay.setVideoPath(videoPath);
        videoPlay.start();

        videoPlay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                imagePlayVideo.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void onClick(View v) {
        int myId = v.getId();
        switch (myId) {
            case R.id.imagePlayVideo:
                videoPlay.start();
                imagePlayVideo.setVisibility(View.GONE);
                break;
            case R.id.imageCancelVideoPage:
                finish();
                break;
        }
    }
}
