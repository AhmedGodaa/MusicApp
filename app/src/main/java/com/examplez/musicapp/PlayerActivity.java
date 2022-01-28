package com.examplez.musicapp;

import static com.examplez.musicapp.MainActivity.musicFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.databinding.ActivityPlayerBinding;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    private ActivityPlayerBinding binding;
    private static ArrayList<MusicFiles> listSongs;
    int position = -1;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listSongs = new ArrayList<>();
        musicActions();
        setSeekBar();
        setSongData();


    }

    private void setSongData() {

        binding.songName.setText(getIntent().getStringExtra(Constants.KEY_TITLE));

        binding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));
        byte[] image = getAlbumArt(getIntent().getStringExtra(Constants.KEY_PATH));
        if (image == null) {
            Glide.with(PlayerActivity.this).load(R.drawable.ic_launcher_background).centerCrop().into(binding.songImageContainer);
        } else {
            Glide.with(PlayerActivity.this).load(image).centerCrop().into(binding.songImageContainer);
        }

    }

    private void setSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekBar.setProgress(mCurrentPosition);
                    binding.tvDurationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });


    }

    private void musicActions() {
        listSongs = musicFiles;
        if (listSongs != null) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(getIntent().getStringExtra(Constants.KEY_PATH));
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);

    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                binding.btnPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       buttonPlayPause();
                    }
                });
            }


        };
        playThread.start();
    }

    private void buttonPlayPause() {
        if (mediaPlayer.isPlaying()) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_play);
            binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
            mediaPlayer.pause();
//
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        if (mediaPlayer != null) {
                            int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                            binding.seekBar.setProgress(mCurrentPosition);
                        }
                        handler.postDelayed(this, 1000);
                }
            });
//

        } else {
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            binding.seekBar.setMax(mediaPlayer.getDuration()/1000 );
            mediaPlayer.start();


            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }


    private void nextThreadBtn() {
    }

    private void prevThreadBtn() {
    }


    //    private void metaData(Uri uri){
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(uri.toString());
//        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration());
//        binding.tvDurationTotal.setText(formattedTime(durationTotal));
//        byte[] art = retriever.getEmbeddedPicture();
//
//
//    }
}