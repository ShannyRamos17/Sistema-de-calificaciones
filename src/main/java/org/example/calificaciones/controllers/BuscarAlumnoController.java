package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BuscarAlumnoController {

    @FXML private TextField txtBusqueda;
    @FXML private Button btnCancelar;
    @FXML private Button btnBuscar;

    private String textoBusqueda = null;

    @FXML
    public void initialize() {
        btnCancelar.setOnAction(e -> cerrar());

        btnBuscar.setOnAction(e -> {
            textoBusqueda = txtBusqueda.getText().trim();
            cerrar();
        });

        // Opción extra: Buscar al presionar ENTER
        txtBusqueda.setOnAction(e -> btnBuscar.fire());
    }

    public String getTextoBusqueda() {
        return textoBusqueda;
    }

    private void cerrar() {
        Stage stage = (Stage) txtBusqueda.getScene().getWindow();
        stage.close();
    }
}
