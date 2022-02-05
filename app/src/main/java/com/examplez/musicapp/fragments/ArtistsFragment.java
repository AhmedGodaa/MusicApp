package com.examplez.musicapp.fragments;

import static com.examplez.musicapp.activities.MainActivity.albumFiles;
import static com.examplez.musicapp.activities.MainActivity.artistFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.examplez.musicapp.R;
import com.examplez.musicapp.adapters.AlbumsAdapter;
import com.examplez.musicapp.adapters.ArtistAdapter;
import com.examplez.musicapp.databinding.FragmentArtistsBinding;
import com.examplez.musicapp.databinding.ItemContainerArtistBinding;
import com.examplez.musicapp.listeners.ArtistListener;
import com.examplez.musicapp.models.Artist;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsFragment extends Fragment implements ArtistListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentArtistsBinding binding;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistsFragment newInstance(String param1, String param2) {
        ArtistsFragment fragment = new ArtistsFragment();
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