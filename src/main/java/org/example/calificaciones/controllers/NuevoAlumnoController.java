package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
// Importar modelo Alumno
import org.example.calificaciones.models.Alumno;

public class NuevoAlumnoController {

    @FXML private TextField txtNombre;
    @FXML private Button btnCancelar, btnGuardar;

    private String nombreIngresado = null;
    private Alumno alumnoAEditar; // Campo para guardar el alumno si estamos editando

    @FXML
    public void initialize() {
        btnCancelar.setOnAction(e -> cerrar());

        btnGuardar.setOnAction(e -> {
            nombreIngresado = txtNombre.getText().trim();

            // Si estamos editando, actualizamos el nombre del alumno directamente
            if (alumnoAEditar != null && !nombreIngresado.isEmpty()) {
                alumnoAEditar.setNombre(nombreIngresado);
            }

            cerrar();
        });
    }

    public String getNombreIngresado() {
        return nombreIngresado;
    }

    // Método para recibir el alumno a editar desde PrincipalController
    public void setAlumnoAEditar(Alumno alumno) {
        this.alumnoAEditar = alumno;
        if (alumno != null) {
            txtNombre.setText(alumno.getNombre()); // Llenar el campo con el nombre actual
        }
    }

    private void cerrar() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}