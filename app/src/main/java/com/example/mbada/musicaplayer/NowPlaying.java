package com.example.mbada.musicaplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class NowPlaying extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private final android.os.Handler mHandler = new Handler();
    private ArrayList<Song> songs = new ArrayList<>();
    private int currentSongId;
    private SeekBar seekBar;
    private ImageView albumArt;
    private TextView title;
    private TextView album;
    private TextView artist;
    private TextView time;
    private Button playButton;
    private Button nextButton;
    private Button previousButton;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Animation animation;
    private ViewPager viewPager;
    //The update ui runnable to be passed to the Ui thread by the handler
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int currentPos = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPos);
                time.setText(Song.formatTime(currentPos));
                mHandler.postDelayed(this, 1000); //Refresh after every second
            }

        }
    };
    private final Thread uiUpdate = new Thread(runnable);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Intent intent = getIntent();
        songs = intent.getParcelableArrayListExtra(Tags.SONGS_LIST);
        currentSongId = intent.getIntExtra(Tags.CURRENT_SONG_ID, 0);
        seekBar = findViewById(R.id.playback_progress);
        albumArt = findViewById(R.id.album_image);
        viewPager = findViewById(R.id.slider);
        myPagerAdapter pagerAdapter = new myPagerAdapter(this,songs);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        animation = AnimationUtils.loadAnimation(this,R.anim.button_bouncer);
        animation.setInterpolator(new myBounceInterpolator(0.2,20));
        // let's initialize our controls here
        playButton = findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPause();
                v.startAnimation(animation);
            }
        });
        nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
                v.startAnimation(animation);
            }
        });
        previousButton = findViewById(R.id.previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrevious();
                v.startAnimation(animation);
            }
        });
        title = findViewById(R.id.song_title);
        artist = findViewById(R.id.artist);
        album = findViewById(R.id.album);
        time = findViewById(R.id.timer);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) { //play next song in library
                currentSongId += 1;
                playSong(currentSongId);
            }
        });
        //setUi();
        playSong(currentSongId);
        viewPager.setCurrentItem(currentSongId);
    }

    //Let's make sure we clean up after ourselves now when we leave the activity by freeing resources.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiUpdate.interrupt();
        uiUpdate.getState();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    //Plays current song
    private void playSong(int songId) {
        mediaPlayer.reset();
        Song song = songs.get(songId);
        try {
            mediaPlayer.setDataSource(song.getFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(0);
            seekHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handles the seek bar and and play back time display. It runs a seperate worker
    // thread which updates the current playback info every second.
    private void seekHandler() {
        if (uiUpdate.getState() == Thread.State.NEW) {
            uiUpdate.start();
        }
    }

    //Play or pause the playing song
    private void playPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setBackgroundResource(android.R.drawable.ic_media_play);
        } else {
            mediaPlayer.start();
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    //Play next song
    private void playNext() {
        if(currentSongId != songs.size()-1){
            currentSongId += 1;
        }else if(currentSongId == songs.size()-1){
            currentSongId = 0;
        }

        if(!mediaPlayer.isPlaying()){
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
        playSong(currentSongId);
        viewPager.setCurrentItem(currentSongId);
    }

    // play previous song
    private void playPrevious() {
        if(currentSongId > 0){
            currentSongId -= 1;
        }else{
            currentSongId = songs.size() - 1;
        }

        if(!mediaPlayer.isPlaying()){
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
        playSong(currentSongId);
        viewPager.setCurrentItem(currentSongId);
    }

    //Set view pager event listeners to handle what happens when the user interacts with the view pager
    @Override
    public void onPageSelected(int position) {
        currentSongId = position;
        playSong(currentSongId);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
