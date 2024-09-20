package org.proyecto.gui;

import org.proyecto.util.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class MainPanel  extends JFrame {

    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextArea textAreaConsola;
    private JButton buttonAbrir;
    private JTextField textFieldPath;
    private JButton buttonIniciar;
    private JButton buttonLimpiar;


    public MainPanel() {
        setTitle("Proyecto Progra 4 - CDR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel1);
        pack();
        // resize the frame
        setSize(800, 400);
        //center the frame
        setLocationRelativeTo(null);
        // make the frame visible
        setVisible(true);





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
                textAreaConsola.setText("Archivo Seleccionado: " + file);
                textAreaConsola.append("\n");
                textAreaConsola.append("Tamaño del archivo: " + fileChooser.getSelectedFile().length() + " bytes");

                int count = 0;
                try {
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                    while (reader.readLine() != null) count++;
                    reader.close();
                } catch (java.io.IOException e1) {
                    e1.printStackTrace();
                }

                textAreaConsola.append(count + " lineas");

                textAreaConsola.append("\n");

            }
        });


        buttonIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!Util.esFormatoCSVCorrecto(textFieldPath.getText())) {
                    textAreaConsola.setForeground(Color.RED);
                    textAreaConsola.append("El archivo no tiene el formato correcto ");
                    textAreaConsola.append("\n");
                    textAreaConsola.setForeground(Color.BLACK);

                    // habilitar boton iniciar

                    textAreaConsola.append("\n");
                    return;
                }

                //divide  el archivo

                textAreaConsola.append("Dividiendo archivo...");
                textAreaConsola.append("\n");
                if (!Util.dividirArchivo(textFieldPath.getText())) {

                    textAreaConsola.append("Error al dividir el archivo ");
                    textAreaConsola.append("\n");

                    return;
                }
                textAreaConsola.append("Archivo dividido");
                textAreaConsola.append("\n");
                buttonIniciar.setEnabled(false);

                buttonAbrir.setEnabled(false);
                textAreaConsola.append("Iniciando procesamiento...");
                textAreaConsola.append("\n");

            }
        });
        buttonLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaConsola.setText("");
                textFieldPath.setText("");
                buttonIniciar.setEnabled(false);
                buttonAbrir.setEnabled(true);
            }
        });
    }

    // metodo que imprime en consola
    public void printToConsole(String message) {
        textAreaConsola.append(message);
        textAreaConsola.append("\n");
    }


}
