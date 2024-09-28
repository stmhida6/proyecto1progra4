package org.proyecto.cdrprocessor;


import javax.swing.*;
import java.io.IOException;

public class CDR {

    private JTextArea textAreaConsola;
    private JLabel labelP1;
    private JLabel labelP2;
    private JLabel labelP3;

    //constructor
    public CDR(JTextArea textAreaConsola, JLabel labelP1, JLabel labelP2, JLabel labelP3) {
        this.textAreaConsola = textAreaConsola;
        this.labelP1 = labelP1;
        this.labelP2 = labelP2;
        this.labelP3 = labelP3;

    }
    // metodo publica para iniciar el procesamiento de los CDR
    public void procesarCDR() {

        textAreaConsola.append("Procesando CDR...\n");
        //SwingUtilities.invokeLater(() -> textAreaConsola.append("Texto desde un hilo...\n"));

        BufferCompartido buffer = new BufferCompartido(10);

        Thread productor1 = new Thread(new CDRProducer(buffer, "part1.csv", "Productor 1",textAreaConsola,labelP1));
        Thread productor2 = new Thread(new CDRProducer(buffer, "part2.csv", "Productor 2",textAreaConsola,labelP2));
        Thread productor3 = new Thread(new CDRProducer(buffer, "part3.csv", "Productor 3",textAreaConsola,labelP3));


        productor1.start();
        productor2.start();
        productor3.start();

        Thread consumidor1 = null;
        Thread consumidor2 = null;

        try {
            consumidor1 = new Thread(new CDRConsumer(buffer, "Consumidor 1",textAreaConsola));
            consumidor2 = new Thread(new CDRConsumer(buffer, "Consumidor 2",textAreaConsola));
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
            textAreaConsola.append("Productor 1 ha terminado.\n");

            productor2.join();
            textAreaConsola.append("Productor 2 ha terminado.\n");

            productor3.join();
            textAreaConsola.append("Productor 3 ha terminado.\n");

            if (consumidor1 != null) {
                consumidor1.join();
            }
            if (consumidor2 != null) {
                consumidor2.join();
            }


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
