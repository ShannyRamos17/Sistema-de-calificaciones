package org.example.calificaciones.controllers;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.net.URL;

public class PrincipalController {

    @FXML private TableView<AlumnoRow> tablaAlumnos;
    @FXML private TableColumn<AlumnoRow, Integer> colNumero;
    @FXML private TableColumn<AlumnoRow, String> colNombre;
    @FXML private TableColumn<AlumnoRow, Button> colModificar;

    @FXML private Label lblPaginacion;
    @FXML private Button btnAnterior, btnSiguiente;
    @FXML private Label lblNombreMaestro, lblGrado;

    @FXML private Button btnNuevoAlumno;
    @FXML private Button btnMenu;

    private Popup popupMenu;

    private ObservableList<AlumnoRow> alumnos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Columnas
        colNumero.setCellValueFactory(cell -> cell.getValue().numeroProperty().asObject());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colModificar.setCellValueFactory(cell -> cell.getValue().botonModificarProperty());

        cargarDatosEjemplo();

        // Botón: nuevo alumno
        btnNuevoAlumno.setOnAction(e -> {
            String nombre = mostrarNuevoAlumno();
            if (nombre != null && !nombre.isEmpty()) {
                alumnos.add(new AlumnoRow(alumnos.size() + 1, nombre));
            }
        });

        // Botón menú hamburguesa
        btnMenu.setOnAction(e -> mostrarMenu());
    }

    private void cargarDatosEjemplo() {
        alumnos.clear();

        alumnos.add(new AlumnoRow(1, "Manuel Torres Rivera"));
        alumnos.add(new AlumnoRow(2, "José Ernesto Ramírez Batalla"));
        alumnos.add(new AlumnoRow(3, "Luis Pérez González"));
        alumnos.add(new AlumnoRow(4, "Natalia García López"));
        alumnos.add(new AlumnoRow(5, "Sandra Castro Gonzáles"));
        alumnos.add(new AlumnoRow(6, "Giovanni Chávez Cárdenas"));
        alumnos.add(new AlumnoRow(7, "André Álvarez Cortés"));

        tablaAlumnos.setItems(alumnos);
        lblPaginacion.setText("1 - 7 / 20");
    }

    // ----------------------------------------------------------
    //              🎓 Clase interna AlumnoRow (NUEVA)
    // ----------------------------------------------------------
    public static class AlumnoRow {

        private final SimpleIntegerProperty numero;
        private final SimpleStringProperty nombre;

        private final ObjectProperty<Button> botonModificar;
        private final ObjectProperty<Button> botonEliminar;

        public AlumnoRow(int numero, String nombre) {
            this.numero = new SimpleIntegerProperty(numero);
            this.nombre = new SimpleStringProperty(nombre);

            // Botón modificar
            Button btnMod = new Button("✎");
            btnMod.setStyle("-fx-font-size: 14px;");
            btnMod.setOnAction(e ->
                    System.out.println("Modificar alumno: " + nombre)
            );
            this.botonModificar = new SimpleObjectProperty<>(btnMod);

            // Botón eliminar (para otras pantallas)
            Button btnDel = new Button("🗑");
            btnDel.setStyle("-fx-font-size: 14px;");
            this.botonEliminar = new SimpleObjectProperty<>(btnDel);
        }

        // GETTERS REQUERIDOS POR TABLEVIEW
        public int getNumero() { return numero.get(); }
        public String getNombre() { return nombre.get(); }
        public Button getBotonModificar() { return botonModificar.get(); }
        public Button getBotonEliminar() { return botonEliminar.get(); }

        // PROPIEDADES
        public SimpleIntegerProperty numeroProperty() { return numero; }
        public SimpleStringProperty nombreProperty() { return nombre; }
        public ObjectProperty<Button> botonModificarProperty() { return botonModificar; }
        public ObjectProperty<Button> botonEliminarProperty() { return botonEliminar; }
    }

    // ----------------------------------------------------------
    //              ⭐ Ventana "Nuevo Alumno"
    // ----------------------------------------------------------
    private String mostrarNuevoAlumno() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/org/example/calificaciones/views/NuevoAlumno.fxml"
            ));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Nuevo Alumno");

            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            NuevoAlumnoController controller = loader.getController();
            return controller.getNombreIngresado();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ----------------------------------------------------------
    //              ⭐ Popup menú hamburguesa
    // ----------------------------------------------------------
    @FXML
    private void mostrarMenu() {

        if (popupMenu != null && popupMenu.isShowing()) {
            popupMenu.hide();
            return;
        }

        VBox contenedor = new VBox(5);
        contenedor.setStyle("-fx-background-color: #E6E6E6; -fx-padding: 10;");
        contenedor.setAlignment(Pos.CENTER_LEFT);

        Button btnCerrarSesion = crearBoton("Cerrar Sesión");
        Button btnModificarMaterias = crearBoton("Modificar Materias");
        Button btnAnadirAlumno = crearBoton("Añadir Alumno");
        Button btnEliminarAlumno = crearBoton("Eliminar Alumno");
        btnEliminarAlumno.setOnAction(e -> abrirEliminarAlumno());
        Button btnBuscarAlumno = crearBoton("Buscar Alumno");

        contenedor.getChildren().addAll(
                btnCerrarSesion,
                btnModificarMaterias,
                btnAnadirAlumno,
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

    private Button crearBoton(String texto) {
        Button b = new Button(texto);
        b.setMaxWidth(160);
        b.setPrefWidth(160);

        b.setStyle("""
            -fx-background-color: #D9D9D9;
            -fx-padding: 10 10;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
        """);

        b.setOnMouseEntered(e -> b.setStyle("""
            -fx-background-color: #C8C8C8;
            -fx-padding: 10 10;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
        """));

        b.setOnMouseExited(e -> b.setStyle("""
            -fx-background-color: #D9D9D9;
            -fx-padding: 10 10;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
        """));

        return b;
    }

    // ----------------------------------------------------------
    //              ⭐ Abrir pantalla Eliminar Alumno
    // ----------------------------------------------------------
    private void abrirEliminarAlumno() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EliminarAlumno.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
