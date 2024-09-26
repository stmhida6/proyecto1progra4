package org.proyecto.cdrprocessor;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Map;

public class CDRConsumer implements Runnable {

    private final BufferCompartido buffer;

    private final String idConsumidor;

    // private PrintWriter impresora;

    private static final Lock lock = new ReentrantLock();

    public CDRConsumer(BufferCompartido buffer, String idConsumidor) throws IOException {
        this.buffer = buffer;
        this.idConsumidor = idConsumidor;
    }

    @Override
    public void run() {

        Connection conexion = null;
        PreparedStatement ps = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //Map<Long, CuentaInfo> cuentasMap = new HashMap<>();

        try {
            conexion = DriverManager.getConnection("jdbc:mysql://10.49.1.135:3306/upana", "db_user", "db_user_pass");
           // System.out.println(buffer.isEmpty());
              while (true) {
         //   while (!buffer.isEmpty() || buffer.hasProducers()) {
                // Loop body
                if (!buffer.hasProducers() && buffer.isEmpty()) {
                    System.out.println("No quedan más elementos por consumir y la producción ha finalizado.");
                    break; // Salir del ciclo si no hay más elementos y la producción ha terminado
                }

                String mensaje = buffer.consumir();
                //   System.out.println("mensaje"+mensaje);

                lock.lock();
                try {
                    String[] partes = mensaje.split(",", 8);
                    if (partes.length == 8) {

                        String numeroCuenta = partes[0];
                        String numeroDelQueLlama = partes[1];
                        String numeroAlQueLlama = partes[2];
                        String timestampLlamada = partes[3];
                        int  duracionLlamada = Integer.parseInt(partes[4]);
                        double tarifaMinuto = Double.parseDouble(partes[5]);
                        double costoLlamada = Math.round(duracionLlamada * tarifaMinuto * 1000.0) / 1000.0;
                        String categoriaLlamada = partes[6];
                        String idProductor = partes[7];

                        String sql = "INSERT INTO llamada (numero_cuenta, numero_del_que_llama, numero_al_que_llama, timestamp_llamada,duracion_llamada,tarifa_minuto,costo_llamada,categoria_llamada,productor,consumidor, fecha) VALUES (?, ?, ?, ?, ?, ? , ?, ?, ?, ?,now())";
                        ps = conexion.prepareStatement(sql);
                        ps.setString(1, numeroCuenta);
                        ps.setString(2, numeroDelQueLlama);
                        ps.setString(3, numeroAlQueLlama);
                        ps.setString(4, timestampLlamada);
                        ps.setInt(5, duracionLlamada);
                        ps.setDouble(6, tarifaMinuto);
                        ps.setDouble(7, costoLlamada);
                        ps.setString(8, categoriaLlamada);
                        ps.setString(9, idProductor);
                        ps.setString(10, idConsumidor);

                        ps.executeUpdate();
                        String salida = numeroCuenta + " " + numeroDelQueLlama + " " + numeroAlQueLlama + " " + timestampLlamada + " " + duracionLlamada + " " + tarifaMinuto + " " + categoriaLlamada+ " " + idProductor+ " " + idConsumidor;


                        System.out.println("Consumido por " + idConsumidor + " : " + salida);
                    }
                } finally {
                    lock.unlock();
                }

            }
          //  System.out.println("fin del consumidor");
         //   Thread.currentThread().interrupt();
        } catch (InterruptedException | SQLException  e) {
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
