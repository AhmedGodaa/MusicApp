package com.examplez.musicapp.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.examplez.musicapp.models.Music;

import java.util.ArrayList;

public class MusicService extends Service {
    IBinder myBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<Music> music = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind", "Method");
        return myBinder;
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
