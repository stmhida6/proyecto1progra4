package org.proyecto.gui;

import org.proyecto.util.Util;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.proyecto.cdrprocessor.CDR;



public class MiAplicacion extends JFrame {
    private int totalLineasArchivo = 0;
    private long startTime;
    public MiAplicacion() {
        // Configuración de la ventana principal
        setTitle("Proyecto Progra 4 - CDR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        startTime = System.currentTimeMillis();
        // Crear JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel 1
        JPanel panel1 = new JPanel(new BorderLayout());

        // Panel superior con JTextField y JButton
        JPanel panelSuperior1 = new JPanel(new BorderLayout());
        JTextField textFieldPath = new JTextField();
        JButton buttonAbrir = new JButton("Abrir");
        panelSuperior1.add(textFieldPath, BorderLayout.CENTER);
        panelSuperior1.add(buttonAbrir, BorderLayout.EAST);

        // JTextArea como consola
        JTextArea textAreaConsola = new JTextArea(10, 30);
        JScrollPane scrollPaneConsola = new JScrollPane(textAreaConsola);

        // Panel para los tres botones adicionales
        JPanel panelBotonesAccion = new JPanel();
        JButton botonAccion2 = new JButton("Reiniciar DB");
        JButton buttonLimpiar = new JButton("Limpiar Consola");
        JButton buttonIniciar = new JButton("Iniciar Procesamiento");
        //cambiar color buttonIniciar
        buttonIniciar.setBackground(Color.GREEN);
        botonAccion2.setBackground(Color.RED);

        // s botones al panel
        JLabel labelP1 = new JLabel("Productor 1");
        JLabel labelP2 = new JLabel("Productor 2");
        JLabel labelP3 = new JLabel("Productor 1");
        JLabel tiempoTotal = new JLabel("Tiempo Total: 00:00:00");
        panelBotonesAccion.add(labelP1);
        panelBotonesAccion.add(labelP2);
        panelBotonesAccion.add(labelP3);

        panelBotonesAccion.add(botonAccion2);
        panelBotonesAccion.add(buttonLimpiar);
        panelBotonesAccion.add(buttonIniciar);

        panelBotonesAccion.add(tiempoTotal);




        textFieldPath.setEditable(false); // No permitir editar el campo de texto
        // 📂
        buttonAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                // Escribir lo que está en el JTextField en el JTextArea
//                String texto = textFieldPath.getText();
//                textAreaConsola.append("Entrada: " + texto + "\n");
//                textFieldPath.setText(""); // Limpiar el campo de texto
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home") + "/Downloads"));
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
                fileChooser.showOpenDialog(null);
                if (fileChooser.getSelectedFile() == null) {
                    return;
                }
                String file = fileChooser.getSelectedFile().getAbsolutePath();

                textFieldPath.setText(file);
                buttonIniciar.setEnabled(true);
                textAreaConsola.setText("Archivo Seleccionado: " + file);
                textAreaConsola.append("\n");
                textAreaConsola.append("Tamaño del archivo: " + fileChooser.getSelectedFile().length() + " bytes");

                int count = 0;
                try {
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                    while (reader.readLine() != null) count++;
                    reader.close();
                } catch (java.io.IOException e1) {
                    e1.printStackTrace();
                }

                totalLineasArchivo = count;
                textAreaConsola.append(count + " lineas");

                textAreaConsola.append("\n");
            }
        });

        // 🚮
        botonAccion2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              Util.reiniciarDB();
            }
        });

        // 🧹
        buttonLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaConsola.setText("");
                textFieldPath.setText("");
                buttonIniciar.setEnabled(false);
                buttonAbrir.setEnabled(true);
                totalLineasArchivo = 0;
                tiempoTotal.setText("Tiempo Total: 00:00:00");
                labelP3.setText("Productor 3");
                labelP2.setText("Productor 2");
                labelP1.setText("Productor 1");



            }
        });

        // ⭐
        buttonIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Util.esFormatoCSVCorrecto(textFieldPath.getText(), totalLineasArchivo)) {
                    textAreaConsola.append("El archivo NO tiene el formato correcto!!! \n");
                    return;
                }
                textAreaConsola.append("El archivo tiene el formato correcto ;) \n");
                textAreaConsola.append("Dividiendo archivo... \n");
                buttonAbrir.setEnabled(false);
                buttonIniciar.setEnabled(false);
                buttonLimpiar.setEnabled(false);
                botonAccion2.setEnabled(false);
                new Thread(() -> {
                    if (!Util.dividirArchivo(textFieldPath.getText())) {
                        SwingUtilities.invokeLater(() -> textAreaConsola.append("Error al dividir el archivo \n"));
                        return;
                    }
                    SwingUtilities.invokeLater(() -> {
                        textAreaConsola.append("Archivo dividido \n");
                        textAreaConsola.append("Iniciando procesamiento...\n");
//                        CDR cdr = new CDR(textAreaConsola);
//                        cdr.procesarCDR();
                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() throws Exception {


                                Timer timer = new Timer(1000, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        long elapsed = System.currentTimeMillis() - startTime;
                                        long seconds = (elapsed / 1000) % 60;
                                        long minutes = (elapsed / (1000 * 60)) % 60;
                                        long hours = (elapsed / (1000 * 60 * 60)) % 24;
                                        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                                        SwingUtilities.invokeLater(() -> tiempoTotal.setText("Tiempo Total: " + timeFormatted));
                                    }
                                });
                                timer.start();
                                CDR cdr = new CDR(textAreaConsola, labelP1, labelP2, labelP3);
                                cdr.procesarCDR();
                                timer.stop();
                                buttonLimpiar.setEnabled(true);
                                buttonLimpiar.setEnabled(true);
                                buttonIniciar.setEnabled(true);
                                botonAccion2.setEnabled(true);
                                return null;
                            }

                            @Override
                            protected void done() {
                             //   buttonLimpiar.setEnabled(true);
                             //   buttonAbrir.setEnabled(true);
                             //   buttonIniciar.setEnabled(true);
                            }
                        };
                        worker.execute();

                       // buttonLimpiar.setEnabled(true);
                       // buttonAbrir.setEnabled(true);
                      //  buttonIniciar.setEnabled(true);
                    });
                }).start();


            }
        });

        //  elementos al panel 1
        panel1.add(panelSuperior1, BorderLayout.NORTH);
        panel1.add(scrollPaneConsola, BorderLayout.CENTER);
        panel1.add(panelBotonesAccion, BorderLayout.SOUTH); // Agregar los botones adicionales debajo del JTextArea


        JPanel panel2 = new JPanel(new BorderLayout());

        // Panel superior
        JPanel panelSuperior2 = new JPanel();
        JComboBox<String> comboBox = new JComboBox<>();
        JButton botonFiltrar = new JButton("Filtrar Llamadas");
        panelSuperior2.add(comboBox);
        panelSuperior2.add(botonFiltrar);

        // Tabla
        String[] columnas = {"Número Cuenta", "Número del que llama", "Número al que llama", "Timestamp", "Duración", "Tarifa Minuto", "Costo Llamada", "Categoría", "Productor", "Consumidor"};
        Object[][] datos = {        };
        DefaultTableModel model = new DefaultTableModel(datos, columnas);
        JTable tabla = new JTable(model);
        JScrollPane scrollPaneTabla = new JScrollPane(tabla);

        // Panel inferior con JTextFields
        JPanel panelInferior = new JPanel(new GridLayout(1, 3));
        JTextField textField1 = new JTextField();
        JTextField textField2 = new JTextField();
        JTextField textField3 = new JTextField();
        panelInferior.add(new JLabel("Total de Llamadas:"));
        panelInferior.add(textField1);
        panelInferior.add(new JLabel("Tottal duración:"));
        panelInferior.add(textField2);
        panelInferior.add(new JLabel("Costo Total:"));
        panelInferior.add(textField3);

        //👀 filtrrar
        botonFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filtro = (String) comboBox.getSelectedItem();
                DefaultTableModel modeloFiltrado = new DefaultTableModel(columnas, 0);
                java.util.List<Object[]> llamadas = Util.obtenerLlamadas(filtro);

                // Filtrar los datos según la opción seleccionada
                for (Object[] fila : llamadas) {
                    // if (fila[2].equals(filtro)) {
                    modeloFiltrado.addRow(fila);
                    // }
                }
                tabla.setModel(modeloFiltrado);


                Object[] datosCuenta = Util.obtenerDatosCuenta(filtro);
                System.out.println("Se obtienen los datos de la cuenta :" + filtro);
                if (datosCuenta != null) {
                    textField1.setText(String.valueOf(datosCuenta[1]));
                    textField2.setText(String.valueOf(datosCuenta[2]));
                    textField3.setText(String.valueOf(datosCuenta[3]));
                } else {
                    textField1.setText("");
                    textField2.setText("");
                    textField3.setText("");
                }

            }
        });

        // Agregar componentes al panel 2
        panel2.add(panelSuperior2, BorderLayout.NORTH);
        panel2.add(scrollPaneTabla, BorderLayout.CENTER);
        panel2.add(panelInferior, BorderLayout.SOUTH);

        // Agregar los paneles al JTabbedPane
        tabbedPane.addTab("Ejecución", panel1);
        tabbedPane.addTab("Resultados", panel2);

        // Agregar un ChangeListener para detectar cuando el usuario cambia a la pestaña 2
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Verificar si la pestaña seleccionada es la pestaña 2
                if (tabbedPane.getSelectedIndex() == 1) {

                    comboBox.setModel(new DefaultComboBoxModel(Util.obtenerCuentas().toArray()));
                    // comboBox.setModel(new DefaultComboBoxModel(Util.obtenerCuentas2().stream().map(cuenta -> (String) cuenta[0]).toArray(String[]::new)));
                    System.out.println("Se  obtienen todas las cuentas");
                    comboBox.setSelectedIndex(0); // Selecciona la primera opción por defecto
                    //limpia la tabla
                    tabla.setModel(new DefaultTableModel(columnas, 0));

                }
            }
        });

        // Agregar el JTabbedPane al JFrame
        add(tabbedPane);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Ejecutar la aplicación
        SwingUtilities.invokeLater(() -> new MiAplicacion());
    }
}
