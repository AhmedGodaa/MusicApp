package com.examplez.musicapp.activities;

import static com.examplez.musicapp.activities.AlbumActivity.songs;
import static com.examplez.musicapp.models.Constants.ACTION_PLAY;
import static com.examplez.musicapp.models.Constants.ACTION_PREVIOUS;
import static com.examplez.musicapp.models.Constants.CHANNEL_ID_2;

import android.animation.ObjectAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.R;
import com.examplez.musicapp.databinding.ActivityPlayerBinding;
import com.examplez.musicapp.listeners.ActionPlaying;
import com.examplez.musicapp.models.Constants;
import com.examplez.musicapp.models.Music;
import com.examplez.musicapp.utils.MusicService;
import com.examplez.musicapp.utils.NotificationReceiver;

import java.util.ArrayList;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class PlayerActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {
    ActivityPlayerBinding binding;
    public static ArrayList<Music> listSongs = new ArrayList<>();
    int position = -1;
    int songPosition;
    static Uri uri;
    Handler handler = new Handler();
    Thread playThread, prevThread, nextThread;
    ObjectAnimator anim;
    MusicService musicService;
    MediaSessionCompat mediaSessionCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        uri = Uri.parse(getIntent().getStringExtra(Constants.KEY_PATH));
        setContentView(binding.getRoot());
        listSongs = songs;
        songPosition = getIntent().getIntExtra(Constants.KEY_POSITION, 0);
        setSongData();
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio ");
        musicServiceStart();
        binding.btnBack.setOnClickListener(v -> onBackPressed());


    }

    private void setAnimation() {
        anim = ObjectAnimator.ofFloat(binding.songImageContainer, "rotation", 0, 360);
        anim.setDuration(15000);
        anim.setRepeatCount(5);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatMode(ObjectAnimator.RESTART);
        anim.start();
    }

    private void musicServiceStart() {
        showNotification(R.drawable.ic_pause);
        Intent intent = new Intent(getBaseContext(), MusicService.class);
        intent.putExtra(Constants.KEY_SERVICE_POSITION, String.valueOf(songPosition));
        intent.putExtra(Constants.SERVICE_STARTER, false);
        startService(intent);

    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
        musicService = binder.getService();
        Toast.makeText(this, "Connected" + musicService, Toast.LENGTH_SHORT).show();
        setSeekBar();
        setAnimation();


    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;

    }

    private void setSongData() {
        binding.songName.setText(getIntent().getStringExtra(Constants.KEY_TITLE));
        binding.tvDurationTotal.setText(formattedTime(Integer.parseInt(getIntent().getStringExtra(Constants.KEY_DURATION))));
        metaData(uri);
    }

    private void setSeekBar() {
        binding.seekBar.setMax(musicService.getDuration() / 1000f);
        binding.seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float i, boolean b) {
                if (musicService != null && b) {
                    int c = (int) i;
                    musicService.seekTo(c * 1000);
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
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    binding.seekBar.setProgress(mCurrentPosition);
                    binding.tvDurationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });


    }

    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                binding.btnPlayPause.setOnClickListener(view -> buttonPlayPause());
            }


        };
        playThread.start();
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                binding.btnNext.setOnClickListener(view -> buttonNext());
            }


        };
        nextThread.start();

    }

    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                binding.btnPrevious.setOnClickListener(view -> buttonPrevious());
            }


        };
        prevThread.start();
    }

    @Override
    public void buttonPlayPause() {
        if (musicService.isPlaying()) {
            anim.pause();
            binding.btnPlayPause.setImageResource(R.drawable.ic_play);
            showNotification(R.drawable.ic_play);
            binding.seekBar.setMax(musicService.getDuration() / 1000f);
            musicService.pause();
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });

        } else {
            anim.resume();
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            showNotification(R.drawable.ic_pause);
            binding.seekBar.setMax(musicService.getDuration() / 1000f);
            musicService.start();


            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    public void buttonNext() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            metaData(uri);
            musicService.createMediaPlayer(position);
            binding.tvDurationTotal.setText(formattedTime(musicService.getDuration() / 1000));
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.seekBar.setMax(musicService.getDuration() / 1000f);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            showNotification(R.drawable.ic_pause);
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            musicService.start();


        } else {
            musicService.stop();
            musicService.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            metaData(uri);
            musicService.createMediaPlayer(position);
            binding.tvDurationTotal.setText(formattedTime(musicService.getDuration() / 1000));
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.seekBar.setMax(musicService.getDuration() / 1000f);


            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            showNotification(R.drawable.ic_play);
            binding.btnPlayPause.setImageResource(R.drawable.ic_play);


        }
    }

    public void buttonPrevious() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            position = ((position - 1) < 0 ? listSongs.size() - 1 : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            metaData(uri);
            musicService.createMediaPlayer(position);
            binding.tvDurationTotal.setText(formattedTime(musicService.getDuration() / 1000));
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.seekBar.setMax(musicService.getDuration() / 1000f);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            showNotification(R.drawable.ic_pause);
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            musicService.start();


        } else {
            musicService.stop();
            musicService.release();
            position = ((position - 1) < 0 ? listSongs.size() - 1 : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            metaData(uri);
            musicService.createMediaPlayer(position);
            binding.tvDurationTotal.setText(formattedTime(musicService.getDuration() / 1000));


            binding.songName.setText(listSongs.get(position).getTitle());
            binding.seekBar.setMax(musicService.getDuration() / 1000f);


            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            showNotification(R.drawable.ic_play);
            binding.btnPlayPause.setImageResource(R.drawable.ic_play);


        }

    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut;
        String totalNew;
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
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
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
            Palette.from(bitmap).generate(palette -> {
                Palette.Swatch swatch = palette.getDominantSwatch();
                if (swatch != null) {

                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{swatch.getRgb(), 0x00000000});
                    binding.viewBackground.setBackground(gradientDrawable);


                } else {
                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{0xff000000, 0xff000000});
                    binding.viewBackground.setBackground(gradientDrawable);

                }
            });


        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.ic_launcher_background)
                    .into(binding.songImageContainer);
        }

    }


    public void showNotification(int btnPlayPause) {
        Intent intent = new Intent(this, PlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        byte[] picture = getImage(listSongs.get(songPosition).getPath());
        Bitmap thumb;

        if (picture != null) {
            thumb = BitmapFactory.decodeByteArray(picture, 0, picture.length);


        } else {
            thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        }
        Notification notification = new androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(btnPlayPause)
                .setLargeIcon(thumb)
                .setContentTitle(listSongs.get(songPosition).getTitle())
                .setContentText(listSongs.get(songPosition).getArtist())
                .addAction(R.drawable.ic_skip_previous, "Previous", prevPending)
                .addAction(btnPlayPause, "Pause", pausePending)
                .addAction(R.drawable.ic_skip_next, "Next", nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);


    }

    private byte[] getImage(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        return retriever.getEmbeddedPicture();
    }
}