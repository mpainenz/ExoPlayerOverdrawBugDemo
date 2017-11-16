package com.solus.exoplayeroverdrawbugdemo;

import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class TextureViewActivity extends AppCompatActivity {

    private SimpleExoPlayer mPlayer;

    private static int VIDEO_HEIGHT = 1280;
    private static int VIDEO_WIDTH = 818;

    private TextureView mPlayerTexture1;
    private TextureView mPlayerTexture2;

    private MySurfaceListener mSurfaceListener1;
    private MySurfaceListener mSurfaceListener2;

    private MediaSource mMediaSource;

    private Button mButton;

    private boolean IsPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view);

        mPlayerTexture1 = findViewById(R.id.playerTexture1);
        mSurfaceListener1 = new MySurfaceListener();
        mPlayerTexture1.setSurfaceTextureListener(mSurfaceListener1);

        mPlayerTexture2 = findViewById(R.id.playerTexture2);
        mSurfaceListener2 = new MySurfaceListener();
        mPlayerTexture2.setSurfaceTextureListener(mSurfaceListener2);

        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        mPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        mPlayer.setPlayWhenReady(true);

        mMediaSource = buildMediaSource("https://i.imgur.com/EmWHjBC.mp4");

        mPlayer.prepare(mMediaSource);


        mButton = findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapPlayers();
            }
        });
    }

    private MediaSource buildMediaSource(String url) {
        Uri uri = Uri.parse(url);

        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }


    private void onSurfaceAvailabilityChanged() {
        if (!IsPlaying) {
            if (mSurfaceListener1.surfaceAvailable) {
                updateTextureViewScaling(mPlayerTexture1, mSurfaceListener1.width, mSurfaceListener1.height);
                mPlayer.setVideoSurface(mSurfaceListener1.videoSurface);
                mPlayer.setPlayWhenReady(true);
                IsPlaying = true;
            }
        }
    }

    private void swapPlayers() {
        if (IsPlaying) {
            if (mSurfaceListener2.surfaceAvailable) {
                updateTextureViewScaling(mPlayerTexture2, mSurfaceListener2.width, mSurfaceListener2.height);
                mPlayer.setVideoSurface(mSurfaceListener2.videoSurface);
            }
        }
    }

    private void updateTextureViewScaling(TextureView tv, int viewWidth, int viewHeight) {
        float scaleX = 1.0f;
        float scaleY = 1.0f;

        if (VIDEO_WIDTH > viewWidth && VIDEO_WIDTH > viewHeight) {
            scaleX = (float) VIDEO_WIDTH / viewWidth;
            scaleY = (float) VIDEO_HEIGHT / viewHeight;
        } else if (VIDEO_WIDTH < viewWidth && VIDEO_HEIGHT < viewHeight) {
            scaleY = (float) viewWidth / VIDEO_WIDTH;
            scaleX = (float) viewHeight / VIDEO_HEIGHT;
        } else if (viewWidth > VIDEO_WIDTH) {
            scaleY = ((float) viewWidth / VIDEO_WIDTH) / ((float) viewHeight / VIDEO_HEIGHT);
        } else if (viewHeight > VIDEO_HEIGHT) {
            scaleX = ((float) viewHeight / VIDEO_WIDTH) / ((float) viewWidth / VIDEO_WIDTH);
        }

        // Calculate pivot points, in our case crop from center
        int pivotPointX = viewWidth / 2;
        int pivotPointY = viewHeight / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY, pivotPointX, pivotPointY);

        tv.setTransform(matrix);
    }

    class MySurfaceListener implements TextureView.SurfaceTextureListener {
        boolean surfaceAvailable = false;

        Surface videoSurface;
        int width;
        int height;

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            videoSurface = new Surface(surface);
            this.width = width;
            this.height = height;

            surfaceAvailable = true;
            onSurfaceAvailabilityChanged();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    }


}
