package org.proyecto.cdrprocessor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BufferCompartido {

    private final BlockingQueue<String> queue;
    private boolean producersActive = true;

    public BufferCompartido(int capacidad) {


        this.queue = new ArrayBlockingQueue<>(capacidad);

    }

    public void producir(String item) throws InterruptedException {
        queue.put(item);
        System.out.println("Elemento producido: " + item);
    }

    public String consumir() throws InterruptedException {
        String item = queue.take();
        System.out.println("Elemento consumido: " + item);
        return item;
    }


    public boolean size() {

       // System.out.println(" isEmpty: " +    queue.isEmpty());

        return queue.size() > 0;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized void setProducersActive(boolean active) {
        this.producersActive = active;
    }

    public synchronized boolean hasProducers() {
        return this.producersActive;
    }
}
