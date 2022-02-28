package com.examplez.musicapp.fragments;

import static com.examplez.musicapp.activities.MainActivity.artistFiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.examplez.musicapp.adapters.ArtistAdapter;
import com.examplez.musicapp.databinding.FragmentArtistsBinding;
import com.examplez.musicapp.listeners.ArtistListener;
import com.examplez.musicapp.models.Artist;


public class ArtistsFragment extends Fragment implements ArtistListener {


    private FragmentArtistsBinding binding;

    public ArtistsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistsBinding.inflate(getLayoutInflater());
        setRecyclerView();
        return binding.getRoot();
    }

    private void setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);

        if (!(artistFiles.size() < 1)) {
            ArtistAdapter artistAdapter = new ArtistAdapter(artistFiles, getContext(), this);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            binding.recyclerView.setAdapter(artistAdapter);
        }


    }

    @Override
    public void onArtistClicked(Artist artist) {

    }
}