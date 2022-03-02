package com.examplez.musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.examplez.musicapp.databinding.ItemContainerPlaylistBinding;
import com.examplez.musicapp.listeners.PlaylistListener;
import com.examplez.musicapp.models.Playlist;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    ArrayList<Playlist> data;
    Context context;
    PlaylistListener modelListener;

    public PlaylistAdapter(ArrayList<Playlist> data, Context context, PlaylistListener modelListener) {
        this.context = context;
        this.data = data;
        this.modelListener = modelListener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerPlaylistBinding binding = ItemContainerPlaylistBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);


        return new PlaylistViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.setPlaylistData(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ItemContainerPlaylistBinding binding;


        public PlaylistViewHolder(@NonNull ItemContainerPlaylistBinding itemContainerPlaylistBinding) {
            super(itemContainerPlaylistBinding.getRoot());
            binding = itemContainerPlaylistBinding;


        }

        public void setPlaylistData(Playlist model, int position) {
            modelListener.onClick(model, position);

        }
    }
}
