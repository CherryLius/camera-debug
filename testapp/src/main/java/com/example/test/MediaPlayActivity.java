package com.example.test;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

public class MediaPlayActivity extends AppCompatActivity {

    VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        String path = "/storage/sdcard0/Android/data/com.newer.kt/cache/01和足球宝宝赛跑.mp4";
        mVideoView.setVideoPath(path);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
    }
}
