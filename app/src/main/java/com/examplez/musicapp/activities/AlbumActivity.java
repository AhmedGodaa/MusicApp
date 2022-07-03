package com.examplez.musicapp.activities;

import static com.examplez.musicapp.activities.MainActivity.albumFiles;
import static com.examplez.musicapp.activities.MainActivity.musicFiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.R;
import com.examplez.musicapp.adapters.MusicAdapter;
import com.examplez.musicapp.databinding.ActivityAlbumBinding;
import com.examplez.musicapp.listeners.MusicListener;
import com.examplez.musicapp.models.Album;
import com.examplez.musicapp.models.Constants;
import com.examplez.musicapp.models.Music;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity implements MusicListener {
    ActivityAlbumBinding binding;
    MusicAdapter musicAdapter;
    Album album;
    int albumPosition;
    public static ArrayList<Music> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setData();
        setSongs();
        setImage();
        setRecyclerView();
        binding.imageBack.setOnClickListener(v -> onBackPressed());


    }


    private void setSongs() {
        album = albumFiles.get(albumPosition);
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
    private void setImage(){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(songs.get(0).getPath());
        byte[] image = retriever.getEmbeddedPicture();
        if (image != null){
            Glide.with(this).load(image).centerCrop().into(binding.albumImage);
            Glide.with(this).load(image).centerCrop().into(binding.albumContainer);


          Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            Palette.from(bitmap).generate(palette -> {
                assert palette != null;
                Palette.Swatch swatch = palette.getDominantSwatch();
                GradientDrawable gradientDrawable;
                if (swatch != null) {

                    gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{swatch.getRgb(), 0x00000000});


                } else {
                    gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{0xff000000, 0xff000000});

                }
                binding.albumLayer.setBackground(gradientDrawable);
            });



        }else {
            Glide.with(this).load(R.drawable.im_logo).centerCrop().into(binding.albumImage);

        }

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
        Music _music = songs.get(position);
        intent.putExtra(Constants.KEY_POSITION, position);
        intent.putExtra(Constants.KEY_TITLE, _music.getTitle());
        intent.putExtra(Constants.KEY_ALBUM, _music.getAlbum());
        intent.putExtra(Constants.KEY_ARTIST, _music.getArtist());
        intent.putExtra(Constants.KEY_PATH, _music.getPath());
        intent.putExtra(Constants.KEY_DURATION, _music.getDuration());
        startActivity(intent);


    }


}