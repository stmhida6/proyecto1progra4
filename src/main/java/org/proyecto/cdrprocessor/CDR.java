package org.proyecto.cdrprocessor;


import org.proyecto.gui.MainPanel;

import javax.swing.*;
import java.io.IOException;

public class CDR {
    private MainPanel mainPanel;

    //constructor
    public CDR(MainPanel mainPanel) {


        this.mainPanel = mainPanel;

    }
    // metodo publica para iniciar el procesamiento de los CDR
    public void procesarCDR() {
        mainPanel.printToConsole("¡Procesado!");

        BufferCompartido buffer = new BufferCompartido(10);




        Thread productor1 = new Thread(new CDRProducer(buffer, "part1.csv", "Productor 1"));
        Thread productor2 = new Thread(new CDRProducer(buffer, "part2.csv", "Productor 2"));
        Thread productor3 = new Thread(new CDRProducer(buffer, "part3.csv", "Productor 3"));
        productor1.start();
        productor2.start();
        productor3.start();

        Thread consumidor1 = null;
        Thread consumidor2 = null;

        try {
            consumidor1 = new Thread(new CDRConsumer(buffer, "Consumidor 1"));
            consumidor2 = new Thread(new CDRConsumer(buffer, "Consumidor 2"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (consumidor1 != null) {
            consumidor1.start();
        }
        if (consumidor2 != null) {
            consumidor2.start();
        }

        try {
            productor1.join();
            productor2.join();
            productor3.join();
            if (consumidor1 != null) {
                consumidor1.join();
            }
            if (consumidor2 != null) {
                consumidor2.join();
            }
            //matar hilos

            //consumidor1.interrupt();

            //consumidor2.interrupt();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
