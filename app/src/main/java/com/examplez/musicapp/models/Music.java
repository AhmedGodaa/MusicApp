package com.examplez.musicapp.models;

public class Music {
    private String album;
    private String title;
    private String duration;
    private String path;
    private String artist;
    private String id;
    private String genre;

    public Music(String album, String title, String duration, String path, String artist, String id, String genre) {
        this.album = album;
        this.title = title;
        this.duration = duration;
        this.path = path;
        this.artist = artist;
        this.id = id;
        this.genre = genre;
    }


    public String getPath() {
        return path;
    }


    public String getTitle() {
        return title;
    }


    public String getArtist() {
        return artist;
    }


    public String getAlbum() {
        return album;
    }


    public String getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }

}
