package org.example.calificaciones.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatosGlobales {
    private static DatosGlobales instancia;
    private ObservableList<Alumno> listaAlumnos;
    private ObservableList<Materia> listaMaterias;

    // ⭐ NUEVO: Lista de Docentes
    private ObservableList<Docente> listaDocentes;

    private String usuarioActual;

    private DatosGlobales() {
        listaAlumnos = FXCollections.observableArrayList();
        listaMaterias = FXCollections.observableArrayList();
        listaDocentes = FXCollections.observableArrayList(); // Inicializar
        cargarDatosIniciales();
    }

    public static DatosGlobales getInstance() {
        if (instancia == null) instancia = new DatosGlobales();
        return instancia;
    }

    public ObservableList<Alumno> getListaAlumnos() { return listaAlumnos; }
    public ObservableList<Materia> getListaMaterias() { return listaMaterias; }

    // ⭐ Getter para Docentes
    public ObservableList<Docente> getListaDocentes() { return listaDocentes; }

    public String getUsuarioActual() { return usuarioActual; }
    public void setUsuarioActual(String usuarioActual) { this.usuarioActual = usuarioActual; }

    private void cargarDatosIniciales() {
        // Materias
        listaMaterias.add(new Materia("Arte y Cultura"));
        listaMaterias.add(new Materia("Biología y Ciencias de la Vida"));
        listaMaterias.add(new Materia("Matemáticas: Problemas de la Vida Diaria"));
        listaMaterias.add(new Materia("Formación Cívica y Ética"));
        listaMaterias.add(new Materia("Español: Principios del Lenguaje"));
        listaMaterias.add(new Materia("Ingles IV"));

        // Alumnos
        Alumno a1 = new Alumno(1, "William Samano");
        a1.inicializarMaterias(listaMaterias);
        listaAlumnos.add(a1);

        // ⭐ NUEVO: Cargar Docentes de Ejemplo (Según tu imagen)
        listaDocentes.add(new Docente(1, "Shanny Ramos"));
        listaDocentes.add(new Docente(2, "Murat Castro"));
        listaDocentes.add(new Docente(3, "Leonardo Espinoza"));
        listaDocentes.add(new Docente(4, "Angelica Tapia"));
        listaDocentes.add(new Docente(5, "Monserrat Torres"));
        listaDocentes.add(new Docente(6, "Samuel Rivera"));
        listaDocentes.add(new Docente(7, "Mauricio Velazquez"));
    }
}