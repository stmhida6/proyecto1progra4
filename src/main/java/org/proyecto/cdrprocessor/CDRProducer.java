package org.proyecto.cdrprocessor;

import org.proyecto.gui.MainPanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import static org.proyecto.gui.MainPanel;

public class CDRProducer implements Runnable {
    private final BufferCompartido buffer;

    private final String rutaArchivo;

    private final String idProductor;



    public CDRProducer(BufferCompartido buffer, String rutaArchivo, String idProductor) {
        this.buffer = buffer;
        this.rutaArchivo = rutaArchivo;
        this.idProductor = idProductor;

    }

    @Override
    public void run() {
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(rutaArchivo)))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String mensaje = linea + "," + idProductor;
                buffer.producir(mensaje);
                System.out.println("Producido por  " + idProductor + " : " + mensaje+ " en el archivo: "+rutaArchivo);
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
