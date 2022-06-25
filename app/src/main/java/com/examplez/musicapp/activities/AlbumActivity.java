package com.examplez.musicapp.activities;

import static com.examplez.musicapp.activities.MainActivity.albumFiles;
import static com.examplez.musicapp.activities.MainActivity.musicFiles;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.examplez.musicapp.adapters.MusicAdapter;
import com.examplez.musicapp.databinding.ActivityAlbumBinding;
import com.examplez.musicapp.listeners.MusicListener;
import com.examplez.musicapp.models.Album;
import com.examplez.musicapp.models.Constants;
import com.examplez.musicapp.models.Music;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity implements MusicListener {
    ActivityAlbumBinding binding;
    int albumPosition;
    Album album;
    ArrayList<Music> songs;
    MusicAdapter musicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setData();
        setSongs();
        setRecyclerView();


    }


    private void setSongs() {
        songs = new ArrayList<>();
        for (int i = 0; i < musicFiles.size(); i++) {
            if (musicFiles.get(i).getAlbum().equals(album.getAlbum())) {
                songs.add(musicFiles.get(i));
            }
        }
    }


    private void setData() {
        albumPosition = getIntent().getIntExtra(Constants.KEY_POSITION, 0);
        album = albumFiles.get(albumPosition);
        String details = "" + album.getNumberOfSongs() + " Songs";
        binding.albumName.setText(album.getAlbum());
        binding.albumAuthor.setText(album.getArtist());
        binding.albumDetails.setText(details);
    }

    private void setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);
        if (!(songs.size() < 1)) {
            musicAdapter = new MusicAdapter(this, songs, this);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            binding.recyclerView.setAdapter(musicAdapter);

        }

    }


    @Override
    public void onClick(Music music, int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        Music _music = musicFiles.get(position);
        intent.putExtra(Constants.KEY_POSITION, position);
        intent.putExtra(Constants.KEY_TITLE, _music.getTitle());
        intent.putExtra(Constants.KEY_ALBUM, _music.getAlbum());
        intent.putExtra(Constants.KEY_ARTIST, _music.getArtist());
        intent.putExtra(Constants.KEY_PATH, _music.getPath());
        intent.putExtra(Constants.KEY_DURATION, _music.getDuration());
        startActivity(intent);


    }


}