package com.artistmanager.domain;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
public class Artist {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "artist_type", nullable = false, columnDefinition = "enum('solo', 'band')")
    @Enumerated(EnumType.STRING)
    private ArtistType artistType;

    @Column(name = "artist_name", nullable = false)
    private String name;

    @Column(name = "release_year", nullable = false)
    private int formationYear;

    @Column(name = "end_year")
    private Integer endYear;

    @Column(name = "official_website")
    private String website;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Album> albums = new LinkedHashSet<>();

    public Artist() {

    }


    public Artist(ArtistType artistType, String name, int formationYear, Integer endYear, String website) {
        this.artistType = artistType;
        this.name = name;
        this.formationYear = formationYear;
        this.endYear = endYear;
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;
        Artist other = (Artist) o;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }



    //Getters/Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArtistType getArtistType() {
        return artistType;
    }

    public void setArtistType(ArtistType artistType) {
        this.artistType = artistType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFormationYear() {
        return formationYear;
    }

    public void setFormationYear(int formationYear) {
        this.formationYear = formationYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    @Override
    public String toString() {

        return String.format(
                "Artist: id=%d, artistType=%s, name='%s', release_year=%d, end_year=%d, website='%s'",
                id, artistType, name, formationYear, endYear, website);
    }

}
