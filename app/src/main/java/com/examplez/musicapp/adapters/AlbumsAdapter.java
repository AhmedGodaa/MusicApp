package com.examplez.musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.examplez.musicapp.databinding.ItemContainerAlbumBinding;

import com.examplez.musicapp.listeners.MusicListener;

import com.examplez.musicapp.models.Music;

import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {
    private Context context;
    private ArrayList<Music> music;
    private MusicListener listener;

    public AlbumsAdapter(Context context, ArrayList<Music> music, MusicListener listener) {
        this.context = context;
        this.music = music;
        this.listener = listener;
    }


    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerAlbumBinding binding = ItemContainerAlbumBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AlbumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.setAlbumsData(music.get(position));
    }

    @Override
    public int getItemCount() {
        return music.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        ItemContainerAlbumBinding binding;

        public AlbumViewHolder(@NonNull ItemContainerAlbumBinding itemContainerAlbumBinding) {
            super(itemContainerAlbumBinding.getRoot());
            binding = itemContainerAlbumBinding;

        }

        void setAlbumsData(Music music) {


        }
    }
}
