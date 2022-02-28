package com.examplez.musicapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.examplez.musicapp.R;
import com.examplez.musicapp.adapters.TapAccessorAdapter;
import com.examplez.musicapp.databinding.ActivityMainBinding;
import com.examplez.musicapp.fragments.SongsFragment;
import com.examplez.musicapp.models.Album;
import com.examplez.musicapp.models.Artist;
import com.examplez.musicapp.models.Music;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ActivityMainBinding binding;
    TapAccessorAdapter tapAccessorAdapter;
    ViewPager viewPager;
    TabLayout tab;
    public static final int REQUEST_CODE = 1;
    public static ArrayList<Music> musicFiles;
    public static ArrayList<Album> albumFiles;
    public static ArrayList<Artist> artistFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        permission();


        navigationView();


    }

    private void navigationView() {


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//                binding.navigationView.setNavigationItemSelectedListener(this);
        View headerView = binding.navigationView.getHeaderView(0);
        TextView headerAlbums = headerView.findViewById(R.id.headerAlbums);
        TextView headerSongs = headerView.findViewById(R.id.headerSongs);
        TextView headerArtist = headerView.findViewById(R.id.headerArtist);

        headerSongs.setText(String.valueOf(musicFiles.size()));
        headerAlbums.setText(String.valueOf(albumFiles.size()));
        headerArtist.setText(String.valueOf(artistFiles.size()));

    }


    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        } else {

            musicFiles = getAllAudio(this);
            albumFiles = getAllAlbums(this);
            artistFiles = getAllArtists(this);
            init();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFiles = getAllAudio(this);
                albumFiles = getAllAlbums(this);
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
                Music musicFiles = new Music(path, title, artist, album, duration, id);


                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }

    public static ArrayList<Album> getAllAlbums(Context context) {
        ArrayList<Album> tempAlbumList = new ArrayList<>();

        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
        };
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String album = cursor.getString(1);
                String artist = cursor.getString(2);
                String albumImage = cursor.getString(3);
                String numberOfSongs = cursor.getString(4);
                Album albumFiles = new Album(id, album, artist, albumImage, numberOfSongs);
                tempAlbumList.add(albumFiles);

            }
            cursor.close();

        }
        return tempAlbumList;
    }

    public static ArrayList<Artist> getAllArtists(Context context) {
        ArrayList<Artist> tempArtistList = new ArrayList<>();

        String[] projection = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
        };
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String artist = cursor.getString(1);
                String numberOfTracks = cursor.getString(2);
                String numberOfAlbums = cursor.getString(3);
                Artist artistFiles = new Artist(id, artist, numberOfTracks, numberOfAlbums);
                tempArtistList.add(artistFiles);

            }
            cursor.close();

        }
        return tempArtistList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String userInput = s.toLowerCase();
        ArrayList<Music> mFiles = new ArrayList<>();
        for (Music song : musicFiles) {
            if (song.getTitle().toLowerCase().contains(userInput)) {
                mFiles.add(song);
            }
        }
        SongsFragment songsFragment = new SongsFragment();
        songsFragment.musicAdapter.updateList(mFiles);

        return true;
    }
}