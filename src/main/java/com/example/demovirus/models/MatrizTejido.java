package com.example.demovirus.models;

/**
 * Modelo del tejido celular representado mediante una matriz bidimensional.
 * Administra el estado biológico de cada celda (sana o infectada) y ejecuta
 * las reglas de transición correspondientes a las fases de reproducción y muerte.
 */
public class MatrizTejido {

    // Identificadores de estado para cada celda
    public static final int CELDA_SANA = 0;
    public static final int CELDA_INFECTADA = 1;

    // Parámetros de la simulación
    public static final int TAMANIO = 10;
    public static final int UMBRAL_MUERTE = 5;

    private int[][] grid;

    /**
     * Construye un tejido celular donde todas las celdas inician en estado sano.
     */
    public MatrizTejido() {
        this.grid = new int[TAMANIO][TAMANIO];
    }

    /**
     * Constructor de copia profunda. Crea una réplica exacta de la matriz dada
     * para aislar cada estado histórico almacenado en la pila.
     *
     * @param otraMatriz Matriz de enteros fuente para duplicar.
     */
    public MatrizTejido(int[][] otraMatriz) {
        this.grid = new int[TAMANIO][TAMANIO];
        for (int i = 0; i < TAMANIO; i++) {
            System.arraycopy(otraMatriz[i], 0, this.grid[i], 0, TAMANIO);
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setCelda(int fila, int columna, int valor) {
        if (fila >= 0 && fila < TAMANIO && columna >= 0 && columna < TAMANIO) {
            grid[fila][columna] = valor;
        }
    }

    public int getCelda(int fila, int columna) {
        if (fila >= 0 && fila < TAMANIO && columna >= 0 && columna < TAMANIO) {
            return grid[fila][columna];
        }
        return CELDA_SANA;
    }

    /**
     * Cuenta la cantidad de células infectadas alrededor de la celda (r, c).
     * Se evalúa una vecindad de Moore (hasta 8 celdas adyacentes) respetando
     * los límites de la matriz (sin frontera toroidal).
     *
     * @param r Índice de fila.
     * @param c Índice de columna.
     * @return Cantidad de vecinos infectados.
     */
    public int contarVecinosInfectados(int r, int c) {
        int contador = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue; // Omitir la celda central
                }

                int vecinoFila = r + i;
                int vecinoColumna = c + j;

                if (vecinoFila >= 0 && vecinoFila < TAMANIO && vecinoColumna >= 0 && vecinoColumna < TAMANIO) {
                    if (grid[vecinoFila][vecinoColumna] == CELDA_INFECTADA) {
                        contador++;
                    }
                }
            }
        }
        return contador;
    }

    /**
     * Ejecuta la Fase de Reproducción (contagio):
     * Toda célula infectada transmite el virus a todas las células sanas vecinas.
     * Se utiliza una matriz auxiliar para sincronizar la actualización del tejido.
     *
     * @return Arreglo de enteros: [nuevasInfectadas, nuevasSanas].
     */
    public int[] ejecutarReproduccion() {
        int[][] nuevaGrid = new int[TAMANIO][TAMANIO];
        for (int i = 0; i < TAMANIO; i++) {
            System.arraycopy(grid[i], 0, nuevaGrid[i], 0, TAMANIO);
        }

        int nuevasInf = 0;

        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (grid[i][j] == CELDA_INFECTADA) {
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            int ni = i + di;
                            int nj = j + dj;
                            if (ni >= 0 && ni < TAMANIO && nj >= 0 && nj < TAMANIO) {
                                if (grid[ni][nj] == CELDA_SANA && nuevaGrid[ni][nj] == CELDA_SANA) {
                                    nuevaGrid[ni][nj] = CELDA_INFECTADA;
                                    nuevasInf++;
                                }
                            }
                        }
                    }
                }
            }
        }

        this.grid = nuevaGrid;
        return new int[]{nuevasInf, 0};
    }

    /**
     * Ejecuta la Fase de Muerte (recuperación):
     * Toda célula infectada que cuente con 5 o más vecinos infectados muere por
     * sobreinfección y pasa nuevamente a ser una célula sana.
     *
     * @return Arreglo de enteros: [nuevasInfectadas, nuevasSanas].
     */
    public int[] ejecutarMuerte() {
        int[][] nuevaGrid = new int[TAMANIO][TAMANIO];
        for (int i = 0; i < TAMANIO; i++) {
            System.arraycopy(grid[i], 0, nuevaGrid[i], 0, TAMANIO);
        }

        int nuevasSan = 0;

        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (grid[i][j] == CELDA_INFECTADA) {
                    int vecinosInfectados = contarVecinosInfectados(i, j);
                    if (vecinosInfectados >= UMBRAL_MUERTE) {
                        nuevaGrid[i][j] = CELDA_SANA;
                        nuevasSan++;
                    }
                }
            }
        }

        this.grid = nuevaGrid;
        return new int[]{0, nuevasSan};
    }

    /**
     * Cuenta el total de celdas sanas en el tejido.
     */
    public int contarSanas() {
        int total = 0;
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (grid[i][j] == CELDA_SANA) {
                    total++;
                }
            }
        }
        return total;
    }

    /**
     * Cuenta el total de celdas infectadas en el tejido.
     */
    public int contarInfectadas() {
        int total = 0;
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (grid[i][j] == CELDA_INFECTADA) {
                    total++;
                }
            }
        }
        return total;
    }
}
