package com.example.demovirus.controllers;

import com.example.demovirus.models.MatrizTejido;
import com.example.demovirus.structures.ListaEnlazadaEstadisticas;
import com.example.demovirus.structures.PilaGeneraciones;

/**
 * Controlador principal de la lógica del autómata celular.
 * Coordina las transiciones de estado del tejido, la alternancia de fases
 * (Reproducción / Muerte), el almacenamiento histórico en la pila y la recolección
 * de métricas en la lista enlazada.
 */
public class SimulacionController {
    private MatrizTejido tejido;
    private PilaGeneraciones pilaGeneraciones;
    private ListaEnlazadaEstadisticas listaEstadisticas;
    private int numeroGeneracion;
    private boolean esFaseReproduccion; // true = Reproducción, false = Muerte
    private boolean simulacionIniciada;

    public SimulacionController() {
        this.tejido = new MatrizTejido();
        this.pilaGeneraciones = new PilaGeneraciones();
        this.listaEstadisticas = new ListaEnlazadaEstadisticas();
        this.numeroGeneracion = 0;
        this.esFaseReproduccion = true; // El ciclo comienza siempre en fase de reproducción
        this.simulacionIniciada = false;
    }

    /**
     * Da inicio formal a la simulación:
     * Guarda la generación 0 en la pila y crea el primer registro en la lista de estadísticas.
     */
    public void iniciarSimulacion() {
        if (tejido.contarInfectadas() > 0) {
            this.simulacionIniciada = true;
            this.pilaGeneraciones.apilar(tejido.getGrid(), numeroGeneracion, "Inicial");
            this.listaEstadisticas.agregar(
                    numeroGeneracion,
                    tejido.contarSanas(),
                    tejido.contarInfectadas(),
                    0,
                    0
            );
        }
    }

    /**
     * Calcula y aplica la siguiente generación según la fase activa del ciclo.
     * Almacena el nuevo estado en la pila y registra las métricas resultantes.
     *
     * @return Mensaje descriptivo con el resultado de la generación.
     */
    public String avanzarGeneracion() {
        if (!simulacionIniciada) {
            return "La simulación no ha sido iniciada.";
        }

        if (tejido.contarInfectadas() == 0) {
            return "El tejido está completamente sano. No hay más cambios que producir.";
        }

        int[] resultados;
        String nombreFase;

        if (esFaseReproduccion) {
            resultados = tejido.ejecutarReproduccion();
            nombreFase = "Reproducción";
        } else {
            resultados = tejido.ejecutarMuerte();
            nombreFase = "Muerte";
        }

        this.numeroGeneracion++;

        // Guardar el estado actual en la pila
        this.pilaGeneraciones.apilar(tejido.getGrid(), numeroGeneracion, nombreFase);

        // Añadir registro en la lista enlazada de estadísticas
        this.listaEstadisticas.agregar(
                numeroGeneracion,
                tejido.contarSanas(),
                tejido.contarInfectadas(),
                resultados[0],
                resultados[1]
        );

        // Alternar fase para la siguiente iteración
        this.esFaseReproduccion = !esFaseReproduccion;

        return "Generación " + numeroGeneracion + " ejecutada (" + nombreFase + ").";
    }

    /**
     * Revierte el tejido a la generación inmediatamente anterior desapilando el último estado.
     * Mantiene la pila y la lista enlazada de estadísticas en sincronía.
     *
     * @return true si fue posible retroceder, false si ya se encuentra en la generación base.
     */
    public boolean retrocederGeneracion() {
        if (pilaGeneraciones.getTamano() <= 1) {
            return false;
        }

        // Remover el estado más reciente de la pila
        pilaGeneraciones.desapilar();

        // Remover la estadística correspondiente para mantener coherencia
        listaEstadisticas.eliminarUltimo();

        // Restaurar el estado que quedó en la cima
        var nodoAnterior = pilaGeneraciones.verCima();
        this.tejido = new MatrizTejido(nodoAnterior.getMatriz());
        this.numeroGeneracion = nodoAnterior.getNumeroGeneracion();

        // Sincronizar la fase esperada según el registro restaurado
        this.esFaseReproduccion = nodoAnterior.getFase().equals("Muerte") || nodoAnterior.getFase().equals("Inicial");
        return true;
    }

    /**
     * Reinicia el estado de la simulación para permitir una nueva prueba desde cero.
     */
    public void reiniciar() {
        this.tejido = new MatrizTejido();
        this.pilaGeneraciones = new PilaGeneraciones();
        this.listaEstadisticas.limpiar();
        this.numeroGeneracion = 0;
        this.esFaseReproduccion = true;
        this.simulacionIniciada = false;
    }

    public MatrizTejido getTejido() {
        return tejido;
    }

    public int getNumeroGeneracion() {
        return numeroGeneracion;
    }

    public String getFaseActual() {
        return esFaseReproduccion ? "Reproducción" : "Muerte";
    }

    public boolean isSimulacionIniciada() {
        return simulacionIniciada;
    }

    public ListaEnlazadaEstadisticas getListaEstadisticas() {
        return listaEstadisticas;
    }

    public PilaGeneraciones getPilaGeneraciones() {
        return pilaGeneraciones;
    }

    /**
     * Cambia el estado de una celda entre sana e infectada antes de que inicie la simulación.
     */
    public void alternarCelda(int fila, int col) {
        if (!simulacionIniciada) {
            int estadoActual = tejido.getCelda(fila, col);
            tejido.setCelda(fila, col, estadoActual == MatrizTejido.CELDA_SANA ? MatrizTejido.CELDA_INFECTADA : MatrizTejido.CELDA_SANA);
        }
    }
}
