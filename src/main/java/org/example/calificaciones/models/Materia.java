package org.example.calificaciones.models;

import javafx.beans.property.SimpleStringProperty;

public class Materia {
    private final SimpleStringProperty nombre;

    public Materia(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
    }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public SimpleStringProperty nombreProperty() { return nombre; }
}
