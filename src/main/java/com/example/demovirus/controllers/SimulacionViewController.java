package com.example.demovirus.controllers;

import com.example.demovirus.models.MatrizTejido;
import com.example.demovirus.structures.NodoEstadistica;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

/**
 * Controlador de la interfaz gráfica de usuario en JavaFX.
 * Conecta los eventos de la vista con la lógica de negocio y refleja
 * visualmente el estado de las celdas en el GridPane.
 */
public class SimulacionViewController {

    @FXML private GridPane gridPaneMatriz;
    @FXML private Label lblGeneracion;
    @FXML private Label lblFase;
    @FXML private Label lblInfectadas;
    @FXML private Button btnIniciar;
    @FXML private Button btnSiguiente;
    @FXML private Button btnAnterior;
    @FXML private Button btnReiniciar;
    @FXML private TextArea txtAreaEstadisticas;

    private SimulacionController logica;
    private Button[][] botonesMatriz;

    @FXML
    public void initialize() {
        logica = new SimulacionController();
        botonesMatriz = new Button[MatrizTejido.TAMANIO][MatrizTejido.TAMANIO];
        inicializarMatrizVisual();
        actualizarVista();
    }

    /**
     * Construye dinámicamente la cuadrícula de botones interactivos dentro del GridPane.
     */
    private void inicializarMatrizVisual() {
        gridPaneMatriz.getChildren().clear();
        for (int i = 0; i < MatrizTejido.TAMANIO; i++) {
            for (int j = 0; j < MatrizTejido.TAMANIO; j++) {
                Button celdaBtn = new Button();
                celdaBtn.setMinSize(40, 40);
                celdaBtn.setPrefSize(40, 40);

                final int fila = i;
                final int col = j;

                celdaBtn.setOnAction(e -> {
                    logica.alternarCelda(fila, col);
                    sincronizarMatrizVisual();
                    actualizarVista();
                });

                botonesMatriz[i][j] = celdaBtn;
                gridPaneMatriz.add(celdaBtn, j, i);
            }
        }
        sincronizarMatrizVisual();
    }

    /**
     * Pinta cada celda según su estado biológico:
     * Rojo para células infectadas y gris claro para células sanas.
     */
    private void sincronizarMatrizVisual() {
        int[][] grid = logica.getTejido().getGrid();
        for (int i = 0; i < MatrizTejido.TAMANIO; i++) {
            for (int j = 0; j < MatrizTejido.TAMANIO; j++) {
                if (grid[i][j] == MatrizTejido.CELDA_INFECTADA) {
                    botonesMatriz[i][j].setStyle("-fx-background-color: #e53935; -fx-border-color: #b71c1c;");
                } else {
                    botonesMatriz[i][j].setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc;");
                }
            }
        }
    }

    @FXML
    private void handleIniciar() {
        if (logica.getTejido().contarInfectadas() == 0) {
            lblGeneracion.setText("Debe colocar al menos una célula infectada antes de iniciar la simulación.");
            return;
        }

        logica.iniciarSimulacion();
        btnIniciar.setDisable(true);
        btnSiguiente.setDisable(false);
        btnAnterior.setDisable(true); // Al iniciar en Gen 0 no hay generaciones anteriores
        actualizarVista();
    }

    @FXML
    private void handleSiguiente() {
        logica.avanzarGeneracion();
        sincronizarMatrizVisual();
        actualizarVista();
        btnAnterior.setDisable(false); // Ya se puede retroceder

        // Caso especial: si ya no quedan células infectadas en la matriz
        if (logica.getTejido().contarInfectadas() == 0) {
            btnSiguiente.setDisable(true);
            lblFase.setText("Simulación finalizada: Tejido sano");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Caso Especial: Tejido Sano");
            alert.setHeaderText("Simulación Finalizada");
            alert.setContentText("Ya no quedan células infectadas en la matriz.\nEl tejido se considera completamente sano y no tiene más cambios que producir.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAnterior() {
        boolean retrocedio = logica.retrocederGeneracion();
        if (retrocedio) {
            btnSiguiente.setDisable(false);
            sincronizarMatrizVisual();
            actualizarVista();

            // Si regresamos a la generación inicial (tamaño de pila <= 1), deshabilitar "Anterior"
            if (logica.getPilaGeneraciones().getTamano() <= 1) {
                btnAnterior.setDisable(true);
            }
        } else {
            btnAnterior.setDisable(true);
            lblGeneracion.setText("Generación inicial alcanzada (no se puede retroceder más).");
        }
    }

    @FXML
    private void handleReiniciar() {
        logica.reiniciar();
        sincronizarMatrizVisual();
        actualizarVista();
        btnIniciar.setDisable(false);
        btnSiguiente.setDisable(true);
        btnAnterior.setDisable(true);
    }

    /**
     * Refresca las etiquetas informativas y el área de texto con las estadísticas históricas.
     */
    private void actualizarVista() {
        lblGeneracion.setText("Generación: " + logica.getNumeroGeneracion());
        lblFase.setText("Fase: " + logica.getFaseActual());
        lblInfectadas.setText("Infectadas: " + logica.getTejido().contarInfectadas() + " | Sanas: " + logica.getTejido().contarSanas());

        // Recorrido de la lista enlazada para volcar el historial en el TextArea
        StringBuilder sb = new StringBuilder();
        NodoEstadistica actual = logica.getListaEstadisticas().getCabeza();
        while (actual != null) {
            sb.append(String.format("Gen %d (%s) -> Sanas: %d | Infectadas: %d | Nuevas Inf: %d | Nuevas San: %d%n",
                    actual.getGeneracion(),
                    actual.getGeneracion() == 0 ? "Inicial" : (actual.getGeneracion() % 2 != 0 ? "Reproducción" : "Muerte"),
                    actual.getCelulasSanas(),
                    actual.getCelulasInfectadas(),
                    actual.getNuevasInfectadas(),
                    actual.getNuevasSanas()));
            actual = actual.getSiguiente();
        }
        txtAreaEstadisticas.setText(sb.toString());
    }
}
