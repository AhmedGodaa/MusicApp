package com.examplez.musicapp.fragments;


import static com.examplez.musicapp.activities.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.examplez.musicapp.adapters.AlbumsAdapter;
import com.examplez.musicapp.adapters.MusicAdapter;
import com.examplez.musicapp.databinding.FragmentAlbumBinding;
import com.examplez.musicapp.listeners.MusicListener;
import com.examplez.musicapp.models.Music;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment implements MusicListener {
    private FragmentAlbumBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AlbumsAdapter albumsAdapter;


    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
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
        binding = FragmentAlbumBinding.inflate(getLayoutInflater());
        binding.recyclerView.setHasFixedSize(true);
        setRecyclerView();


        return binding.getRoot();

    }

    private void setRecyclerView() {
        if (!(musicFiles.size()  < 1)){
            albumsAdapter = new AlbumsAdapter(getContext(),musicFiles,this);
        }



    }


    @Override
    public void onMusicClicked(Music musicFiles, int position) {

    }
}