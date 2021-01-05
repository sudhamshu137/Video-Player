package com.example.mp4player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.io.File;

public class player extends AppCompatActivity {

    String path;

    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btfullscreen;
    SimpleExoPlayer simpleExoPlayer;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progress_bar);
        btfullscreen = findViewById(R.id.bt_fullscreen);

        Intent i = getIntent();
        //set orientation as portrait
        //set flag value false
        if(i.hasExtra("path")){
            path = i.getStringExtra("path");

            File file=new File(path);
            Uri localUri=Uri.fromFile(file);

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(), new DefaultLoadControl());

            MediaSource mediaSource = buildMediaSource(localUri);

            playerView.setPlayer(simpleExoPlayer);

            simpleExoPlayer.prepare(mediaSource);

            simpleExoPlayer.setPlayWhenReady(true);

        }
        else{
            path = i.getStringExtra("url");

            //video url
            Uri videoUrl = Uri.parse(path);
//        Uri videoUrl = Uri.parse("Internal storage/WhatsApp/Media/WhatsApp Video/VID-20201019-WA0030.mp4");

            //Initialize load control
            LoadControl loadControl = new DefaultLoadControl();

            //Initialize bandwidth
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            //Initialize trackselector
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            //initialize simple exo-player
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(player.this,trackSelector,loadControl);

            //initialize data source factory
            DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");

            //initialize extractors factory
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            //initialize media source
            MediaSource mediaSource = new ExtractorMediaSource(videoUrl,factory,extractorsFactory,null,null);

            //set player
            playerView.setPlayer(simpleExoPlayer);

            //keep screen awake
            playerView.setKeepScreenOn(true);

            //prepare media
            simpleExoPlayer.prepare(mediaSource);

            //play video when ready
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    //condition
                    if(playbackState==Player.STATE_BUFFERING){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });

        }
        btfullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    btfullscreen.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_fullscreen_enter));

                    //set orientation as portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    //set flag value false
                    flag = false;
                }
                else{
                    btfullscreen.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_fullscreen_exit));

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    flag = true;
                }
            }
        });


    }




    @Override
    protected void onPause() {
        super.onPause();

        //stop video when ready
        simpleExoPlayer.setPlayWhenReady(false);

        //get playback state
        simpleExoPlayer.getPlaybackState();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //play video when ready
        simpleExoPlayer.setPlayWhenReady(true);

        //get playback state
        simpleExoPlayer.getPlaybackState();

    }



















    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(player.this,"ua"),
                new DefaultExtractorsFactory(),null,null);

    }







}