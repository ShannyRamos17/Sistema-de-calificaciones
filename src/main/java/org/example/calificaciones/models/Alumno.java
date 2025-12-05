package org.example.calificaciones.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Alumno {
    private final SimpleIntegerProperty numero;
    private final SimpleStringProperty nombre;

    // ⭐ NUEVO: Guardamos el ID del docente que creó al alumno
    private int idDocente;

    // La "mochila" de calificaciones
    private ObservableList<CalificacionRow> calificaciones;

    // Actualizamos el constructor para pedir el ID del Docente
    public Alumno(int numero, String nombre, int idDocente) {
        this.numero = new SimpleIntegerProperty(numero);
        this.nombre = new SimpleStringProperty(nombre);
        this.idDocente = idDocente; // Guardamos el dueño del alumno
        this.calificaciones = FXCollections.observableArrayList();
    }

    public int getNumero() { return numero.get(); }
    public SimpleIntegerProperty numeroProperty() { return numero; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public SimpleStringProperty nombreProperty() { return nombre; }

    // Getter para saber de quién es este alumno
    public int getIdDocente() { return idDocente; }

    public ObservableList<CalificacionRow> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(ObservableList<CalificacionRow> nuevasCalificaciones) {
        this.calificaciones.setAll(nuevasCalificaciones);
    }

    public void inicializarMaterias(ObservableList<Materia> materiasGlobales) {
        ObservableList<CalificacionRow> nuevaLista = FXCollections.observableArrayList();

        for (Materia mGlobal : materiasGlobales) {
            boolean encontrada = false;
            for (CalificacionRow rowActual : this.calificaciones) {
                if (rowActual.materiaProperty().get().equals(mGlobal.getNombre())) {
                    nuevaLista.add(rowActual);
                    encontrada = true;
                    break;
                }
            }
            if (!encontrada) {
                nuevaLista.add(new CalificacionRow(mGlobal.getNombre()));
            }
        }
        this.calificaciones.setAll(nuevaLista);
    }
}