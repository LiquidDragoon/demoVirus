module com.example.demovirus {
    requires javafx.controls;
    requires javafx.fxml;

    // Apertura de paquetes para reflexión e inyección de vistas FXML
    opens com.example.demovirus to javafx.fxml;
    opens com.example.demovirus.controllers to javafx.fxml;

    // Exportación de paquetes principales del módulo
    exports com.example.demovirus;
    exports com.example.demovirus.controllers;
}
