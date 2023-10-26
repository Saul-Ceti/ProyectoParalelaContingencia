package com.erner.proyectoparalelacontingencia;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProyectoParalelaContingencia extends JFrame {

    //Variables necesarias para el programa
    Cliente[] clientes;
    Secuencial secuencial = new Secuencial();
    ForkJoin forkJoin = new ForkJoin();
    MetodoEjecutor metodoEjecutor = new MetodoEjecutor();

    public ProyectoParalelaContingencia() {
        //Parámetros para el JFrame o la ventana pricipal
        setTitle("Generaciónde cuentas CLABE para transferencias SPEI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        ////////////////////////////////////////////////////////////////////////

        //Campos de visualización de datos
        //Datos originales
        JTextArea originalData = new JTextArea();
        JScrollPane original = new JScrollPane(originalData);
        original.setBounds(50, 50, 850, 200);

        //Conteo de datos
        JLabel cantidadClientes = new JLabel("Esperando...");
        cantidadClientes.setBounds(450, 265, 100, 20);

        //Datos procesados
        JTextArea processData = new JTextArea();
        JScrollPane process = new JScrollPane(processData);
        process.setBounds(50, 300, 850, 200);
        ////////////////////////////////////////////////////////////////////////

        //Botones para el archivo
        //Subir archivo crudo
        JButton btnSubir = new JButton("Subir archivo");
        btnSubir.setBounds(950, 50, 200, 50);

        //Descargar archivo procesado
        JButton btnDescargar = new JButton("Descargar archivo");
        btnDescargar.setBounds(950, 110, 200, 50);
        ////////////////////////////////////////////////////////////////////////

        //Botones para procesar los datos y sus etiquetas
        //Botón Secuencial
        JButton btnSecuencial = new JButton("Secuencial");
        btnSecuencial.setBounds(950, 300, 200, 50);
        JLabel timeSecuencial = new JLabel("Esperando");
        timeSecuencial.setBounds(950, 355, 200, 10);

        //Botón ForkJoin
        JButton btnForkJoin = new JButton("ForkJoin");
        btnForkJoin.setBounds(950, 370, 200, 50);
        JLabel timeForkJoin = new JLabel("Esperando");
        timeForkJoin.setBounds(950, 425, 250, 10);

        //Botón ExecutorService
        JButton btnExecutorService = new JButton("ExecutorService");
        btnExecutorService.setBounds(950, 440, 200, 50);
        JLabel timeExecutorService = new JLabel("Esperando");
        timeExecutorService.setBounds(950, 495, 250, 10);
        ////////////////////////////////////////////////////////////////////////

        //Botón para limpiar
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(950, 170, 200, 50);
        ////////////////////////////////////////////////////////////////////////

        //Agrega los componentes al frame
        getContentPane().add(original);
        getContentPane().add(cantidadClientes);
        getContentPane().add(process);
        getContentPane().add(btnSecuencial);
        getContentPane().add(btnForkJoin);
        getContentPane().add(btnExecutorService);
        getContentPane().add(btnSubir);
        getContentPane().add(btnLimpiar);
        getContentPane().add(timeSecuencial);
        getContentPane().add(timeForkJoin);
        getContentPane().add(timeExecutorService);
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        ////////////////////////ACCIONES DE BOTONES/////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        //Limpiar
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                originalData.setText("");
                processData.setText("");
                cantidadClientes.setText("0");
                timeSecuencial.setText("0.0ms");
                timeForkJoin.setText("0.0ms");
                timeExecutorService.setText("0.0ms");

                clientes = null;
            }
        });
        ////////////////////////////////////////////////////////////////////////
        // Subir archivo
        btnSubir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processData.setText("");
                timeSecuencial.setText("0.0ms");
                timeForkJoin.setText("0.0ms");
                timeExecutorService.setText("0.0ms");

                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    // El usuario seleccionó un archivo
                    java.io.File file = fileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();

                    // Crea una instancia de LeerArchivo con la ruta del archivo
                    LeerArchivo lector = new LeerArchivo(filePath);

                    // Llama al método leerDeportistas para obtener un arreglo de Deportista
                    clientes = lector.leerClientes();
                    cantidadClientes.setText(Integer.toString(clientes.length));

                    imprimirClientes(clientes, originalData);
                } else {
                    // El usuario canceló la selección del archivo
                    JOptionPane.showMessageDialog(null, "Selección de archivo cancelada.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
        ////////////////////////////////////////////////////////////////////////
        // Proceso secuencial
        btnSecuencial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clientes == null) {
                    JOptionPane.showMessageDialog(null, "Primero sube información para ser procesada.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    processData.setText("");

                    long tiempo = medirTiempoEjecucion(() -> {
                        secuencial.generarCuentaClabe(clientes);
                    });

                    imprimirClientes(clientes, processData);

                    String tiempoFormateado = formatoTiempo(tiempo);
                    timeSecuencial.setText("Tiempo: " + tiempoFormateado + " ms:ns");
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////
        // ForkJoin
        btnForkJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clientes == null) {
                    JOptionPane.showMessageDialog(null, "Primero sube información para ser procesada.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    processData.setText("");

                    long tiempo = medirTiempoEjecucion(() -> {
                        forkJoin.generarCuentaClabe(clientes);
                    });

                    imprimirClientes(clientes, processData);

                    String tiempoFormateado = formatoTiempo(tiempo);
                    timeForkJoin.setText("Tiempo: " + tiempoFormateado + " ms:ns");
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////
        // ExecutorService
        btnExecutorService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clientes == null) {
                    JOptionPane.showMessageDialog(null, "Primero sube información para ser procesada.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    processData.setText("");

                    long tiempo = medirTiempoEjecucion(() -> {
                        metodoEjecutor.generarCuentaClabeParalelo(clientes, timeExecutorService);
                    });

                    imprimirClientes(clientes, processData);

                    formatoTiempo(tiempo);
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////
        setVisible(true);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        ProyectoParalelaContingencia proyectoApp = new ProyectoParalelaContingencia();
    }

    public void imprimirClientes(Cliente[] clientes, JTextArea area) {
        // Iterar a través del arreglo de deportistas
        for (Cliente cliente : clientes) {
            if (cliente.getCuentaClabe() != null) {
                area.append("Nombre: " + cliente.getNombre() + ", ");
                area.append("Cuenta CLABE: " + cliente.getCuentaClabe() + "\n");
            } else {
                // Obtén la información del deportista y agrega al JTextArea
                area.append("Código de usuario: " + cliente.getCodigo() + "\n");
                area.append("Nombre: " + cliente.getNombre() + "\n");
                area.append("Código de estado de nacimiento: " + cliente.getCdgEstadoNacimiento() + "\n");
                area.append("Edad: " + cliente.getEdad() + "\n");
                area.append("\n"); // Agregar una línea en blanco entre clientes                
            }
        }
    }

    public long medirTiempoEjecucion(Runnable runnable) {
        long startTime = System.nanoTime();
        runnable.run();
        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    public String formatoTiempo(long tiempoNanosegundos) {
        long milliseconds = tiempoNanosegundos / 1_000_000;
        long nanoseconds = tiempoNanosegundos % 1_000_000;

        return String.format("%d:%06d", milliseconds, nanoseconds);
    }
}
