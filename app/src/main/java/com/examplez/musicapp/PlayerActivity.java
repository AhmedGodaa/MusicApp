package com.examplez.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.databinding.ActivityPlayerBinding;

public class PlayerActivity extends AppCompatActivity {
    private ActivityPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvDurationTotal.setText(getIntent().getStringExtra(Constants.KEY_DURATION));



        byte[] image = getAlbumArt(getIntent().getStringExtra(Constants.KEY_PATH));
        if (image == null) {
            Glide.with(PlayerActivity.this).load(R.drawable.ic_launcher_background).centerCrop().into(binding.songImageContainer);
        } else {
            Glide.with(PlayerActivity.this).load(image).centerCrop().into(binding.songImageContainer);
        }
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}