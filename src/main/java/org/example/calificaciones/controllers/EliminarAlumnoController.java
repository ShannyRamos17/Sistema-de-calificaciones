package org.example.calificaciones.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EliminarAlumnoController {

    @FXML private TableView<PrincipalController.AlumnoRow> tablaEliminar;
    @FXML private TableColumn<PrincipalController.AlumnoRow, Integer> colNumero;
    @FXML private TableColumn<PrincipalController.AlumnoRow, String> colNombre;
    @FXML private TableColumn<PrincipalController.AlumnoRow, Button> colEliminar;

    @FXML private Label lblPaginacion;
    @FXML private Button btnCancelar;

    private ObservableList<PrincipalController.AlumnoRow> alumnos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Conexiones correctas a las Properties del nuevo AlumnoRow
        colNumero.setCellValueFactory(cell -> cell.getValue().numeroProperty().asObject());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colEliminar.setCellValueFactory(cell -> cell.getValue().botonEliminarProperty());

        cargarDatosEjemplo();
        agregarEventosEliminar();   // ← Evento de cada botón 🗑

        btnCancelar.setOnAction(e -> volverPantallaPrincipal());
    }

    private void cargarDatosEjemplo() {
        alumnos.clear();

        alumnos.add(new PrincipalController.AlumnoRow(1, "Manuel Torres Rivera"));
        alumnos.add(new PrincipalController.AlumnoRow(2, "José Ernesto Ramírez Batalla"));
        alumnos.add(new PrincipalController.AlumnoRow(3, "Luis Pérez González"));
        alumnos.add(new PrincipalController.AlumnoRow(4, "Natalia García López"));
        alumnos.add(new PrincipalController.AlumnoRow(5, "Sandra Castro Gonzáles"));
        alumnos.add(new PrincipalController.AlumnoRow(6, "Giovanni Chávez Cárdenas"));
        alumnos.add(new PrincipalController.AlumnoRow(7, "André Álvarez Cortés"));

        tablaEliminar.setItems(alumnos);

        lblPaginacion.setText("1 - 7 / 20");
    }

    private void volverPantallaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/views/Principal.fxml"
            ));
            Parent root = loader.load();

            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    //     ⭐ Asignar acciones al botón 🗑 de cada fila
    // ----------------------------------------------------------
    private void agregarEventosEliminar() {
        for (PrincipalController.AlumnoRow alumno : alumnos) {
            alumno.getBotonEliminar().setOnAction(e -> confirmarEliminacion(alumno));
        }
    }

    // ----------------------------------------------------------
    //     ⭐ Confirmación antes de eliminar
    // ----------------------------------------------------------
    private void confirmarEliminacion(PrincipalController.AlumnoRow alumno) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres eliminar a:\n\n" +
                alumno.getNombre() + " ?");

        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnSi, btnCancelar);

        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnSi) {
                alumnos.remove(alumno);
                tablaEliminar.refresh();
            }
        });
    }
}
