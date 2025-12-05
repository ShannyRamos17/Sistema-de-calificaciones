package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox; // Importante para agrupar botones
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.calificaciones.models.DatosGlobales;
import org.example.calificaciones.models.Docente;

public class PrincipalDirectorController {

    @FXML private Label lblNombreDirector;
    @FXML private Label lblPaginacion;
    @FXML private Button btnMenu;

    @FXML private TableView<Docente> tablaDocentes;
    @FXML private TableColumn<Docente, Integer> colNumero;
    @FXML private TableColumn<Docente, String> colNombre;
    @FXML private TableColumn<Docente, Void> colVer;

    private Popup popupMenu; // Variable para el menú

    @FXML
    public void initialize() {
        // 1. Configurar Nombre del Director
        String usuario = DatosGlobales.getInstance().getUsuarioActual();
        lblNombreDirector.setText(usuario != null ? usuario : "Director");

        // 2. Enlazar columnas
        colNumero.setCellValueFactory(cell -> cell.getValue().numeroProperty().asObject());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());

        // 3. Configurar Columna de ACCIONES (Ingresar + Ver)
        colVer.setCellFactory(param -> new TableCell<>() {

            // Botón A: Ingresar como docente (Flecha)
            private final Button btnIngresar = new Button("➜");
            // Botón B: Ver detalles (Ojo)
            private final Button btnVer = new Button("👁");
            // Contenedor para ponerlos juntos
            private final HBox panelBotones = new HBox(10, btnIngresar, btnVer);

            {
                // Estilo Botón Ingresar (Azul)
                btnIngresar.setStyle("-fx-font-size: 14px; -fx-background-color: #4A90E2; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5; -fx-font-weight: bold;");
                btnIngresar.setTooltip(new Tooltip("Ingresar al panel de este docente"));

                btnIngresar.setOnAction(event -> {
                    Docente docente = getTableView().getItems().get(getIndex());
                    entrarComoDocente(docente); // <--- Llama a la nueva función
                });

                // Estilo Botón Ver (Gris)
                btnVer.setStyle("-fx-font-size: 16px; -fx-background-color: #D9D9D9; -fx-cursor: hand; -fx-background-radius: 5; -fx-padding: 2 10;");
                btnVer.setTooltip(new Tooltip("Modificar nombre del docente"));

                btnVer.setOnAction(event -> {
                    Docente docente = getTableView().getItems().get(getIndex());
                    abrirEditorDocente(docente);
                });

                panelBotones.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(panelBotones);
                }
            }
        });

        // 4. Cargar Datos
        tablaDocentes.setItems(DatosGlobales.getInstance().getListaDocentes());

        // 5. Actualizar etiqueta
        actualizarPaginacion();

        // 6. Acción del Menú (Abrir menú hamburguesa)
        btnMenu.setOnAction(e -> mostrarMenu());
    }

    private void actualizarPaginacion() {
        int total = DatosGlobales.getInstance().getListaDocentes().size();
        lblPaginacion.setText("1 - " + total + " / " + total);
    }

    // ----------------------------------------------------------
    //              NUEVA LÓGICA: IMPERSONAR DOCENTE
    // ----------------------------------------------------------
    private void entrarComoDocente(Docente docente) {
        try {
            // 1. Cambiamos el usuario global al nombre del docente seleccionado
            // Esto es lo que permite ver SU información en la siguiente pantalla
            DatosGlobales.getInstance().setUsuarioActual(docente.getNombre());

            // 2. Cargamos la vista del Docente (Principal.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Principal.fxml"));
            Parent root = loader.load();

            // 3. Cambiamos la escena actual
            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Calificaciones - " + docente.getNombre());
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la vista del docente.");
            alert.show();
        }
    }

    // ----------------------------------------------------------
    //              MENÚ HAMBURGUESA DEL DIRECTOR
    // ----------------------------------------------------------
    private void mostrarMenu() {
        if (popupMenu != null && popupMenu.isShowing()) {
            popupMenu.hide();
            return;
        }

        VBox contenedor = new VBox(5);
        contenedor.setStyle("-fx-background-color: #E6E6E6; -fx-padding: 10; -fx-border-color: #999;");
        contenedor.setAlignment(Pos.CENTER_LEFT);

        // Opción 1: Cerrar Sesión
        Button btnCerrarSesion = crearBotonMenu("Cerrar Sesión");
        btnCerrarSesion.setOnAction(e -> cerrarSesion());

        // Opción 2: Añadir Docentes
        Button btnAnadir = crearBotonMenu("Añadir Docentes");
        btnAnadir.setOnAction(e -> {
            if (popupMenu != null) popupMenu.hide();
            abrirAnadirDocente();
        });

        // Opción 3: Eliminar Docentes
        Button btnEliminar = crearBotonMenu("Eliminar Docentes");
        btnEliminar.setOnAction(e -> {
            if (popupMenu != null) popupMenu.hide();
            abrirEliminarDocentes();
        });

        contenedor.getChildren().addAll(btnCerrarSesion, btnAnadir, btnEliminar);

        popupMenu = new Popup();
        popupMenu.getContent().add(contenedor);
        popupMenu.setAutoHide(true);

        Node boton = btnMenu;
        double x = boton.localToScreen(boton.getBoundsInLocal()).getMaxX();
        double y = boton.localToScreen(boton.getBoundsInLocal()).getMaxY();
        popupMenu.show(boton, x, y);
    }

    private Button crearBotonMenu(String texto) {
        Button b = new Button(texto);
        b.setMaxWidth(180);
        b.setPrefWidth(180);
        b.setStyle("-fx-background-color: #D9D9D9; -fx-padding: 10; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #C8C8C8; -fx-padding: 10; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #D9D9D9; -fx-padding: 10; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT;"));
        return b;
    }

    // ----------------------------------------------------------
    //              ACCIONES Y NAVEGACIÓN
    // ----------------------------------------------------------

    private void abrirAnadirDocente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AnadirDocente.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);

            stage.showAndWait();
            actualizarPaginacion();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirEliminarDocentes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EliminarDocentesListado.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Eliminar Docentes");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirEditorDocente(Docente docente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarDocente.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);

            EditarDocenteController controller = loader.getController();
            controller.setDocente(docente);

            stage.showAndWait();

            if (controller.isGuardado()) {
                tablaDocentes.refresh();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrarSesion() {
        if (popupMenu != null) popupMenu.hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}