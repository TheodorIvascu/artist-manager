package com.artistmanager.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {


    private static SessionFactory sessionFactory;



    public static SessionFactory createSessionFactory(){
        Configuration configuration = new Configuration();
        return sessionFactory = configuration.configure().buildSessionFactory();
    }



    public static void closeSession() {
        if(sessionFactory != null){
            sessionFactory.close();
            System.out.println("Session has been closed!");
        }
    }

}
