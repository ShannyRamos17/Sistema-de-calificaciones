package org.example.calificaciones.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.calificaciones.models.Alumno;
import org.example.calificaciones.models.CalificacionRow;
import org.example.calificaciones.models.DatosGlobales;

public class ReportesController {

    @FXML private Label lblNombreAlumno;
    @FXML private TableView<CalificacionRow> tablaCalificaciones;
    @FXML private TableColumn<CalificacionRow, String> colMateria;
    @FXML private TableColumn<CalificacionRow, String> colP1;
    @FXML private TableColumn<CalificacionRow, String> colP2;
    @FXML private TableColumn<CalificacionRow, String> colP3;
    @FXML private TableColumn<CalificacionRow, String> colTotal;

    @FXML private Button btnCancelar;
    @FXML private Button btnGuardar;

    @FXML private Label lblNombreMaestro;
    @FXML private Label lblGrado;

    private Alumno alumnoActual;
    // Lista temporal para editar en la pantalla sin afectar al alumno hasta guardar
    private ObservableList<CalificacionRow> listaTemporal = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        String usuario = DatosGlobales.getInstance().getUsuarioActual();
        if (usuario != null && usuario.equalsIgnoreCase("Director")) {
            lblNombreMaestro.setText("Director");
            lblGrado.setText("Director");
        } else {
            lblNombreMaestro.setText(usuario != null ? usuario : "Docente");
            lblGrado.setText("(Docente)");
        }

        colMateria.setCellValueFactory(cell -> cell.getValue().materiaProperty());
        colTotal.setCellValueFactory(cell -> cell.getValue().totalProperty());

        // Configurar columnas EDITABLES
        configurarColumnaEditable(colP1, 1);
        configurarColumnaEditable(colP2, 2);
        configurarColumnaEditable(colP3, 3);

        tablaCalificaciones.setItems(listaTemporal);

        btnCancelar.setOnAction(e -> cerrarVentana());
        btnGuardar.setOnAction(e -> guardarCambios());
    }

    public void initData(Alumno alumno) {
        this.alumnoActual = alumno;
        lblNombreAlumno.setText(alumno.getNombre());
        cargarCalificaciones();
    }

    private void cargarCalificaciones() {
        listaTemporal.clear();

        if (alumnoActual.getCalificaciones().isEmpty()) {
            // Si es la primera vez, cargamos las materias por defecto desde DatosGlobales
            // (Usamos DatosGlobales para asegurar consistencia si se modificaron las materias)
            if (DatosGlobales.getInstance().getListaMaterias().isEmpty()) {
                // Fallback por seguridad si no hay materias globales definidas
                listaTemporal.add(new CalificacionRow("Arte y Cultura"));
                listaTemporal.add(new CalificacionRow("Biología y Ciencias de la Vida"));
                listaTemporal.add(new CalificacionRow("Matemáticas: Problemas de la Vida Diaria"));
                listaTemporal.add(new CalificacionRow("Formación Cívica y Ética"));
                listaTemporal.add(new CalificacionRow("Español: Principios del Lenguaje"));
                listaTemporal.add(new CalificacionRow("Ingles IV"));
            } else {
                for (var materia : DatosGlobales.getInstance().getListaMaterias()) {
                    listaTemporal.add(new CalificacionRow(materia.getNombre()));
                }
            }
        } else {
            // Si ya tiene calificaciones guardadas, hacemos una COPIA para editar
            for (CalificacionRow rowOriginal : alumnoActual.getCalificaciones()) {
                CalificacionRow copia = new CalificacionRow(rowOriginal.materiaProperty().get());
                copia.p1Property().set(rowOriginal.p1Property().get());
                copia.p2Property().set(rowOriginal.p2Property().get());
                copia.p3Property().set(rowOriginal.p3Property().get());
                copia.calcularPromedio(); // Recalcular total en la copia
                listaTemporal.add(copia);
            }
        }
    }

    private void configurarColumnaEditable(TableColumn<CalificacionRow, String> columna, int periodo) {
        columna.setCellFactory(param -> new TableCell<>() {
            private final TextField txtNota = new TextField();

            {
                txtNota.setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");

                // Listener: cada vez que escribes, actualiza el modelo
                txtNota.textProperty().addListener((obs, oldVal, newVal) -> {
                    // Validar que sea numero o vacio (opcional)
                    if (!newVal.matches("\\d*(\\.\\d*)?")) {
                        txtNota.setText(oldVal); // Rechazar letras
                        return;
                    }

                    if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                        CalificacionRow row = getTableView().getItems().get(getIndex());
                        if (periodo == 1) row.p1Property().set(newVal);
                        if (periodo == 2) row.p2Property().set(newVal);
                        if (periodo == 3) row.p3Property().set(newVal);

                        row.calcularPromedio(); // ¡Esto actualiza el total automáticamente!
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CalificacionRow row = getTableView().getItems().get(getIndex());
                    // Sincronizar el texto del campo con el valor de la fila
                    if (periodo == 1) txtNota.setText(row.p1Property().get());
                    if (periodo == 2) txtNota.setText(row.p2Property().get());
                    if (periodo == 3) txtNota.setText(row.p3Property().get());
                    setGraphic(txtNota);
                }
            }
        });
    }

    private void guardarCambios() {
        // Copiar las filas de la lista temporal a la lista oficial del alumno
        ObservableList<CalificacionRow> nuevasDefinitivas = FXCollections.observableArrayList();

        for (CalificacionRow rowTemp : listaTemporal) {
            // Creamos objetos nuevos para guardar en el alumno
            CalificacionRow rowFinal = new CalificacionRow(rowTemp.materiaProperty().get());
            rowFinal.p1Property().set(rowTemp.p1Property().get());
            rowFinal.p2Property().set(rowTemp.p2Property().get());
            rowFinal.p3Property().set(rowTemp.p3Property().get());
            rowFinal.calcularPromedio();
            nuevasDefinitivas.add(rowFinal);
        }

        // Guardamos en el alumno
        alumnoActual.setCalificaciones(nuevasDefinitivas);

        System.out.println("Calificaciones guardadas para: " + alumnoActual.getNombre());
        cerrarVentana();
    }

    private void cerrarVentana() {
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