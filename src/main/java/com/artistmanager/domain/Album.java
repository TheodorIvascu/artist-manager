package com.artistmanager.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "album_name", nullable = false)
    private String title;

    @Column(name = "album_release_year", nullable = false)
    private int releaseYear;

    @Column(name = "record_studio")
    private String recordStudio;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artists_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Artist artist;

    public Album(String title, int releaseYear, String recordStudio) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.recordStudio = recordStudio;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getRecordStudio() {
        return recordStudio;
    }

    public void setRecordStudio(String recordStudio) {
        this.recordStudio = recordStudio;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }


    public int getId() {
        return id;
    }

    public Album(){

    }

    @Override
    public String toString() {
        return String.format(
                        "Album: title='%s', releaseYear=%d, recordStudio='%s'",
                title, releaseYear, recordStudio);
    }

}
