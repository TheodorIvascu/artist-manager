package com.artistmanager.service;

import com.artistmanager.config.HibernateUtil;
import com.artistmanager.domain.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

public class ArtistCrud {

    public static List<Artist> getAllArtistsList() {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            return session.createQuery("from Artist", Artist.class).list();
        }
    }

    public static void insertArtist(Artist artist) {
        Transaction tx = null;
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(artist);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Could not make insert: " + e.getMessage());
        }
    }

    public static void deleteArtist(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Artist artist = session.get(Artist.class, id);
            if (artist != null) {
                session.remove(artist);
            } else {
                throw new HibernateException("Artist with ID " + id + " not found.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new HibernateException("Could not delete artist: " + e.getMessage(), e);
        }
    }

    public static Artist updateArtist(int artistId, ArtistType artistType, String name,
                                      int releaseYear, Integer endYear, String website) {
        Transaction tx = null;
        Artist result = null;
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Artist artist = session.get(Artist.class, artistId);
            if (artist == null) {
                throw new HibernateException("ID not found");
            }

            artist.setArtistType(artistType);
            artist.setName(name);
            artist.setFormationYear(releaseYear);
            artist.setEndYear(endYear);
            artist.setWebsite(website);

            tx.commit();
            result = artist;
            System.out.println("Artist updated successfully: " + artist.getName());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error updating artist: " + e.getMessage());
        }
        return result;
    }


    public static String findArtistById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.createSessionFactory().openSession();
            Artist artist = session.get(Artist.class, id);
            if (artist != null) {
                return artist.toString();
            } else {
                throw new HibernateException("Error: No artist was found with ID: " + id);
            }
        } catch (HibernateException e) {
            return "ERROR: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error occurred: " + e.getMessage();
        } finally {
            if (session != null) session.close();
        }
    }

    public static Artist findArtistObjectById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.createSessionFactory().openSession();
            Artist artist = session.get(Artist.class, id);
            if (artist != null) {
                return artist;
            } else {
                throw new HibernateException("Error: No artist was found with ID: " + id);
            }
        } finally {
            if (session != null) session.close();
        }
    }
}
