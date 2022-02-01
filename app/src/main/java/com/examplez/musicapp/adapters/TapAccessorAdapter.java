package com.examplez.musicapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.examplez.musicapp.fragments.AlbumFragment;
import com.examplez.musicapp.fragments.SongsFragment;


public class TapAccessorAdapter extends FragmentPagerAdapter {
    public TapAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                SongsFragment songsFragment = new SongsFragment();
                return  songsFragment ;
            case 1 :
                AlbumFragment albumFragment = new AlbumFragment();
                return  albumFragment ;

            default: return  null ;

        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return "Songs" ;
            case 1 :
                return  "Albums" ;
            default:
                return null ;
        }

    }
}
