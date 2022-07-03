package com.examplez.musicapp.fragments;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.databinding.FragmentPlayerBinding;
import com.examplez.musicapp.models.Constants;


public class PlayerFragment extends Fragment {
     FragmentPlayerBinding binding;


    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayerBinding.inflate(getLayoutInflater());
        if (Constants.SHOW_PLAYER) {
            if (Constants.FRAGMENT_PATH != null) {
                byte[] image = getImage(Constants.FRAGMENT_PATH);
                Glide.with(requireContext()).load(image).into(binding.audioImage);
//                binding.audioTitle.setText();

            }
        }

        return binding.getRoot();
    }

    private byte[] getImage(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        return retriever.getEmbeddedPicture();
    }


}