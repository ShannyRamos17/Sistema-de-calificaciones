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

    // 👇 La columna es Void porque generamos el botón visualmente
    @FXML private TableColumn<Alumno, Void> colEliminar;

    @FXML private Label lblPaginacion;
    @FXML private Button btnCancelar;

    @FXML
    public void initialize() {

        // 1. Configurar columnas de datos
        colNumero.setCellValueFactory(cell -> cell.getValue().numeroProperty().asObject());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());

        // 2. Configurar la columna del botón ELIMINAR (CellFactory)
        colEliminar.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("🗑");

            {
                btn.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-cursor: hand;");
                btn.setOnAction(event -> {
                    // Obtener el alumno de la fila actual
                    Alumno alumno = getTableView().getItems().get(getIndex());
                    confirmarEliminacion(alumno);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        // 3. Conectar a los Datos Globales
        // Al usar la misma lista que Principal, los cambios se reflejan automáticamente
        tablaEliminar.setItems(DatosGlobales.getInstance().getListaAlumnos());

        actualizarContador();

        btnCancelar.setOnAction(e -> volverPantallaPrincipal());
    }

    private void actualizarContador() {
        int total = DatosGlobales.getInstance().getListaAlumnos().size();
        lblPaginacion.setText("Total registros: " + total);
    }

    // ----------------------------------------------------------
    //     ⭐ Confirmación antes de eliminar
    // ----------------------------------------------------------
    private void confirmarEliminacion(Alumno alumno) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres eliminar a:\n\n" +
                alumno.getNombre() + " ?\n\nEsta acción no se puede deshacer.");

        ButtonType btnSi = new ButtonType("Sí, eliminar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnNo = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnSi, btnNo);

        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnSi) {
                // 👇 BORRADO REAL: Eliminamos de la lista global
                DatosGlobales.getInstance().getListaAlumnos().remove(alumno);

                // Actualizamos el contador visual
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