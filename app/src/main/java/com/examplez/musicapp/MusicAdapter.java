package com.examplez.musicapp;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.databinding.ItemContainerMusicBinding;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private Context context;
    private ArrayList<MusicFiles> musicFiles;
    private MusicListener musicListener;

    public MusicAdapter(Context context, ArrayList<MusicFiles> musicFiles, MusicListener musicListener) {
        this.context = context;
        this.musicFiles = musicFiles;
        this.musicListener = musicListener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerMusicBinding itemContainerMusicBinding = ItemContainerMusicBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new MusicViewHolder(itemContainerMusicBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.setMusicData(musicFiles.get(position));



    }


    @Override
    public int getItemCount() {
        return musicFiles.size();
    }


    class MusicViewHolder extends RecyclerView.ViewHolder {
        ItemContainerMusicBinding binding;

        MusicViewHolder(@NonNull ItemContainerMusicBinding itemContainerMusicBinding) {
            super(itemContainerMusicBinding.getRoot());
            binding = itemContainerMusicBinding;

        }

        void setMusicData(MusicFiles musicFiles) {
            binding.musicTitle.setText(musicFiles.getTitle());
            binding.getRoot().setOnClickListener(v -> musicListener.onMusicClicked(musicFiles));

            byte[] image = getAlbumArt(musicFiles.getPath());
            if (image == null) {
                Glide.with(context).load(R.drawable.ic_launcher_background).centerCrop().into(binding.audioImage);
            } else {
                Glide.with(context).load(image).centerCrop().into(binding.audioImage);
            }


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
