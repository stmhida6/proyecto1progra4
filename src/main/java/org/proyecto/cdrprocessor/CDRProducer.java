package org.proyecto.cdrprocessor;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CDRProducer implements Runnable {
    private final BufferCompartido buffer;

    private final String rutaArchivo;

    private final String idProductor;
    private JTextArea textAreaConsola;
    private JLabel labelP;


    public CDRProducer(BufferCompartido buffer, String rutaArchivo, String idProductor, JTextArea textAreaConsola,JLabel labelP) {
        this.buffer = buffer;
        this.rutaArchivo = rutaArchivo;
        this.idProductor = idProductor;
        this.textAreaConsola = textAreaConsola;
        this.labelP = labelP;

    }

    @Override
    public void run() {
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(rutaArchivo)))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String mensaje = linea + "," + idProductor;
                buffer.producir(mensaje);
                String cuenta = mensaje.split(",")[0];
                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy"));

                textAreaConsola.append("Producido por  " + idProductor + " -Cuenta: " + cuenta +" Hora:"+ currentDateTime +"\n");
                System.out.println("Producido por  " + idProductor + " : " + mensaje+ " en el archivo: "+rutaArchivo);
                labelP.setText("Producciones: " + (Integer.parseInt(labelP.getText().replaceAll("\\D", "")) + 1));


             //   Thread.sleep((int) (Math.random() * 1000));
            //    mainPanel.printToConsole("Producido por  " + idProductor + " : " + mensaje+ " en el archivo: "+rutaArchivo);



            }
            System.out.println("fin de archivo");
            buffer.setProducersActive(false);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        } //finally {
//            try {
//                buffer.producir("END," + idProductor);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }

        //      }

    }


}
