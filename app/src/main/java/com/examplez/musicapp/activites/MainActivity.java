package com.examplez.musicapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.examplez.musicapp.R;
import com.examplez.musicapp.adapters.TapAccessorAdapter;
import com.examplez.musicapp.databinding.ActivityMainBinding;
import com.examplez.musicapp.models.Music;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TapAccessorAdapter tapAccessorAdapter;
    private ViewPager viewPager;
    private TabLayout tab;
    public static final int REQUEST_CODE = 1;
    public static ArrayList<Music> musicFiles;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        permission();




    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        } else {

            musicFiles = getAllAudio(this);
            init();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFiles = getAllAudio(this);
                init();





            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);

            }
        }
    }

    private void init() {
        viewPager = findViewById(R.id.viewpager);
        tab = findViewById(R.id.tabLayout);
        tapAccessorAdapter = new TapAccessorAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(tapAccessorAdapter);
        tab.setupWithViewPager(viewPager);
    }

    public static ArrayList<Music> getAllAudio(Context context) {
        ArrayList<Music> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID,

        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                String id = cursor.getString(5);
                Music musicFiles = new Music(path, title, artist, album, duration,id);

                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }


}