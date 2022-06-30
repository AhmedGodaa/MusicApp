package com.examplez.musicapp.services;

import static com.examplez.musicapp.activities.AlbumActivity.songs;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.examplez.musicapp.models.Constants;
import com.examplez.musicapp.models.Music;

import java.util.ArrayList;

public class MusicService extends Service {
    IBinder myBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<Music> music = new ArrayList<>();
    Uri uri;
    int position;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        position = Integer.parseInt(intent.getStringExtra(Constants.KEY_SERVICE_POSITION));
        playMedia(position);
        return super.onStartCommand(intent, flags, startId);
    }

    private void playMedia(int position) {
        music = songs;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (music != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }

    }

    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void createMediaPlayer(int position) {
        uri = Uri.parse(music.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }
}
