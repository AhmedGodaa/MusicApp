package com.examplez.musicapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.examplez.musicapp.fragments.AlbumFragment;
import com.examplez.musicapp.fragments.ArtistsFragment;
import com.examplez.musicapp.fragments.GenresFragment;
import com.examplez.musicapp.fragments.PlaylistsFragment;
import com.examplez.musicapp.fragments.SongsFragment;


public class TapAccessorAdapter extends FragmentPagerAdapter {
    public TapAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SongsFragment();
            case 1:
                return new PlaylistsFragment();
            case 2:
                return new AlbumFragment();
            case 3:
                return new ArtistsFragment();
            case 4:
                return new GenresFragment();
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Songs";
            case 1:
                return "Playlists";
            case 2:
                return "Albums";
            case 3:
                return "Artists";
            case 4:
                return "Genres";
            default:
                return null;
        }

    }
}
