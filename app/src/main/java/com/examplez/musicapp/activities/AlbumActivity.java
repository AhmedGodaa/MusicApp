package com.examplez.musicapp.activities;

import static com.examplez.musicapp.activities.MainActivity.albumFiles;
import static com.examplez.musicapp.activities.MainActivity.musicFiles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
    Album album;
    ArrayList<Music> music;
    MusicAdapter musicAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setData();
        setListeners();
        getMusic();
        setRecyclerView();
        Toast.makeText(this, "" + music.size(), Toast.LENGTH_SHORT).show();


    }

    private void setRecyclerView() {
        binding.musicRecyclerView.setHasFixedSize(true);
        if (!(music.size() < 1)) {
            musicAdapter = new MusicAdapter(this, music, this);
            binding.musicRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            binding.musicRecyclerView.setAdapter(musicAdapter);

        }
    }

    private void getMusic() {
        music = new ArrayList<>();
        for (int i = 0; i < musicFiles.size(); i++) {
            if (musicFiles.get(i).getAlbum().equals(album.getAlbum())) {
                music.add(musicFiles.get(i));

            }

        }
    }

    private void setData() {
        Intent intent = getIntent();
        album = albumFiles.get(intent.getIntExtra(Constants.KEY_POSITION, 0));
        String albumDetails = album.getNumberOfSongs() + " Songs";
        binding.albumName.setText(album.getAlbum());
        binding.albumAuthor.setText(album.getArtist());
        binding.albumDetails.setText(albumDetails);

    }


    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onMusicClicked(Music musicFiles, int position) {

    }


//    private void setImage(Album album) {
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(album.getPath());
//        byte[] art = retriever.getEmbeddedPicture();
//        retriever.release();
//        if (art == null) {
//            Glide.with(getApplicationContext()).load(R.drawable.ic_launcher_background).centerCrop().into(binding.albumImage);
//        } else {
//            Glide.with(getApplicationContext()).load(art).centerCrop().into(binding.albumImage);
//        }
//    }
}