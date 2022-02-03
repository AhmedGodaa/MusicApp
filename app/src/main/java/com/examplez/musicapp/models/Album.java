package com.examplez.musicapp.models;

import android.provider.MediaStore;

public class Album {
    private String id;
    private String album;
    private String artist;
    private String albumImage;
    private String numberOfSongs;

    public String getId() {
        return id;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public String getNumberOfSongs() {
        return numberOfSongs;
    }


    public Album(String id, String album, String artist, String albumImage, String numberOfSongs) {
        this.id = id;
        this.album = album;
        this.artist = artist;
        this.albumImage = albumImage;
        this.numberOfSongs = numberOfSongs;
    }

}
