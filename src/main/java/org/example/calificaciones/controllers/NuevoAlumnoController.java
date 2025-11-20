package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NuevoAlumnoController {

    @FXML private TextField txtNombre;
    @FXML private Button btnCancelar, btnGuardar;

    private String nombreIngresado = null;

    @FXML
    public void initialize() {
        btnCancelar.setOnAction(e -> cerrar());
        btnGuardar.setOnAction(e -> {
            nombreIngresado = txtNombre.getText().trim();
            cerrar();
        });
    }

    public String getNombreIngresado() {
        return nombreIngresado;
    }

    private void cerrar() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
