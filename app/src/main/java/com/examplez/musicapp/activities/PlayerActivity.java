package com.examplez.musicapp.activities;

import static com.examplez.musicapp.activities.MainActivity.musicFiles;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.bumptech.glide.Glide;


import com.examplez.musicapp.R;
import com.examplez.musicapp.databinding.ActivityPlayerBinding;
import com.examplez.musicapp.models.Constants;
import com.examplez.musicapp.models.Music;

import java.util.ArrayList;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class PlayerActivity extends AppCompatActivity {
    private ActivityPlayerBinding binding;
    private static ArrayList<Music> listSongs;
    int position = -1;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    private ObjectAnimator anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        uri = Uri.parse(getIntent().getStringExtra(Constants.KEY_PATH));
        setContentView(binding.getRoot());
        listSongs = new ArrayList<>();
        listSongs = musicFiles;
        musicActions();
        setSeekBar();
        setSongData();



        anim = ObjectAnimator.ofFloat(binding.songImageContainer, "rotation", 0, 360);
        anim.setDuration(15000);
        anim.setRepeatCount(5);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatMode(ObjectAnimator.RESTART);
        anim.start();


    }


    private void setSongData() {
        binding.songName.setText(getIntent().getStringExtra(Constants.KEY_TITLE));
        binding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));
        metaData(uri);
    }

    private void setSeekBar() {

        binding.seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float i, boolean b) {
                if (mediaPlayer != null && b) {
                    int c = (int) i;
                    mediaPlayer.seekTo(c * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

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
        if (listSongs != null) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
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


    private void buttonPlayPause() {
        if (mediaPlayer.isPlaying()) {
            anim.pause();
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
            anim.resume();
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
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

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                binding.btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonNext();
                    }
                });
            }


        };
        nextThread.start();

    }

    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonPrevious();
                    }
                });
            }


        };
        prevThread.start();
    }

    private void buttonNext() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            metaData(uri);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            binding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
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
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();


        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            metaData(uri);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            binding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);

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
            binding.btnPlayPause.setImageResource(R.drawable.ic_play);


        }
    }


    private void buttonPrevious() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? listSongs.size() - 1 : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            metaData(uri);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            binding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
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
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();


        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? listSongs.size() - 1 : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            metaData(uri);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            binding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));


            binding.songName.setText(listSongs.get(position).getTitle());
            binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);

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
            binding.btnPlayPause.setImageResource(R.drawable.ic_play);


        }

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

    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;

        if (art != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(binding.songImageContainer);


            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null) {

                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), 0x00000000});
                        binding.background.setBackground(gradientDrawable);


                    } else {
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0xff000000});
                        binding.background.setBackground(gradientDrawable);

                    }
                }
            });
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.ic_launcher_background)
                    .into(binding.songImageContainer);
        }

    }


}