package org.example.calificaciones.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.calificaciones.models.Alumno;
import org.example.calificaciones.models.DatosGlobales;
import org.example.calificaciones.models.Materia;

public class ModificarMateriasController {

    @FXML private TableView<Materia> tablaMaterias;
    @FXML private TableColumn<Materia, String> colMateria;
    @FXML private TableColumn<Materia, Void> colAcciones;

    private ObservableList<Materia> listaTemporal;

    @FXML
    public void initialize() {
        // Cargar lista temporal
        listaTemporal = FXCollections.observableArrayList(DatosGlobales.getInstance().getListaMaterias());

        // 1. Configurar columna de Nombre (CON AUTO-GUARDADO)
        colMateria.setCellValueFactory(cell -> cell.getValue().nombreProperty());

        // 👇 AQUÍ ESTÁ EL TRUCO: Definimos una celda personalizada
        colMateria.setCellFactory(col -> new TableCell<Materia, String>() {
            private final TextField textField = new TextField();

            {
                // Guardar al dar ENTER
                textField.setOnAction(e -> guardarEdicion());

                // Guardar al PERDER EL FOCO (Clic afuera o en Guardar)
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        guardarEdicion();
                    }
                });
            }

            private void guardarEdicion() {
                if (isEditing()) {
                    commitEdit(textField.getText());
                    // Actualizar el modelo manualmente para asegurar
                    if (getTableRow().getItem() != null) {
                        getTableRow().getItem().setNombre(textField.getText());
                    }
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                textField.setText(getItem());
                setText(null);
                setGraphic(textField);
                textField.selectAll();
                textField.requestFocus();
            }

            @Override
            public void cancelEdit() {
                // Sobreescribimos cancelar para que NO cancele al perder foco, sino que guarde
                // (La lógica real está en el listener de focus arriba)
                super.cancelEdit();
                setText(getItem());
                setGraphic(null);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    if (isEditing()) {
                        textField.setText(item);
                        setText(null);
                        setGraphic(textField);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
            }
        });

        tablaMaterias.setEditable(true);

        // 2. Configurar columna Eliminar
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEliminar = new Button("🗑");
            {
                btnEliminar.setStyle("-fx-text-fill: red; -fx-cursor: hand;");
                btnEliminar.setTooltip(new Tooltip("Eliminar Materia"));
                btnEliminar.setOnAction(e -> {
                    listaTemporal.remove(getIndex());
                    tablaMaterias.refresh();
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEliminar);
            }
        });

        tablaMaterias.setItems(listaTemporal);
    }

    @FXML
    private void agregarMateria() {
        Materia nueva = new Materia("Nueva Materia");
        listaTemporal.add(nueva);

        // Truco visual: Seleccionar la nueva y ponerla en modo edición inmediatamente
        int nuevoIndex = listaTemporal.size() - 1;
        tablaMaterias.getSelectionModel().select(nuevoIndex);
        tablaMaterias.edit(nuevoIndex, colMateria);
    }

    @FXML
    private void guardarCambios() {
        // 1. Actualizar lista global
        DatosGlobales.getInstance().getListaMaterias().setAll(listaTemporal);

        // 2. Actualizar a todos los alumnos
        for (Alumno alumno : DatosGlobales.getInstance().getListaAlumnos()) {
            alumno.inicializarMaterias(listaTemporal);
        }

        cerrarVentana();
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaMaterias.getScene().getWindow();
        stage.close();
    }
}