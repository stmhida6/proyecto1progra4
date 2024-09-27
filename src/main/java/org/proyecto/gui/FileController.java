package org.proyecto.gui;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class FileController {
    public static String seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home") + "/Downloads"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        fileChooser.showOpenDialog(null);
        if (fileChooser.getSelectedFile() == null) return null;
        return fileChooser.getSelectedFile().getAbsolutePath();
    }

    public static int contarLineas(String filePath) {
        int count = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while (reader.readLine() != null) count++;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }
}
