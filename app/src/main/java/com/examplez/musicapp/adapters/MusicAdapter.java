package com.examplez.musicapp.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.models.Music;
import com.examplez.musicapp.listeners.MusicListener;
import com.examplez.musicapp.R;
import com.examplez.musicapp.databinding.ItemContainerMusicBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    Context context;
    private ArrayList<Music> musicFiles;
    MusicListener musicListener;

    public MusicAdapter(Context context, ArrayList<Music> musicFiles, MusicListener musicListener) {
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

        void setMusicData(Music musicFiles, int position) {
            binding.musicTitle.setText(musicFiles.getTitle());
            binding.musicArtist.setText(musicFiles.getArtist());
            binding.getRoot().setOnClickListener(v -> musicListener.onClick(musicFiles, position));
            setImage(musicFiles, binding);
            binding.getRoot().setOnLongClickListener(v -> {
                moreClicked(v, position);
                return true;
            });

        }
    }

    private void setImage(Music musicFiles, ItemContainerMusicBinding binding) {

        byte[] art = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(musicFiles.getPath());
            art = retriever.getEmbeddedPicture();
            retriever.release();
        } catch (Exception e) {

        }

        if (art == null) {
            Glide.with(context).load(R.drawable.ic_launcher_background).centerCrop().into(binding.audioImage);
        } else {
            Glide.with(context).load(art).centerCrop().into(binding.audioImage);
        }
    }

    private void moreClicked(View v, int position) {

        PopupMenu popupMenu = new PopupMenu(context, v, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.delete:
                    delete(position, v);
                    break;

            }
            return true;
        });


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

    public void updateList(ArrayList<Music> musicArrayList) {
        musicFiles = new ArrayList<>();
        musicFiles.addAll(musicArrayList);
        notifyDataSetChanged();
    }

}
