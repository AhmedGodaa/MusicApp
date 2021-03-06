package com.examplez.musicapp.fragments;


import static com.examplez.musicapp.activities.MainActivity.albumFiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.examplez.musicapp.activities.AlbumActivity;
import com.examplez.musicapp.adapters.AlbumsAdapter;
import com.examplez.musicapp.databinding.FragmentAlbumBinding;
import com.examplez.musicapp.listeners.AlbumListener;
import com.examplez.musicapp.models.Album;
import com.examplez.musicapp.models.Constants;


public class AlbumFragment extends Fragment implements AlbumListener {
    private FragmentAlbumBinding binding;


    AlbumsAdapter albumsAdapter;


    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlbumBinding.inflate(getLayoutInflater());
        setRecyclerView();


        return binding.getRoot();

    }

    private void setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);
        if (!(albumFiles.size() < 1)) {
            albumsAdapter = new AlbumsAdapter(albumFiles, this, getContext());
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            binding.recyclerView.setAdapter(albumsAdapter);
        }


    }


    @Override
    public void onClick(Album album, int position) {
        Intent intent = new Intent(getContext(), AlbumActivity.class);
        intent.putExtra(Constants.KEY_POSITION, position);
        startActivity(intent);

    }
}