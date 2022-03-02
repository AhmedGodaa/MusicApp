package com.examplez.musicapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.examplez.musicapp.databinding.ItemContainerAlbumSongsBinding;
import com.examplez.musicapp.listeners.MusicListener;
import com.examplez.musicapp.models.Music;

import java.util.ArrayList;

public class AlbumSongsAdapter extends RecyclerView.Adapter<AlbumSongsAdapter.AlbumSongsViewHolder> {
    ArrayList<Music> musicList;
    MusicListener musicListener;

    public AlbumSongsAdapter(ArrayList<Music> musicList, MusicListener musicListener) {
        this.musicList = musicList;
        this.musicListener = musicListener;
    }

    @NonNull
    @Override
    public AlbumSongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerAlbumSongsBinding binding = ItemContainerAlbumSongsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlbumSongsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumSongsViewHolder holder, int position) {
        holder.setAlbumSongsData(musicList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class AlbumSongsViewHolder extends RecyclerView.ViewHolder {
        ItemContainerAlbumSongsBinding binding;

        public AlbumSongsViewHolder(@NonNull ItemContainerAlbumSongsBinding itemContainerAlbumSongsBinding) {
            super(itemContainerAlbumSongsBinding.getRoot());
            binding = itemContainerAlbumSongsBinding;
        }

        void setAlbumSongsData(Music music, int position) {
            binding.songName.setText(music.getTitle());
            binding.songDuration.setText(music.getDuration());
            musicListener.onMusicClicked(music, position);
        }
    }
}
