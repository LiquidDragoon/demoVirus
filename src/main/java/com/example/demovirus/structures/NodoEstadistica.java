package com.example.demovirus.structures;

/**
 * Nodo de la lista enlazada para almacenar las métricas de una generación:
 * número de generación, celdas sanas, infectadas y el conteo de cambios producidos.
 */
public class NodoEstadistica {
    private int generacion;
    private int celulasSanas;
    private int celulasInfectadas;
    private int nuevasInfectadas;
    private int nuevasSanas;
    private NodoEstadistica siguiente;

    public NodoEstadistica(int generacion, int sanas, int infectadas, int nuevasInf, int nuevasSan) {
        this.generacion = generacion;
        this.celulasSanas = sanas;
        this.celulasInfectadas = infectadas;
        this.nuevasInfectadas = nuevasInf;
        this.nuevasSanas = nuevasSan;
        this.siguiente = null;
    }

    public int getGeneracion() {
        return generacion;
    }

    public int getCelulasSanas() {
        return celulasSanas;
    }

    public int getCelulasInfectadas() {
        return celulasInfectadas;
    }

    public int getNuevasInfectadas() {
        return nuevasInfectadas;
    }

    public int getNuevasSanas() {
        return nuevasSanas;
    }

    public NodoEstadistica getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoEstadistica siguiente) {
        this.siguiente = siguiente;
    }
}
