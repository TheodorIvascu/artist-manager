package com.artistmanager.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.artistmanager.domain.Artist;
import com.artistmanager.domain.ArtistType;
import org.hibernate.HibernateException;
import com.artistmanager.service.ArtistCrud;

public class ArtistManager extends JFrame{

    private JPanel mainPanel;
    private JButton addArtist, deleteArtist, updateArtist, selectAllArtists, clear;
    private JTextField artistName, website, formYear, endYear, artistId;
    private JComboBox<ArtistType> artistType;
    private JPanel controlPanel, inputPanel;
    private JTable resultTable;
    private DefaultTableModel tableModel;


    public ArtistManager() {
        setTitle("Artist Manager");

        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        String[] columnNames = {"ID", "Name", "Type", "Formed Year", "End Year", "Website"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        resultTable.setEnabled(false);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultTable.setCellSelectionEnabled(true);
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        resultTable.setPreferredScrollableViewportSize(new Dimension(500, resultTable.getRowHeight() * 7));
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        artistType = new JComboBox<>(ArtistType.values());
        addArtist = new JButton("Add");
        updateArtist = new JButton("Update");
        deleteArtist = new JButton("Delete");
        selectAllArtists = new JButton("Return All Artists");
        clear = new JButton("Clear");

        controlPanel.add(artistType);
        controlPanel.add(addArtist);
        controlPanel.add(updateArtist);
        controlPanel.add(deleteArtist);
        controlPanel.add(selectAllArtists);
        controlPanel.add(clear);

        inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        artistId = new JTextField();
        artistName = new JTextField();
        website = new JTextField();
        formYear = new JTextField();
        endYear = new JTextField();

        inputPanel.add(new JLabel("Artist ID:"));
        inputPanel.add(artistId);
        inputPanel.add(new JLabel("Artist Name:"));
        inputPanel.add(artistName);
        inputPanel.add(new JLabel("Official Website:"));
        inputPanel.add(website);
        inputPanel.add(new JLabel("Formed Year:"));
        inputPanel.add(formYear);
        inputPanel.add(new JLabel("End Year:"));
        inputPanel.add(endYear);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);




        selectAllArtists.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, Void> sw = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {


                        try{

                            List<Artist> artistList = ArtistCrud.getAllArtistsList();
                            tableModel.setRowCount(0);

                            for (Artist artist : artistList) {
                                tableModel.addRow(new Object[]{
                                        artist.getId(),
                                        artist.getName(),
                                        artist.getArtistType(),
                                        artist.getFormationYear(),
                                        artist.getEndYear() != null ? artist.getEndYear() : "N/A",
                                        artist.getWebsite()
                                });
                            }

                        }catch (Exception e){
                            System.out.println(e.getMessage());

                            JOptionPane.showMessageDialog(null, "Failed to load artists!");
                        }



                        return null;
                    }
                };

                sw.execute();
            }
        });




        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setNumRows(0);
                artistName.setText("");
                artistId.setText("");
                formYear.setText("");
                endYear.setText("");
                website.setText("");
            }
        });

        deleteArtist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = artistId.getText().trim();
                if (!idText.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idText);
                        int option = JOptionPane.showOptionDialog(
                                null,
                                "Are you sure you want to delete this artist?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                new String[]{"YES", "NO"},
                                "NO"
                        );
                        if (option == JOptionPane.YES_OPTION) {
                            String artistDetails = ArtistCrud.findArtistById(id);

                            SwingWorker sw = new SwingWorker() {
                                @Override
                                protected Object doInBackground() throws Exception {

                                    try{
                                        ArtistCrud.deleteArtist(id);
                                        JOptionPane.showMessageDialog(null,
                                                artistDetails + "\nArtist with ID " + id + " was deleted.");

                                    }catch(Exception e){
                                        System.out.println(e.getMessage());
                                        JOptionPane.showMessageDialog(null, "Delete action was not completed!");

                                    }
                                    return null;
                                }
                            };

                            sw.execute();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid ID: Must be a number.",
                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                    } catch (HibernateException exception) {
                        JOptionPane.showMessageDialog(null,
                                exception.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "ID field is empty. Enter a valid ID.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addArtist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = artistName.getText().trim();
                String idStr = artistId.getText().trim();
                String websiteStr = website.getText().trim();
                String formYearStr = formYear.getText().trim();
                String endYearStr = endYear.getText().trim();

                if (!idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Do not enter an ID. It is automatically assigned by the database.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Artist name cannot be empty.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int releaseYear;
                try {
                    releaseYear = Integer.parseInt(formYearStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Formed year must be a valid number.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Integer endYearVal = null;
                if (!endYearStr.isEmpty() && !endYearStr.equalsIgnoreCase("N/A")) {
                    try {
                        endYearVal = Integer.parseInt(endYearStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,
                                "End year must be a number or left empty.",
                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                ArtistType selectedType = (ArtistType) artistType.getSelectedItem();
                Artist artist = new Artist(selectedType, name, releaseYear, endYearVal, websiteStr);

               SwingWorker sw = new SwingWorker() {
                   @Override
                   protected Object doInBackground() throws Exception {

                       try{

                           ArtistCrud.insertArtist(artist);

                           JOptionPane.showMessageDialog(null,
                                   "Artist added successfully!",
                                   "Success", JOptionPane.INFORMATION_MESSAGE);

                       }catch (Exception e){
                           System.out.println(e.getMessage());
                           JOptionPane.showMessageDialog(null, "Cannot add artist!");

                       }

                       return null;



                   }


               };

               sw.execute();


            }
        });

        updateArtist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = artistId.getText().trim();
                String name = artistName.getText().trim();
                String websiteStr = website.getText().trim();
                String formedStr = formYear.getText().trim();
                String endStr = endYear.getText().trim();
                ArtistType type = (ArtistType) artistType.getSelectedItem();

                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Select a row from the table first (ID field is empty).",
                            "Validation", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Artist name cannot be empty.",
                            "Validation", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id;
                int formedYear;
                Integer endYearVal = null;

                try {
                    id = Integer.parseInt(idStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "ID must be a number.",
                            "Validation", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    formedYear = Integer.parseInt(formedStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Formed year must be a number.",
                            "Validation", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!endStr.isEmpty() && !endStr.equalsIgnoreCase("N/A")) {
                    try {
                        endYearVal = Integer.parseInt(endStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,
                                "End year must be a number or left empty.",
                                "Validation", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Integer finalEndYearVal = endYearVal;
                SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {

                        try{
                            ArtistCrud.updateArtist(id, type, name, formedYear, finalEndYearVal, websiteStr);

                            tableModel.setRowCount(0);
                            for (Artist a : ArtistCrud.getAllArtistsList()) {
                                tableModel.addRow(new Object[]{
                                        a.getId(),
                                        a.getName(),
                                        a.getArtistType(),
                                        a.getFormationYear(),
                                        a.getEndYear() != null ? a.getEndYear() : "N/A",
                                        a.getWebsite()
                                });
                            }

                        }catch (Exception e){
//                            System.out.println(e.getMessage());
                            JOptionPane.showMessageDialog(null, "Artist with this id was not found!!");
                        }

                        ArtistCrud.findArtistObjectById(id);
                        JOptionPane.showMessageDialog(null,
                                "Artist updated successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    }
                };
                sw.execute();
            }
        });
    }


}
