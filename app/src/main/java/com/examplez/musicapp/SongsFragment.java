package com.examplez.musicapp;

import static com.examplez.musicapp.MainActivity.musicFiles;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;


import androidx.core.app.ActivityOptionsCompat;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Explode;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.examplez.musicapp.databinding.FragmentSongsBinding;
import com.examplez.musicapp.databinding.ItemContainerMusicBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFragment extends Fragment implements MusicListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentSongsBinding binding;
    private MusicAdapter musicAdapter;
    private RecyclerView recyclerView;

    public SongsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFragment newInstance(String param1, String param2) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSongsBinding.inflate(getLayoutInflater());
        binding.recyclerView.setHasFixedSize(true);


        if (!(musicFiles.size() < 1)) {
            musicAdapter = new MusicAdapter(getContext(), musicFiles, this);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            binding.recyclerView.setAdapter(musicAdapter);

        }


        return binding.getRoot();
    }


    @Override
    public void onMusicClicked(MusicFiles musicFiles) {
        Intent intent = new Intent(getContext(), PlayerActivity.class);
        intent.putExtra(Constants.KEY_TITLE, musicFiles.getTitle());
        intent.putExtra(Constants.KEY_ALBUM, musicFiles.getAlbum());
        intent.putExtra(Constants.KEY_ARTIST, musicFiles.getArtist());
        intent.putExtra(Constants.KEY_PATH, musicFiles.getPath());
        intent.putExtra(Constants.KEY_DURATION, musicFiles.getDuration());
        setAnimation(intent);
    }

    private void setAnimation(Intent intent) {
        ItemContainerMusicBinding itemContainerMusicBinding = ItemContainerMusicBinding.inflate(getLayoutInflater());

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(itemContainerMusicBinding.audioImage, "imageTransition");
        Pair<View, String> p1 = Pair.create(itemContainerMusicBinding.audioImage, "imageTransition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(intent, options.toBundle());




    }
}