package com.erner.proyectoparalelacontingencia;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ForkJoin {

    private ForkJoinPool pool = new ForkJoinPool();

    public void generarCuentaClabe(Cliente[] clientes) {
        pool.invoke(new GenerarCuentaClabeTask(clientes, 0, clientes.length));
    }

    private class GenerarCuentaClabeTask extends RecursiveTask<Void> {
        private static final int THRESHOLD = 5;
        private Cliente[] clientes;
        private int start;
        private int end;

        GenerarCuentaClabeTask(Cliente[] clientes, int start, int end) {
            this.clientes = clientes;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    Cliente cliente = clientes[i];
                    // Extraer los datos necesarios
                    String codigo = cliente.getCodigo();
                    String nombre = cliente.getNombre();
                    int cdgEstadoNacimiento = cliente.getCdgEstadoNacimiento();
                    int edad = cliente.getEdad();

                    // Crear la cuenta CLABE
                    String cuentaClabe = "8527";
                    String codigoLastTwoDigits = codigo.substring(codigo.length() - 2);
                    cuentaClabe += codigoLastTwoDigits;
                    cuentaClabe += "145";
                    String cdgEstadoNacimientoStr = String.format("%02d", cdgEstadoNacimiento);
                    cuentaClabe += cdgEstadoNacimientoStr;
                    cuentaClabe += "6";
                    String edadStr = String.format("%02d", edad);
                    cuentaClabe += edadStr;
                    cuentaClabe += codigo.substring(0, 1);

                    // Verificar si la cuenta CLABE tiene 17 caracteres
                    if (cuentaClabe.length() == 17) {
                        // Asignar la cuenta CLABE al cliente
                        cliente.setCuentaClabe(cuentaClabe);
                    } 
                }
            } else {
                int middle = (start + end) / 2;
                GenerarCuentaClabeTask leftTask = new GenerarCuentaClabeTask(clientes, start, middle);
                GenerarCuentaClabeTask rightTask = new GenerarCuentaClabeTask(clientes, middle, end);
                leftTask.fork();
                rightTask.fork();
                leftTask.join();
                rightTask.join();
            }
            return null;
        }
    }
}
