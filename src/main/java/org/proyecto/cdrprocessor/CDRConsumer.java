package org.proyecto.cdrprocessor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
     //   this.impresora = new PrintWriter(new FileWriter(rutaArchivo, true));
    }

    @Override
    public void run() {

        Connection conexion = null;
        PreparedStatement ps = null;

        Map<Long, CuentaInfo> cuentasMap = new HashMap<>();

        try {
          //  conexion = DriverManager.getConnection("jdbc:mysql://10.49.1.135:3306/app_db", "db_user", "db_user_pass");
            //System.out.println(buffer.size());
            while (true) {

                if (!buffer.size()) {
                    break;
                }

                String mensaje = buffer.consumir();
             //   System.out.println("mensaje"+mensaje);

                lock.lock();
                try {
                    String[] partes = mensaje.split(",", 7);
                    if (partes.length == 7) {

                        String numeroCuenta = partes[0];
                        String numeroDelQueLlama = partes[1];
                        String numeroAlQueLlama = partes[2];
                        String timestampLlamada = partes[3];
                        String duracionLlamada = partes[4];
                        String tarifaMinuto = partes[5];
                        String categoriaLlamada = partes[6];


                     System.out.println("Consumido por " + idConsumidor + " : "+ mensaje );
                    }
                } finally {
                    lock.unlock();
                }

            }
         //   System.out.println("fin de archivo consumidor");
        } catch (InterruptedException  e) {
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
