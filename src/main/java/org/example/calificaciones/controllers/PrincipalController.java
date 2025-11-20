package org.example.calificaciones.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private ObservableList<AlumnoRow> alumnos = FXCollections.observableArrayList();
    @FXML private Button btnNuevoAlumno;

    @FXML
    public void initialize() {

        // Vincular columnas
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colModificar.setCellValueFactory(new PropertyValueFactory<>("botonModificar"));

        cargarDatosEjemplo();

        btnNuevoAlumno.setOnAction(e -> {
            String nombre = mostrarNuevoAlumno();
            if (nombre != null && !nombre.isEmpty()) {
                int nuevoNum = alumnos.size() + 1;
                alumnos.add(new AlumnoRow(nuevoNum, nombre));
            }
        });
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

    // Clase interna para filas
    public static class AlumnoRow {
        private int numero;
        private String nombre;
        private Button botonModificar;

        public AlumnoRow(int numero, String nombre) {
            this.numero = numero;
            this.nombre = nombre;

            botonModificar = new Button("✎");
            botonModificar.setOnAction(e -> System.out.println("Modificar alumno: " + nombre));
        }

        public int getNumero() { return numero; }
        public String getNombre() { return nombre; }
        public Button getBotonModificar() { return botonModificar; }
    }

    private String mostrarNuevoAlumno() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NuevoAlumno.fxml"));
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

}
