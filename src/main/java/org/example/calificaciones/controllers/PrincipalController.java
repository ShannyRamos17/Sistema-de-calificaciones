package org.example.calificaciones.controllers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.example.calificaciones.models.Alumno;
import org.example.calificaciones.models.DatosGlobales;

public class PrincipalController {

    @FXML private TableView<Alumno> tablaAlumnos;
    @FXML private TableColumn<Alumno, Integer> colNumero;
    @FXML private TableColumn<Alumno, String> colNombre;
    @FXML private TableColumn<Alumno, Void> colModificar;

    @FXML private Label lblPaginacion;
    @FXML private Button btnAnterior, btnSiguiente;
    @FXML private Label lblNombreMaestro, lblGrado;
    @FXML private Button btnNuevoAlumno;
    @FXML private Button btnMenu;

    private Popup popupMenu;

    // Lista inteligente para filtrar (Buscador local)
    private FilteredList<Alumno> listaFiltrada;

    @FXML
    public void initialize() {
        // Lógica de bienvenida
        String usuario = DatosGlobales.getInstance().getUsuarioActual();

        if (usuario != null && usuario.equalsIgnoreCase("Director")) {
            lblNombreMaestro.setText("Director");
            lblGrado.setText("Director");
        } else {
            lblNombreMaestro.setText(usuario != null ? usuario : "Docente");
            lblGrado.setText("(Docente)");
        }


        colNumero.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    // getIndex() devuelve 0, 1, 2... le sumamos 1 para mostrar 1, 2, 3...
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });

        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());

        // Configuración de los botones de acción en la tabla
        colModificar.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("✎");
            private final Button btnReportes = new Button("📋");
            private final HBox container = new HBox(10, btnEditar, btnReportes);

            {
                btnEditar.setStyle("-fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5;");
                btnEditar.setTooltip(new Tooltip("Modificar datos"));
                btnEditar.setOnAction(event -> {
                    Alumno alumno = getTableView().getItems().get(getIndex());
                    abrirDialogoAlumno(alumno, "Modificar Alumno");
                    tablaAlumnos.refresh();
                });

                btnReportes.setStyle("-fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5;");
                btnReportes.setTooltip(new Tooltip("Ver reporte"));
                btnReportes.setOnAction(event -> {
                    Alumno alumno = getTableView().getItems().get(getIndex());
                    abrirPantallaReportes(alumno);
                });
                container.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });

        // Cargar SOLAMENTE los alumnos del docente conectado
        ObservableList<Alumno> misAlumnos = DatosGlobales.getInstance().getAlumnosDelDocenteActual();

        // Envolvemos esa lista en OTRA FilteredList local para que funcione el buscador de texto
        listaFiltrada = new FilteredList<>(misAlumnos, p -> true);

        tablaAlumnos.setItems(listaFiltrada);
        actualizarContador();

        btnNuevoAlumno.setOnAction(e -> {
            String nombre = abrirDialogoAlumno(null, "Nuevo Alumno");

            if (nombre != null && !nombre.isEmpty()) {
                // 1. Obtener ID del docente actual
                int idDocente = DatosGlobales.getInstance().getIdDocenteLogueado();

                // 2. Generar nuevo ID único interno (para la base de datos)
                int nuevoId = DatosGlobales.getInstance().getListaAlumnos().size() + 1;

                // 3. Crear instancia PASANDO EL ID DEL DOCENTE
                Alumno nuevo = new Alumno(nuevoId, nombre, idDocente);

                // 4. Inicializar sus materias
                nuevo.inicializarMaterias(DatosGlobales.getInstance().getListaMaterias());

                // 5. Agregar a la lista GLOBAL (la tabla se actualiza sola por el filtro)
                DatosGlobales.getInstance().getListaAlumnos().add(nuevo);

                actualizarContador();
            }
        });

        btnMenu.setOnAction(e -> mostrarMenu());
    }

    private void actualizarContador() {
        int visibles = listaFiltrada.size();
        // Total de MIS alumnos, no de toda la base de datos
        int total = DatosGlobales.getInstance().getAlumnosDelDocenteActual().size();
        lblPaginacion.setText(visibles + " / " + total);
    }


    private void mostrarMenu() {
        if (popupMenu != null && popupMenu.isShowing()) {
            popupMenu.hide();
            return;
        }

        VBox contenedor = new VBox(5);
        contenedor.setStyle("-fx-background-color: #E6E6E6; -fx-padding: 10; -fx-border-color: #999;");
        contenedor.setAlignment(Pos.CENTER_LEFT);

        Button btnCerrarSesion = crearBotonMenu("Cerrar Sesión");
        btnCerrarSesion.setOnAction(e -> cerrarSesion());

        Button btnModificarMaterias = crearBotonMenu("Modificar Materias");
        btnModificarMaterias.setOnAction(e -> {
            if (popupMenu != null) popupMenu.hide();
            abrirModificarMaterias();
        });

        Button btnEliminarAlumno = crearBotonMenu("Eliminar Alumno");
        btnEliminarAlumno.setOnAction(e -> abrirEliminarAlumno());

        Button btnBuscarAlumno = crearBotonMenu("Buscar Alumno");
        btnBuscarAlumno.setOnAction(e -> {
            if (popupMenu != null) popupMenu.hide();
            abrirBuscador();
        });

        contenedor.getChildren().addAll(
                btnCerrarSesion,
                btnModificarMaterias,
                btnEliminarAlumno,
                btnBuscarAlumno
        );

        popupMenu = new Popup();
        popupMenu.getContent().add(contenedor);
        popupMenu.setAutoHide(true);
        Node boton = btnMenu;
        double x = boton.localToScreen(boton.getBoundsInLocal()).getMaxX();
        double y = boton.localToScreen(boton.getBoundsInLocal()).getMaxY();
        popupMenu.show(boton, x, y);
    }



    private String abrirDialogoAlumno(Alumno alumnoAEditar, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NuevoAlumno.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle(titulo);
            dialogStage.setScene(new Scene(root));
            NuevoAlumnoController controller = loader.getController();
            if (alumnoAEditar != null) controller.setAlumnoAEditar(alumnoAEditar);
            dialogStage.showAndWait();
            return controller.getNombreIngresado();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    private void abrirPantallaReportes(Alumno alumno) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Reportes.fxml"));
            Parent root = loader.load();
            ReportesController controller = loader.getController();
            controller.initData(alumno);
            Stage stage = (Stage) tablaAlumnos.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void abrirBuscador() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BuscarAlumno.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.showAndWait();

            BuscarAlumnoController controller = loader.getController();
            String busqueda = controller.getTextoBusqueda();

            if (busqueda != null && !busqueda.isEmpty()) {
                listaFiltrada.setPredicate(alumno -> {
                    String nombreAlumno = alumno.getNombre().toLowerCase();
                    String filtro = busqueda.toLowerCase();
                    return nombreAlumno.contains(filtro);
                });
            } else {
                listaFiltrada.setPredicate(alumno -> true);
            }
            actualizarContador();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Button crearBotonMenu(String texto) {
        Button b = new Button(texto);
        b.setMaxWidth(160); b.setPrefWidth(160);
        b.setStyle("-fx-background-color: #D9D9D9; -fx-padding: 10; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #C8C8C8; -fx-padding: 10; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #D9D9D9; -fx-padding: 10; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT;"));
        return b;
    }

    private void abrirModificarMaterias() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModificarMaterias.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Materias");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void abrirEliminarAlumno() {
        if (popupMenu != null) popupMenu.hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EliminarAlumno.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cerrarSesion() {
        if (popupMenu != null) popupMenu.hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}