package org.proyecto.util;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import java.sql.*;

public class Util
{ private static final String DB_URL = "jdbc:mysql://10.49.1.135:3306/upana";
    private static final String DB_USER = "db_user";
    private static final String DB_PASS = "db_user_pass";

    public Util()
    {

    }

    public static boolean  esFormatoCSVCorrecto(String rutaArchivo, int totalLineasArchivo) {
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
          File file1 = new File("src/main/resources/part1.csv");
            File file2 = new File("src/main/resources/part2.csv");
           File file3 = new File("src/main/resources/part3.csv");
            if (file1.exists()) {
                file1.delete();
            }
            if (file2.exists()) {
                file2.delete();
            }
            if (file3.exists()) {
                file3.delete();
            }

         BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
         List<String> lines = new ArrayList<>();
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
               FileWriter writer = new FileWriter("src/main/resources/part" + (i + 1) + ".csv");
                for (int j = start; j < end; j++) {
                    writer.write(lines.get(j) + "\n");
                }
                writer.close();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }


    public static List<Object[]> obtenerLlamadas(String numeroCuenta) {
        List<Object[]> llamadas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = DriverManager.getConnection(DB_URL , DB_USER , DB_PASS);
            String sql = "SELECT numero_cuenta, numero_del_que_llama, numero_al_que_llama, timestamp_llamada, duracion_llamada, tarifa_minuto, costo_llamada, categoria_llamada, productor, consumidor FROM llamada WHERE numero_cuenta = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, numeroCuenta);
            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] llamada = new Object[10];
                llamada[0] = rs.getString("numero_cuenta");
                llamada[1] = rs.getString("numero_del_que_llama");
                llamada[2] = rs.getString("numero_al_que_llama");
                llamada[3] = rs.getString("timestamp_llamada");
                llamada[4] = rs.getInt("duracion_llamada");
                llamada[5] = rs.getDouble("tarifa_minuto");
                llamada[6] = rs.getDouble("costo_llamada");
                llamada[7] = rs.getString("categoria_llamada");
                llamada[8] = rs.getString("productor");
                llamada[9] = rs.getString("consumidor");
                llamadas.add(llamada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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

        return llamadas;
    }
    public static List<Object[]> obtenerCuentas2() {
       List<Object[]> cuentas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = DriverManager.getConnection(DB_URL , DB_USER , DB_PASS);
            String sql = "SELECT * FROM cuenta";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] cuenta = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 0; i < cuenta.length; i++) {
                    cuenta[i] = rs.getObject(i + 1);
                }
                cuentas.add(cuenta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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

        return cuentas;
    }

    public static Object[] obtenerDatosCuenta(String numeroCuenta) {
        Object[] cuenta = null;
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = DriverManager.getConnection(DB_URL , DB_USER , DB_PASS);
            String sql = "SELECT * FROM cuenta WHERE numero_cuenta = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, numeroCuenta);
            rs = ps.executeQuery();

            if (rs.next()) {
                cuenta = new Object[4];
                cuenta[0] = rs.getString("numero_cuenta");
                cuenta[1] = rs.getInt("total_llamadas");
                cuenta[2] = rs.getInt("total_duracion");
                cuenta[3] = rs.getDouble("costo_total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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

        return cuenta;
    }

    public static List<String> obtenerCuentas() {
        List<String> cuentas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = DriverManager.getConnection(DB_URL , DB_USER , DB_PASS);
            String sql = "SELECT numero_cuenta FROM cuenta";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                return cuentas; // Retornar lista vacía si no hay resultados
            }

            while (rs.next()) {
                cuentas.add(rs.getString("numero_cuenta"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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

        return cuentas;
    }


    public static void reiniciarDB() {
        Connection conexion = null;
        Statement stmt = null;

        try {
            conexion = DriverManager.getConnection(DB_URL , DB_USER , DB_PASS);
            stmt = conexion.createStatement();
            stmt.executeUpdate("DELETE FROM llamada");
            stmt.executeUpdate("DELETE FROM cuenta");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
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
