package com.example.demovirus.structures;

/**
 * Pila dinámica (LIFO) implementada mediante nodos enlazados.
 * Permite registrar cada generación calculada y desapilar para revertir
 * la simulación al estado inmediatamente anterior.
 */
public class PilaGeneraciones {
    private NodoPila cima;
    private int tamano;

    public PilaGeneraciones() {
        this.cima = null;
        this.tamano = 0;
    }

    /**
     * Inserta un nuevo estado en el tope de la pila.
     */
    public void apilar(int[][] matriz, int numeroGeneracion, String fase) {
        NodoPila nuevoNodo = new NodoPila(matriz, numeroGeneracion, fase);
        nuevoNodo.setSiguiente(cima);
        this.cima = nuevoNodo;
        this.tamano++;
    }

    /**
     * Extrae y devuelve el nodo situado en el tope de la pila.
     * Retorna null si la pila se encuentra vacía.
     */
    public NodoPila desapilar() {
        if (estaVacia()) {
            return null;
        }
        NodoPila nodoDesapilado = cima;
        this.cima = cima.getSiguiente();
        this.tamano--;
        return nodoDesapilado;
    }

    /**
     * Consulta el nodo en la cima sin removerlo.
     */
    public NodoPila verCima() {
        return cima;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int getTamano() {
        return tamano;
    }
}
