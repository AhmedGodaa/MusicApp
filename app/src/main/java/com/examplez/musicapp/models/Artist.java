package com.examplez.musicapp.models;

public class Artist {
    private String artistId;
    private String artist;
    private String numberOfTracks;
    private String numberOfAlbums;

    public Artist(String artistId, String artist, String numberOfTracks, String numberOfAlbums) {
        this.artistId = artistId;
        this.artist = artist;
        this.numberOfTracks = numberOfTracks;
        this.numberOfAlbums = numberOfAlbums;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtist() {
        return artist;
    }

    public String getNumberOfTracks() {
        return numberOfTracks;
    }

    public String getNumberOfAlbums() {
        return numberOfAlbums;
    }
}
