package org.example.calificaciones.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatosGlobales {
    private static DatosGlobales instancia;
    private ObservableList<Alumno> listaAlumnos;

    // ⭐ NUEVO: Lista global de materias
    private ObservableList<Materia> listaMaterias;
    private String usuarioActual;

    private DatosGlobales() {
        listaAlumnos = FXCollections.observableArrayList();
        listaMaterias = FXCollections.observableArrayList();
        cargarDatosIniciales();
    }

    public static DatosGlobales getInstance() {
        if (instancia == null) {
            instancia = new DatosGlobales();
        }
        return instancia;
    }

    public ObservableList<Alumno> getListaAlumnos() { return listaAlumnos; }

    // ⭐ NUEVO: Getter para materias
    public ObservableList<Materia> getListaMaterias() { return listaMaterias; }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    private void cargarDatosIniciales() {
        // Cargar materias por defecto
        listaMaterias.add(new Materia("Arte y Cultura"));
        listaMaterias.add(new Materia("Biología y Ciencias de la Vida"));
        listaMaterias.add(new Materia("Matemáticas: Problemas de la Vida Diaria"));
        listaMaterias.add(new Materia("Formación Cívica y Ética"));
        listaMaterias.add(new Materia("Español: Principios del Lenguaje"));
        listaMaterias.add(new Materia("Ingles IV"));

        // Cargar alumnos (con sus calificaciones basadas en las materias)
        Alumno a1 = new Alumno(1, "Manuel Torres Rivera");
        a1.inicializarMaterias(listaMaterias); // Método nuevo que crearemos
        listaAlumnos.add(a1);

        // ... agrega más alumnos si quieres ...
    }

}