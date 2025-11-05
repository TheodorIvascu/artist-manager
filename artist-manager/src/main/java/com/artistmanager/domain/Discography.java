package com.artistmanager.domain;

import java.util.Set;

public class Discography {
    private final Artist artist;
    private final Set<Album> albums;

    public Discography(Artist artist, Set<Album> albums) {
        this.artist = artist;
        this.albums = albums;
    }


    public Artist getArtist() {
        return artist;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    @Override
    public String toString() {
        return "Discography{" +
                "artist=" + artist +
                ", albums=" + albums +
                '}';
    }
}

