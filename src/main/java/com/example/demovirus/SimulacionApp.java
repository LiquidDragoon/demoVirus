package com.example.demovirus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicación JavaFX.
 * Carga la vista principal FXML y configura la ventana de la simulación.
 */
public class SimulacionApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimulacionApp.class.getResource("/com/example/demovirus/views/simulacion-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 740);

        stage.setTitle("Simulación de Propagación de Virus - Tejido Celular (10x10)");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
