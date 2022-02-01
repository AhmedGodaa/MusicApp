package com.examplez.musicapp.listeners;

import com.examplez.musicapp.models.Music;

public interface MusicListener {
    void onMusicClicked(Music musicFiles);

    void musicListener(int position);
}
