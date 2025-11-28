package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.calificaciones.models.Docente;

public class EditarDocenteController {

    @FXML private TextField txtNombre;
    @FXML private Button btnCancelar;
    @FXML private Button btnGuardar;

    private Docente docenteAEditar;
    private boolean guardado = false;

    @FXML
    public void initialize() {
        btnCancelar.setOnAction(e -> cerrar());

        btnGuardar.setOnAction(e -> guardar());

        // Guardar al dar Enter
        txtNombre.setOnAction(e -> guardar());
    }

    public void setDocente(Docente docente) {
        this.docenteAEditar = docente;
        if (docente != null) {
            txtNombre.setText(docente.getNombre());
            txtNombre.selectAll(); // Seleccionar texto para editar rápido
            txtNombre.requestFocus();
        }
    }

    private void guardar() {
        String nuevoNombre = txtNombre.getText().trim();

        if (!nuevoNombre.isEmpty() && docenteAEditar != null) {
            // Actualizamos el modelo directamente
            docenteAEditar.setNombre(nuevoNombre);
            guardado = true;
            cerrar();
        }
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void cerrar() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}