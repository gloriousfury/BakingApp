/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.gloriousfury.bakingapp.ui.activity;

import android.annotation.SuppressLint;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gloriousfury.bakingapp.R;
import com.gloriousfury.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;



public class SingleStepActivity extends AppCompatActivity implements View.OnClickListener {

    Toast mCurrentToast;


    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    // autoplay = false
    private boolean autoPlay = false;

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screen


    // used to remember the playback position
    private int currentWindow;
    private long playbackPosition;

    // constant fields for saving and restoring bundle
    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";
    public static final String LIFECYCLE_CALLBACK_VIDEO_URL= "video_url";

    String videoUrl, getStepString;
    TextView stepDescription, previousTextView, nextTextView;
    ArrayList<Step> stepArrayList = new ArrayList<>();
    int stepPosition;
    RelativeLayout backView, previousView, nextView;
    Step singleStep;
    ImageView videoError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_step);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        mPlayerView = (SimpleExoPlayerView) findViewById(R.id.media_view);
        videoError = (ImageView) findViewById(R.id.img_video_error);
        stepDescription = (TextView) findViewById(R.id.step_long_description);
        backView = (RelativeLayout) findViewById(R.id.rl_back);
        previousView = (RelativeLayout) findViewById(R.id.rl_previous_step);
        nextTextView = (TextView) findViewById(R.id.tv_next_step);
        previousTextView = (TextView) findViewById(R.id.tv_previous_step);
        nextView = (RelativeLayout) findViewById(R.id.rl_next_step);
        backView.setOnClickListener(this);
        previousView.setOnClickListener(this);
        nextView.setOnClickListener(this);


        stepArrayList = getIntent().getExtras().getParcelableArrayList("StepArrayList");
        stepPosition = getIntent().getIntExtra("StepPosition", 0);

        singleStep = stepArrayList.get(stepPosition);

        // Retrieve list index values that were sent through an intent; use them to display the desired Android-Me body part image
        // Use setListindex(int index) to set the list index for all BodyPartFragments
        showToast(String.valueOf(stepPosition));

        // Only create new fragments when there is no previously saved state
        if (savedInstanceState == null|| !savedInstanceState.containsKey(LIFECYCLE_CALLBACK_VIDEO_URL)) {
            changeStepView(stepPosition);



        } else {

            changeStepView(stepPosition);
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
            mPlayer.seekTo(currentWindow, playbackPosition);

        }

    }


    void initializePlayer(String videoUrl) {
        // create a new instance of SimpleExoPlayer
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        // attach the just created player to the view responsible for displaying the media (i.e. media controls, visual feedback)
        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(autoPlay);

        // resume playback position
        mPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);

        // now we are ready to start playing our media files
        mPlayer.prepare(mediaSource);
    }


    /*
    * This method returns ExtractorMediaSource or one of its compositions
    * ExtractorMediaSource is suitable for playing regular files like (mp4, mp3, webm etc.)
    * This is appropriate for the baking app project, since all recipe videos are not in adaptive formats (i.e. HLS, Dash etc)
    */
    public static MediaSource buildMediaSource(Uri uri) {
        DefaultExtractorsFactory extractorSourceFactory = new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ua");

//        ExtractorMediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        // this return a single mediaSource object. i.e. no next, previous buttons to play next/prev media file
//        return new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        /*
         * Uncomment the line below to play multiple meidiaSources in sequence aka playlist (and totally without buffering!)
         * NOTE: you have to comment the return statement just above this comment
         */
//         ExtractorMediaSource videoSource1 = new ExtractorMediaSource(Uri.parse(VIDEO_1), dataSourceFactory, extractorSourceFactory, null, null);


        return new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);
        // returns a mediaSource collection
//          ConcatenatingMediaSource(videoSource1, audioSource, videoSource2);


    }


    // save app state before all members are gone forever :D
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
        * A simple configuration change such as screen rotation will destroy this activity
        * so we'll save the player state here in a bundle (that we can later access in onCreate) before everything is lost
        * NOTE: we cannot save player state in onDestroy like we did in onPause and onStop
        * the reason being our activity will be recreated from scratch and we would have lost all members (e.g. variables, objects) of this activity
        */
        if (mPlayer == null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
            outState.putString(LIFECYCLE_CALLBACK_VIDEO_URL, videoUrl);
        }
    }

    // This is just an implementation detail to have a pure full screen experience. Nothing fancy here
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    private void releasePlayer() {
        if (mPlayer != null) {
            // save the player state before releasing its resources
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            autoPlay = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // start in pure full screen
//        hideSystemUi();

    }


    /**
     * Before API level 24 we release player resources early
     * because there is no guarantee of onStop being called before the system terminates our app
     * remember onPause means the activity is partly obscured by something else (e.g. incoming call, or alert dialog)
     * so we do not want to be playing media while our activity is not in the foreground.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    // API level 24+ we release the player resources when the activity is no longer visible (onStop)
    // NOTE: On API 24+, onPause is still visible!!! So we do not not want to release the player resources
    // this is made possible by the new Android Multi-Window Support https://developer.android.com/guide/topics/ui/multi-window.html
    // We stop playing media on API 24+ only when our activity is no longer visible aka onStop
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    void showToast(String text) {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
        }
        mCurrentToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        mCurrentToast.show();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rl_back:

                onBackPressed();
                break;
            case R.id.rl_next_step:

                if (stepPosition >= 0 && stepPosition < stepArrayList.size() - 1) {
                    stepPosition++;
                    changeStepView(stepPosition);

                }

                break;

            case R.id.rl_previous_step:

                if (stepPosition >=0) {
                    stepPosition--;
                    changeStepView(stepPosition);

                }

                break;

            default:


        }


    }

    public void changeStepView(int position) {

        if (position == (stepArrayList.size() - 1)) {

            nextTextView.setTextColor(ContextCompat.getColor(this, R.color.light_grey));



        }else if(position == 0){

            previousTextView.setTextColor(ContextCompat.getColor(this, R.color.light_grey));
        }else{
            nextTextView.setTextColor(ContextCompat.getColor(this, R.color.grey));
            previousTextView.setTextColor(ContextCompat.getColor(this, R.color.grey));

        }


        singleStep = stepArrayList.get(position);

        videoUrl = singleStep.getVideoURL();
        getStepString = singleStep.getDescription();


        if (getStepString != null) {
            stepDescription.setText(getStepString);

        }

        if ((!TextUtils.isEmpty(videoUrl))) {
            videoError.setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(videoUrl);
        }else {
            videoError.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.INVISIBLE);

        }


    }
}
