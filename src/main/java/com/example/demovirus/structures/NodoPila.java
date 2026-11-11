package com.example.demovirus.structures;

/**
 * Nodo para la estructura lineal PilaGeneraciones.
 * Almacena una instantánea profunda de la matriz del tejido, el número de generación
 * y el nombre de la fase ejecutada para permitir el retroceso paso a paso.
 */
public class NodoPila {
    private int[][] matriz;
    private int numeroGeneracion;
    private String fase;
    private NodoPila siguiente;

    public NodoPila(int[][] matriz, int numeroGeneracion, String fase) {
        // Se realiza una copia profunda para asegurar que mutaciones futuras
        // en el tejido no alteren el estado guardado en este nodo.
        this.matriz = new int[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++) {
            System.arraycopy(matriz[i], 0, this.matriz[i], 0, matriz[i].length);
        }
        this.numeroGeneracion = numeroGeneracion;
        this.fase = fase;
        this.siguiente = null;
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public int getNumeroGeneracion() {
        return numeroGeneracion;
    }

    public String getFase() {
        return fase;
    }

    public NodoPila getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoPila siguiente) {
        this.siguiente = siguiente;
    }
}
