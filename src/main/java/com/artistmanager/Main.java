package com.artistmanager;

import com.artistmanager.ui.ArtistManager;

import javax.swing.*;


public class Main {


    public static void main(String[] args) {


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ArtistManager artistManager = new ArtistManager();
            }
        });












    }


}
