package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
        import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.calificaciones.models.DatosGlobales;
import org.example.calificaciones.models.Docente;

public class EliminarDocentesListadoController {

    @FXML private Label lblNombreDirector;
    @FXML private Label lblPaginacion;
    @FXML private TableView<Docente> tablaDocentes;
    @FXML private TableColumn<Docente, Integer> colNumero;
    @FXML private TableColumn<Docente, String> colNombre;
    @FXML private TableColumn<Docente, Void> colEliminar;
    @FXML private Button btnVolver;
    @FXML private Button btnCancelar;

    @FXML
    public void initialize() {
        String usuario = DatosGlobales.getInstance().getUsuarioActual();
        lblNombreDirector.setText(usuario != null ? usuario : "Director");

        colNumero.setCellValueFactory(cell -> cell.getValue().numeroProperty().asObject());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());

        // Configurar Botón "Basura"
        colEliminar.setCellFactory(param -> new TableCell<>() {
            private final Button btnBasura = new Button("🗑");

            {
                btnBasura.setStyle("-fx-font-size: 16px; -fx-background-color: #E6E6E6; -fx-text-fill: #555; -fx-cursor: hand; -fx-background-radius: 5;");
                btnBasura.setTooltip(new Tooltip("Eliminar docente permanentemente"));

                btnBasura.setOnAction(event -> {
                    Docente docente = getTableView().getItems().get(getIndex());
                    abrirConfirmacionEliminar(docente);
                });

                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnBasura);
                }
            }
        });

        tablaDocentes.setItems(DatosGlobales.getInstance().getListaDocentes());
        actualizarContador();

        btnVolver.setOnAction(e -> volverAlPrincipal());
        btnCancelar.setOnAction(e -> volverAlPrincipal());
    }

    private void actualizarContador() {
        lblPaginacion.setText("Total: " + DatosGlobales.getInstance().getListaDocentes().size());
    }

    private void abrirConfirmacionEliminar(Docente docente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EliminarDocente.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);

            EliminarDocenteController controller = loader.getController();
            controller.setDocente(docente);

            stage.showAndWait();

            if (controller.isEliminado()) {
                tablaDocentes.refresh();
                actualizarContador();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void volverAlPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PrincipalDirector.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}