package org.proyecto.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel   {

    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextArea textAreaConsola;
    private JButton buttonAbrir;
    private JTextField textFieldPath;
    private JButton buttonIniciar;


    public MainPanel() {
        buttonAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home") + "/Downloads"));
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
                fileChooser.showOpenDialog(null);
                if (fileChooser.getSelectedFile() == null) {
                    return;
                }
                String file = fileChooser.getSelectedFile().getAbsolutePath();

                textFieldPath.setText(file);
                buttonIniciar.setEnabled(true);
                textAreaConsola.setText("File selected: " + file);
                textAreaConsola.append("\n");
                textAreaConsola.append("Size file: " + fileChooser.getSelectedFile().length() + " bytes");

                int count = 0;
                try {
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                    while (reader.readLine() != null) count++;
                    reader.close();
                } catch (java.io.IOException e1) {
                    e1.printStackTrace();
                }

                textAreaConsola.append(count + " lines");

                textAreaConsola.append("\n");

            }
        });


    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Proyecto Progra 4 - CDR");
        frame.setContentPane(new MainPanel().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // resize the frame
        frame.setSize(600, 400);
        //center the frame
        frame.setLocationRelativeTo(null);
        // make the frame visible
        frame.setVisible(true);
    }

}
