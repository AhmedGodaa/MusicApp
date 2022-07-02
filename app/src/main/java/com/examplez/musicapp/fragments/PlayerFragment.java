package com.examplez.musicapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.examplez.musicapp.databinding.FragmentPlayerBinding;
import com.examplez.musicapp.models.Constants;
import com.examplez.musicapp.utils.PreferenceManager;


public class PlayerFragment extends Fragment {
    private FragmentPlayerBinding binding;
    PreferenceManager preferenceManager;
    public static boolean SHOW_MEDIA_PLAYER;


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
        preferenceManager = new PreferenceManager(requireContext());
        String uri = preferenceManager.getString(Constants.AUDIO_URI);

        if (uri != null) {
            binding.parentLayout.setVisibility(View.VISIBLE);
            setData();
        } else {
            binding.parentLayout.setVisibility(View.GONE);
        }
        return binding.getRoot();
    }


    private void setData() {
        binding.audioDescription.setText("");
    }
}