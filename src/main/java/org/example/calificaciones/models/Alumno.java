package org.example.calificaciones.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Alumno {
    private final SimpleIntegerProperty numero;
    private final SimpleStringProperty nombre;

    // La "mochila" de calificaciones
    private ObservableList<CalificacionRow> calificaciones;

    public Alumno(int numero, String nombre) {
        this.numero = new SimpleIntegerProperty(numero);
        this.nombre = new SimpleStringProperty(nombre);
        this.calificaciones = FXCollections.observableArrayList();
    }

    public int getNumero() { return numero.get(); }
    public SimpleIntegerProperty numeroProperty() { return numero; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public SimpleStringProperty nombreProperty() { return nombre; }

    public ObservableList<CalificacionRow> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(ObservableList<CalificacionRow> nuevasCalificaciones) {
        this.calificaciones.setAll(nuevasCalificaciones);
    }

    // ⭐ NUEVO MÉTODO: Sincronizar con la lista global de materias
    // Este método se llama desde ModificarMateriasController cuando guardas cambios
    public void inicializarMaterias(ObservableList<Materia> materiasGlobales) {

        ObservableList<CalificacionRow> nuevaLista = FXCollections.observableArrayList();

        for (Materia mGlobal : materiasGlobales) {
            boolean encontrada = false;

            // 1. Buscamos si el alumno ya tenía esta materia para NO borrarle la nota
            for (CalificacionRow rowActual : this.calificaciones) {
                if (rowActual.materiaProperty().get().equals(mGlobal.getNombre())) {
                    nuevaLista.add(rowActual); // Conservamos la fila con sus notas viejas
                    encontrada = true;
                    break;
                }
            }

            // 2. Si es una materia nueva (o cambió de nombre), la creamos vacía
            if (!encontrada) {
                nuevaLista.add(new CalificacionRow(mGlobal.getNombre()));
            }
        }

        // 3. Reemplazamos la lista vieja con la nueva (ordenada según las materias globales)
        this.calificaciones.setAll(nuevaLista);
    }
}