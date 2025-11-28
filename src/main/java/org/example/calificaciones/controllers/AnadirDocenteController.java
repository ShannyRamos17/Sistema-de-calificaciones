package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.calificaciones.models.DatosGlobales;
import org.example.calificaciones.models.Docente;

public class AnadirDocenteController {

    @FXML private TextField txtNombre;
    @FXML private Button btnCancelar;
    @FXML private Button btnGuardar;

    @FXML
    public void initialize() {
        btnCancelar.setOnAction(e -> cerrar());

        btnGuardar.setOnAction(e -> guardarDocente());
        txtNombre.setOnAction(e -> guardarDocente()); // Guardar con Enter
    }

    private void guardarDocente() {
        String nombre = txtNombre.getText().trim();

        if (!nombre.isEmpty()) {
            // Generar nuevo ID automático
            int nuevoId = DatosGlobales.getInstance().getListaDocentes().size() + 1;

            // ⭐ Crear y agregar el docente a la lista global
            // (Esto automáticamente habilita su login porque el LoginController lee esta lista)
            Docente nuevoDocente = new Docente(nuevoId, nombre);
            DatosGlobales.getInstance().getListaDocentes().add(nuevoDocente);

            cerrar();
        }
    }

    private void cerrar() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
