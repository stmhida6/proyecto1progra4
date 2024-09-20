package org.proyecto.util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;


public class Util
{
    //clase con util para el proyecto

    //constructor de la clase
    public Util()
    {

    }

    public static boolean  esFormatoCSVCorrecto(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String line;
            String[] values;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            while ((line = br.readLine()) != null) {
                values = line.split(",");
                if (values.length != 7) {
                    return false; // Número incorrecto de columnas
                }

                // Validación básica de tipos de datos
                if (!Pattern.matches("\\d+", values[0]) ||
                        !Pattern.matches("\\d+-\\d+", values[1]) ||
                        !Pattern.matches("\\d+-\\d+", values[2]) ||
                       // !(sdf.parse(values[3], null) == null) || // Validación de fecha y hora
                        !Pattern.matches("\\d+", values[4]) ||
                        !Pattern.matches("\\d+\\.\\d+", values[5]) ||
                        !Pattern.matches("internacional|nacional|local", values[6])) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean dividirArchivo(String rutaArchivo) {
        try {
            // Borrar los archivos part1.csv, part2.csv, part3.csv si existen
            java.io.File file1 = new java.io.File("src/main/resources/part1.csv");
            java.io.File file2 = new java.io.File("src/main/resources/part2.csv");
            java.io.File file3 = new java.io.File("src/main/resources/part3.csv");
            if (file1.exists()) {
                file1.delete();
            }
            if (file2.exists()) {
                file2.delete();
            }
            if (file3.exists()) {
                file3.delete();
            }

            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(rutaArchivo));
            java.util.List<String> lines = new java.util.ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            int totalLines = lines.size();
            int partSize = totalLines / 3;

            for (int i = 0; i < 3; i++) {
                int start = i * partSize;
                int end = (i == 2) ? totalLines : (i + 1) * partSize;
                java.io.FileWriter writer = new java.io.FileWriter("src/main/resources/part" + (i + 1) + ".csv");
                for (int j = start; j < end; j++) {
                    writer.write(lines.get(j) + "\n");
                }
                writer.close();
            }

            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();

            return false;
        }
    }


}
