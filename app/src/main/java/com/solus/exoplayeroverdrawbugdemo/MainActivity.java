package com.solus.exoplayeroverdrawbugdemo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {

    private SimpleExoPlayer mPlayer;

    private SimpleExoPlayerView mPlayerView1;
    private SimpleExoPlayerView mPlayerView2;

    private MediaSource mMediaSource;

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerView1 = findViewById(R.id.playerView1);
        mPlayerView2 = findViewById(R.id.playerView2);

        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        mPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        mPlayer.setPlayWhenReady(true);

        mPlayerView1.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        mPlayerView2.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        mMediaSource = buildMediaSource("https://i.imgur.com/EmWHjBC.mp4");

        mPlayerView1.setPlayer(mPlayer);
        mPlayer.prepare(mMediaSource);
        mPlayer.setPlayWhenReady(true);

        mButton = findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerView1.setPlayer(null);
                mPlayerView2.setPlayer(mPlayer);
            }
        });
    }

    private MediaSource buildMediaSource(String url) {
        Uri uri = Uri.parse(url);

        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }
}
