package com.examplez.musicapp.activities;

import static com.examplez.musicapp.activities.MainActivity.musicFiles;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.examplez.musicapp.adapters.AlbumSongsAdapter;
import com.examplez.musicapp.databinding.ActivityAlbumBinding;
import com.examplez.musicapp.listeners.AlbumListener;
import com.examplez.musicapp.listeners.MusicListener;
import com.examplez.musicapp.models.Album;
import com.examplez.musicapp.models.Music;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity implements AlbumListener, MusicListener {
    ActivityAlbumBinding binding;
    AlbumSongsAdapter albumSongsAdapter;
    String albumName;
    ArrayList<Music> albumSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerView.setHasFixedSize(true);
        albumSongs = new ArrayList<>();

        getAlbumSongs();
        setRecyclerView();


    }

    private void setRecyclerView() {
        albumSongsAdapter = new AlbumSongsAdapter(albumSongs,this);
        binding.recyclerView.setAdapter(albumSongsAdapter);
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager());
    }

    private void getAlbumSongs() {
        int j = 0;
        for (int i = 0; i < musicFiles.size(); i++) {
            if (albumName.equals(musicFiles.get(i).getAlbum())) {
                albumSongs.add(j, musicFiles.get(i));
                j++;
            }
        }
    }

    @Override
    public void onClick(Album album, int position) {
        albumName = album.getAlbum();
        Toast.makeText(this, albumName, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onClick(Music music, int position) {

    }
}