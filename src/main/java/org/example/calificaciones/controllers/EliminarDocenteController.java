package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.calificaciones.models.DatosGlobales;
import org.example.calificaciones.models.Docente;

public class EliminarDocenteController {

    @FXML private TextField txtNombre;
    @FXML private Button btnCancelar;
    @FXML private Button btnEliminar;

    private Docente docenteAEliminar;
    private boolean eliminado = false;

    @FXML
    public void initialize() {
        btnCancelar.setOnAction(e -> cerrar());
        btnEliminar.setOnAction(e -> confirmarEliminacion());
    }

    public void setDocente(Docente docente) {
        this.docenteAEliminar = docente;
        if (docente != null) {
            txtNombre.setText(docente.getNombre());
        }
    }

    private void confirmarEliminacion() {
        if (docenteAEliminar != null) {

            DatosGlobales.getInstance().getListaDocentes().remove(docenteAEliminar);
            eliminado = true;
            cerrar();
        }
    }

    public boolean isEliminado() {
        return eliminado;
    }

    private void cerrar() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}