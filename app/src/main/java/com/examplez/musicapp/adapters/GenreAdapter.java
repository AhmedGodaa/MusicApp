package com.examplez.musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.examplez.musicapp.databinding.ItemContainerGenreBinding;
import com.examplez.musicapp.listeners.GenreListener;
import com.examplez.musicapp.models.Genre;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    ArrayList<Genre> data;
    Context context;
    GenreListener modelListener;

    public GenreAdapter(ArrayList<Genre> data, Context context, GenreListener modelListener) {
        this.context = context;
        this.data = data;
        this.modelListener = modelListener;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerGenreBinding binding = ItemContainerGenreBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);


        return new GenreViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.setGenreData(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {
        ItemContainerGenreBinding binding;


        public GenreViewHolder(@NonNull ItemContainerGenreBinding itemContainerGenreBinding) {
            super(itemContainerGenreBinding.getRoot());
            binding = itemContainerGenreBinding;


        }

        public void setGenreData(Genre model, int position) {
            modelListener.onClick(model, position);

        }
    }
}
