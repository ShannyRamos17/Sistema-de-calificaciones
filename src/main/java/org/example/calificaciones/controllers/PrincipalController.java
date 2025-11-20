package org.example.calificaciones.controllers;

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
import javafx.stage.Stage;

public class PrincipalController {

    @FXML private TableView<AlumnoRow> tablaAlumnos;
    @FXML private TableColumn<AlumnoRow, Integer> colNumero;
    @FXML private TableColumn<AlumnoRow, String> colNombre;
    @FXML private TableColumn<AlumnoRow, Button> colModificar;

    @FXML private Label lblPaginacion;
    @FXML private Button btnAnterior, btnSiguiente;
    @FXML private Label lblNombreMaestro, lblGrado;

    @FXML private Button btnNuevoAlumno;
    @FXML private Button btnMenu; // 🔹 BOTÓN DE HAMBURGUESA

    private PopupControl popupMenu;
    @FXML private VBox contenedorMenu; // 🔹 Donde se colocará el menú lateral

    private ObservableList<AlumnoRow> alumnos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Vincular columnas
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colModificar.setCellValueFactory(new PropertyValueFactory<>("botonModificar"));

        cargarDatosEjemplo();

        // ⭐ BOTÓN: Añadir nuevo alumno
        btnNuevoAlumno.setOnAction(e -> {
            String nombre = mostrarNuevoAlumno();
            if (nombre != null && !nombre.isEmpty()) {
                int nuevoNum = alumnos.size() + 1;
                alumnos.add(new AlumnoRow(nuevoNum, nombre));
            }
        });

        // ⭐ BOTÓN: Menú hamburguesa
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
    //              🎓 Clase interna AlumnoRow
    // ----------------------------------------------------------
    public static class AlumnoRow {
        private int numero;
        private String nombre;
        private Button botonModificar;

        public AlumnoRow(int numero, String nombre) {
            this.numero = numero;
            this.nombre = nombre;

            botonModificar = new Button("✎");
            botonModificar.setOnAction(e ->
                    System.out.println("Modificar alumno: " + nombre)
            );
        }

        public int getNumero() { return numero; }
        public String getNombre() { return nombre; }
        public Button getBotonModificar() { return botonModificar; }
    }

    // ----------------------------------------------------------
    //              ⭐ Mostrar ventana "Nuevo Alumno"
    // ----------------------------------------------------------
    private String mostrarNuevoAlumno() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/calificaciones/views/NuevoAlumno.fxml"));
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
    //              ⭐ Mostrar menú hamburguesa
    // ----------------------------------------------------------
    @FXML
    private void mostrarMenu() {

        if (popupMenu != null && popupMenu.isShowing()) {
            popupMenu.hide();
            return;
        }

        VBox contenedor = new VBox(5);
        contenedor.setStyle("-fx-background-color: #E6E6E6; -fx-padding: 10; -fx-pref-width: 180;");
        contenedor.setAlignment(Pos.CENTER_LEFT);

        Button btnCerrarSesion = crearBoton("Cerrar Sesión");
        Button btnModificarMaterias = crearBoton("Modificar Materias");
        Button btnAnadirAlumno = crearBoton("Añadir Alumno");
        Button btnEliminarAlumno = crearBoton("Eliminar Alumno");
        Button btnBuscarAlumno = crearBoton("Buscar Alumno");

        contenedor.getChildren().addAll(
                btnCerrarSesion,
                btnModificarMaterias,
                btnAnadirAlumno,
                btnEliminarAlumno,
                btnBuscarAlumno
        );

        popupMenu = new PopupControl();
        popupMenu.getScene().setRoot(contenedor);
        popupMenu.setAutoHide(true);

        // Mostrarlo a la derecha del botón hamburguesa
        Node boton = btnMenu;
        popupMenu.show(boton, boton.localToScreen(boton.getBoundsInLocal()).getMaxX(),
                boton.localToScreen(boton.getBoundsInLocal()).getMaxY());
    }


    /** Método para crear botones con el mismo estilo */
    private Button crearBoton(String texto) {
        Button b = new Button(texto);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("""
            -fx-background-color: #D9D9D9;
            -fx-padding: 8 10;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
            """);

        // Efecto hover
        b.setOnMouseEntered(e -> b.setStyle("""
            -fx-background-color: #C8C8C8;
            -fx-padding: 8 10;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
            """));

        b.setOnMouseExited(e -> b.setStyle("""
            -fx-background-color: #D9D9D9;
            -fx-padding: 8 10;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
            """));

        return b;
    }

}
