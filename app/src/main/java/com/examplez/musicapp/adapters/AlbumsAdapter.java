package com.examplez.musicapp.adapters;

import static com.examplez.musicapp.activities.MainActivity.albumFiles;
import static com.examplez.musicapp.activities.MainActivity.musicFiles;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.R;
import com.examplez.musicapp.databinding.ItemContainerAlbumBinding;
import com.examplez.musicapp.listeners.AlbumListener;
import com.examplez.musicapp.models.Album;
import com.examplez.musicapp.models.Music;

import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {
    ArrayList<Album> albumsFiles;
    AlbumListener listener;
    Context context;

    ArrayList<Music> songs;


    public AlbumsAdapter(ArrayList<Album> albumsFiles, AlbumListener listener, Context context) {
        this.albumsFiles = albumsFiles;
        this.listener = listener;
        this.context = context;
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
            getAlbumAudio(position, album);
            binding.albumName.setText(album.getAlbum());
            binding.artist.setText(album.getArtist());
            String numberOfSongs = " ● " + album.getNumberOfSongs() + " Songs ";
            binding.albumSize.setText(numberOfSongs);
            setImage(songs.get(0), binding);
            binding.albumImage.setImageDrawable(Drawable.createFromPath(album.getAlbumImage()));
            binding.getRoot().setOnClickListener(view -> listener.onClick(album, position));


        }


        private void getAlbumAudio(int albumPosition, Album album) {
            album = albumFiles.get(albumPosition);
            songs = new ArrayList<>();
            for (int i = 0; i < musicFiles.size(); i++) {
                if (musicFiles.get(i).getAlbum().equals(album.getAlbum())) {
                    songs.add(musicFiles.get(i));
                    break;
                }
            }
        }


        private void setImage(Music music, ItemContainerAlbumBinding binding) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(music.getPath());
            byte[] art = retriever.getEmbeddedPicture();
            retriever.release();
            if (art == null) {
                Glide.with(context).load(R.drawable.ic_launcher_background).centerCrop().into(binding.albumImage);
            } else {
                Glide.with(context).load(art).centerCrop().into(binding.albumImage);
            }
        }


    }
}
