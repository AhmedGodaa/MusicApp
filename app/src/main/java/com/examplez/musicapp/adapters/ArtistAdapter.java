package com.examplez.musicapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.examplez.musicapp.databinding.ItemContainerArtistBinding;
import com.examplez.musicapp.listeners.ArtistListener;
import com.examplez.musicapp.models.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private ArrayList<Artist> data;
    private Context context;
    private ArtistListener modelListener;

    public ArtistAdapter(ArrayList<Artist> data, Context context, ArtistListener modelListener) {
        this.context = context;
        this.data = data;
        this.modelListener = modelListener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerArtistBinding binding = ItemContainerArtistBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);


        return new ArtistViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        holder.setArtistData(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder {
        ItemContainerArtistBinding binding;


        public ArtistViewHolder(@NonNull ItemContainerArtistBinding itemContainerArtistBinding) {
            super(itemContainerArtistBinding.getRoot());
            binding = itemContainerArtistBinding;


        }

        public void setArtistData(Artist model, int position) {
            binding.artistName.setText(model.getArtist());
//            binding.numberOfAlbums.setText(model.getNumberOfAlbums());
            binding.numberOfTracks.setText(model.getNumberOfTracks());

        }
    }
}
