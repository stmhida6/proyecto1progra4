package org.proyecto.cdrprocessor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Consumidor implements Runnable {

    private final BufferCompartido buffer;

    private final String idConsumidor;

   // private PrintWriter impresora;

    private static final Lock lock = new ReentrantLock();

    public Consumidor(BufferCompartido buffer,  String idConsumidor) throws IOException {
        this.buffer = buffer;
        this.idConsumidor = idConsumidor;
     //   this.impresora = new PrintWriter(new FileWriter(rutaArchivo, true));
    }

    @Override
    public void run() {

        Connection conexion = null;
        PreparedStatement ps = null;


        try {
            conexion = DriverManager.getConnection("jdbc:mysql://10.49.1.135:3306/app_db", "db_user", "db_user_pass");

            while (true) {
                String mensaje = buffer.consumir();
                if (mensaje.startsWith("END")) {
                    break;
                }
                lock.lock();
                try {
                    String[] partes = mensaje.split(",", 3);
                    if (partes.length == 3) {
                        String nombre = partes[0];
                        String apellido = partes[1];
                        String idProductor = partes[2];
                        String sql = "INSERT INTO persona (nombre, apellido, productor,consumidor, fecha) VALUES (?, ?, ?, ?,now())";
                        ps = conexion.prepareStatement(sql);
                        ps.setString(1, nombre);
                        ps.setString(2, apellido);
                        ps.setString(3, idProductor);
                        ps.setString(4, idConsumidor);
                        ps.executeUpdate();
                        String salida = nombre + " " + apellido + "," + idProductor + "," + idConsumidor;


                        System.out.println("Consumido por " + idConsumidor + " : " + salida);
                    }
                } finally {
                    lock.unlock();
                }

            }
        } catch (InterruptedException  | SQLException e) {
            Thread.currentThread().interrupt();
        } finally {
           // impresora.close();
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }
    }
}
