package org.example.calificaciones.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Docente {
    private final SimpleIntegerProperty numero;
    private final SimpleStringProperty nombre;

    public Docente(int numero, String nombre) {
        this.numero = new SimpleIntegerProperty(numero);
        this.nombre = new SimpleStringProperty(nombre);
    }

    public int getNumero() { return numero.get(); }
    public SimpleIntegerProperty numeroProperty() { return numero; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public SimpleStringProperty nombreProperty() { return nombre; }
}
