package com.examplez.musicapp;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.databinding.ItemContainerMusicBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
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
        holder.setMusicData(musicFiles.get(position), position);


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

        void setMusicData(MusicFiles musicFiles, int position) {
            binding.musicTitle.setText(musicFiles.getTitle());
            binding.more.setOnClickListener(v -> {
                Toast.makeText(context, "hello " + position, Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            delete(position, v);
                            break;

                    }
                    return true;
                });

            });
            binding.getRoot().setOnClickListener(v -> {
                        musicListener.onMusicClicked(musicFiles);
                        musicListener.musicListener(position);

                    }

            );

            byte[] image = getAlbumArt(musicFiles.getPath());
            if (image == null) {
                Glide.with(context).load(R.drawable.ic_launcher_background).centerCrop().into(binding.audioImage);
            } else {
                Glide.with(context).load(image).centerCrop().into(binding.audioImage);
            }


        }


    }

    private void delete(int position, View v) {
        Uri fileUri = ContentUris.withAppendedId(MediaStore.Audio.Media.
                EXTERNAL_CONTENT_URI, Long.parseLong(musicFiles.get(position).getId()));
        File file = new File(musicFiles.get(position).getPath());
        boolean deleted = file.delete();
        if (deleted) {
            context.getContentResolver().delete(fileUri, null, null);
            musicFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, musicFiles.size());
            Snackbar.make(v, "File deleted", Snackbar.LENGTH_LONG)
                    .show();

        } else {
            Snackbar.make(v, "File not deleted", Snackbar.LENGTH_LONG)
                    .show();

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
