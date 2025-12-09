package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.calificaciones.models.Alumno;
import org.example.calificaciones.models.DatosGlobales;

public class EliminarAlumnoController {

    @FXML private TableView<Alumno> tablaEliminar;
    @FXML private TableColumn<Alumno, Integer> colNumero;
    @FXML private TableColumn<Alumno, String> colNombre;
    @FXML private TableColumn<Alumno, Void> colEliminar;
    @FXML private Label lblPaginacion;
    @FXML private Button btnCancelar;

    @FXML
    public void initialize() {
        colNumero.setCellValueFactory(cell -> cell.getValue().numeroProperty().asObject());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());

        colEliminar.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("🗑");
            {
                btn.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-cursor: hand;");
                btn.setOnAction(event -> {
                    Alumno alumno = getTableView().getItems().get(getIndex());
                    confirmarEliminacion(alumno);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });


        tablaEliminar.setItems(DatosGlobales.getInstance().getAlumnosDelDocenteActual());

        actualizarContador();
        btnCancelar.setOnAction(e -> volverPantallaPrincipal());
    }

    private void actualizarContador() {
        // Contamos solo los items visibles en la tabla (los filtrados)
        int total = tablaEliminar.getItems().size();
        lblPaginacion.setText("Mis alumnos: " + total);
    }

    private void confirmarEliminacion(Alumno alumno) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres eliminar a:\n\n" + alumno.getNombre() + "?");

        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                // Borramos de la lista GLOBAL real, el filtro se actualiza solo
                DatosGlobales.getInstance().getListaAlumnos().remove(alumno);
                actualizarContador();
            }
        });
    }

    private void volverPantallaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Principal.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}