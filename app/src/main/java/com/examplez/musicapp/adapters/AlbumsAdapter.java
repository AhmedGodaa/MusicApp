package com.examplez.musicapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.examplez.musicapp.databinding.ItemContainerAlbumBinding;
import com.examplez.musicapp.listeners.AlbumListener;
import com.examplez.musicapp.models.Album;
import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {
    private Context context;
    private ArrayList<Album> albumsFiles;
    private AlbumListener listener;

    public AlbumsAdapter(Context context, ArrayList<Album> albumsFiles, AlbumListener listener) {
        this.context = context;
        this.albumsFiles = albumsFiles;
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
        holder.setAlbumsData(albumsFiles.get(position), position);
    }

    @Override
    public int getItemCount() {
        return albumsFiles.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        ItemContainerAlbumBinding binding;

        public AlbumViewHolder(@NonNull ItemContainerAlbumBinding itemContainerAlbumBinding) {
            super(itemContainerAlbumBinding.getRoot());
            binding = itemContainerAlbumBinding;

        }


        void setAlbumsData(Album album, int position) {
            binding.albumName.setText(album.getAlbum());
            binding.artist.setText(album.getArtist());
            String numberOfSongs = " ● " + album.getNumberOfSongs() + " Songs ";
            binding.albumSize.setText(numberOfSongs);
            binding.albumImage.setImageDrawable(Drawable.createFromPath(album.getAlbumImage()));
            binding.getRoot().setOnClickListener(view -> {
                listener.onAlbumClicked(album, position);

            });


        }

    }


}
