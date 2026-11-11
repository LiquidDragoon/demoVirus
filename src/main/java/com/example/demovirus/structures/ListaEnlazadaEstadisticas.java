package com.example.demovirus.structures;

/**
 * Lista simplemente enlazada para almacenar el registro cronológico de estadísticas.
 * Cada nodo representa el balance poblacional registrado al término de una generación.
 */
public class ListaEnlazadaEstadisticas {
    private NodoEstadistica cabeza;
    private NodoEstadistica cola;
    private int totalElementos;

    public ListaEnlazadaEstadisticas() {
        this.cabeza = null;
        this.cola = null;
        this.totalElementos = 0;
    }

    /**
     * Inserta un nuevo registro al final de la lista en O(1) usando el puntero cola.
     */
    public void agregar(int generacion, int sanas, int infectadas, int nuevasInf, int nuevasSan) {
        NodoEstadistica nuevo = new NodoEstadistica(generacion, sanas, infectadas, nuevasInf, nuevasSan);
        if (cabeza == null) {
            this.cabeza = nuevo;
            this.cola = nuevo;
        } else {
            this.cola.setSiguiente(nuevo);
            this.cola = nuevo;
        }
        this.totalElementos++;
    }

    /**
     * Elimina el último nodo de la lista para mantener la coherencia
     * cuando el usuario retrocede una generación mediante la pila.
     */
    public void eliminarUltimo() {
        if (cabeza == null) {
            return;
        }
        if (cabeza == cola) {
            this.cabeza = null;
            this.cola = null;
            this.totalElementos = 0;
            return;
        }

        NodoEstadistica actual = cabeza;
        while (actual.getSiguiente() != cola) {
            actual = actual.getSiguiente();
        }
        actual.setSiguiente(null);
        this.cola = actual;
        this.totalElementos--;
    }

    public NodoEstadistica getCabeza() {
        return cabeza;
    }

    public int getTotalElementos() {
        return totalElementos;
    }

    /**
     * Restablece la lista a un estado vacío.
     */
    public void limpiar() {
        this.cabeza = null;
        this.cola = null;
        this.totalElementos = 0;
    }
}
