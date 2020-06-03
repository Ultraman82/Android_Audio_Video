package com.example.videoaudio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private VideoView mVideoView;
    private Button btnPlayVideo, btnPlayMusic, btnPauseMusic;
    private MediaPlayer mMediaPlayer;
    private SeekBar volumeSeekBar, moveSeekBar;

    private MediaController mMediaController;
    private AudioManager mAudioManager;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = findViewById(R.id.myVideoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPause);

        btnPlayVideo.setOnClickListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(MainActivity.this);
        btnPlayMusic.setOnClickListener(MainActivity.this);


        mMediaController = new MediaController(MainActivity.this);
        mMediaPlayer = MediaPlayer.create(this, R.raw.firststep);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        volumeSeekBar = findViewById(R.id.seekBar);
        moveSeekBar = findViewById(R.id.seekBarMove);

        int maximumVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        int currentvolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar.setMax(maximumVolume);
        volumeSeekBar.setProgress(currentvolume);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //Toast.makeText(MainActivity.this, Integer.toString(progress), Toast.LENGTH_SHORT ).show();
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



//        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/"+R.raw.sunrise));
//                mVideoView.setMediaController(new MediaController(MainActivity.this));


//                mVideoView.requestFocus();
//                mVideoView.start();
//            }
//        });
        moveSeekBar.setOnSeekBarChangeListener(MainActivity.this);
        moveSeekBar.setMax(mMediaPlayer.getDuration());
        mMediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()) {
            case R.id.btnPlayVideo:
                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sunrise);
                mVideoView.setVideoURI(videoUri);
                mVideoView.setMediaController(mMediaController);
                mMediaController.setAnchorView(mVideoView);
                mVideoView.start();
                break;
            case R.id.btnPlayMusic:
                mMediaPlayer.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        moveSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    }
                }, 0, 1000);
                break;
            case R.id.btnPause:
                mMediaPlayer.pause();
                timer.cancel();
                break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
           mMediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mMediaPlayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();
        Toast.makeText(this, "Music is finished", Toast.LENGTH_SHORT).show();
    }
}