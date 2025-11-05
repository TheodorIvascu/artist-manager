package com.artistmanager.service;

import com.artistmanager.config.HibernateUtil;
import com.artistmanager.domain.Album;
import com.artistmanager.domain.Artist;
import com.artistmanager.domain.ArtistType;
import com.artistmanager.domain.Discography;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

public class ArtistConsoleQueries {

    public static String selectAllArtists() {
        Session session = null;
        try {
            session = HibernateUtil.createSessionFactory().openSession();
            Query<Artist> query = session.createQuery("from Artist", Artist.class);
            List<Artist> artists = query.list();

            StringBuilder sb = new StringBuilder();
            if (artists != null) {
                sb.append("Number of artists returned: ").append(artists.size()).append("\n\n");
                for (Artist artist : artists) {
                    sb.append(artist.toString()).append("\n");
                    Set<Album> albumsInfo = artist.getAlbums();
                    sb.append("  Albums (").append(albumsInfo.size()).append("):\n");
                    for (Album album : albumsInfo) {
                        sb.append("    ").append(album.toString()).append("\n");
                    }
                    sb.append("--------------------------------------------------\n");
                }
            }
            return sb.toString();
        } finally {
            if (session != null) session.close();
        }
    }

    static void selectInfoArtistDiscography(int id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.createSessionFactory().openSession();
            tx = session.beginTransaction();

            Artist artist = session.get(Artist.class, id);
            if (artist == null) {
                System.out.println("Artist with ID " + id + " not found.");
            } else {
                Set<Album> albumSet = artist.getAlbums();
                Discography discography = new Discography(artist, albumSet);
                System.out.println(discography);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error returning results: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }


    public static void updateArtistsAlbum(int id, Album album) {
        Transaction tx = null;
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Artist artist = session.get(Artist.class, id);
            if (artist != null) {
                Set<Album> albumSet = artist.getAlbums();
                if (!albumSet.contains(album)) {
                    albumSet.add(album);
                    artist.setAlbums(albumSet);
                }
                tx.commit();
                System.out.println("Artist updated successfully: " + artist.getName());
            } else {
                System.out.println("Artist with ID " + id + " not found.");
                if (tx != null) tx.rollback();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error updating artist: " + e.getMessage());
        }
    }

    static void selectArtistAfterType(String type) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.createSessionFactory().openSession();
            tx = session.beginTransaction();

            Query<Artist> q = session.createQuery("from Artist where artistType = :inputType", Artist.class);
            q.setParameter("inputType", ArtistType.valueOf(type.toLowerCase()));
            List<Artist> list = q.list();
            if (list != null) list.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error returning results: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

    static void selectArtistsYear(int year) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.createSessionFactory().openSession();
            tx = session.beginTransaction();

            Query<Artist> q = session.createQuery("from Artist where formationYear >= :year", Artist.class);
            q.setParameter("year", year);
            List<Artist> list = q.list();
            if (list != null) list.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error returning results: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

}
