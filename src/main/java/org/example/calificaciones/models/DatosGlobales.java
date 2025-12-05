package org.example.calificaciones.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
// Importamos FilteredList para poder filtrar alumnos
import javafx.collections.transformation.FilteredList;

public class DatosGlobales {
    private static DatosGlobales instancia;
    private ObservableList<Alumno> listaAlumnos;
    private ObservableList<Materia> listaMaterias;
    private ObservableList<Docente> listaDocentes;

    private String usuarioActual; // Ej: "Shanny Ramos"

    private DatosGlobales() {
        listaAlumnos = FXCollections.observableArrayList();
        listaMaterias = FXCollections.observableArrayList();
        listaDocentes = FXCollections.observableArrayList();
        cargarDatosIniciales();
    }

    public static DatosGlobales getInstance() {
        if (instancia == null) instancia = new DatosGlobales();
        return instancia;
    }

    public ObservableList<Alumno> getListaAlumnos() { return listaAlumnos; }
    public ObservableList<Materia> getListaMaterias() { return listaMaterias; }
    public ObservableList<Docente> getListaDocentes() { return listaDocentes; }

    public String getUsuarioActual() { return usuarioActual; }
    public void setUsuarioActual(String usuarioActual) { this.usuarioActual = usuarioActual; }

    // ⭐ NUEVO: Obtener el ID del Docente logueado actualmente
    public int getIdDocenteLogueado() {
        if (usuarioActual == null) return -1;

        for (Docente d : listaDocentes) {
            if (d.getNombre().equalsIgnoreCase(usuarioActual)) {
                return d.getNumero();
            }
        }
        return -1; // No encontrado
    }

    // ⭐ NUEVO: Obtener solo los alumnos del maestro actual
    public FilteredList<Alumno> getAlumnosDelDocenteActual() {
        int idDocente = getIdDocenteLogueado();

        // Creamos una lista filtrada que se actualiza sola
        FilteredList<Alumno> filtrados = new FilteredList<>(listaAlumnos);

        // La condición: solo pasar si el ID del docente coincide
        filtrados.setPredicate(alumno -> alumno.getIdDocente() == idDocente);

        return filtrados;
    }

    private void cargarDatosIniciales() {
        // Materias
        listaMaterias.add(new Materia("Arte y Cultura"));
        listaMaterias.add(new Materia("Biología y Ciencias de la Vida"));
        listaMaterias.add(new Materia("Matemáticas: Problemas de la Vida Diaria"));
        listaMaterias.add(new Materia("Formación Cívica y Ética"));
        listaMaterias.add(new Materia("Español: Principios del Lenguaje"));
        listaMaterias.add(new Materia("Ingles IV"));

        // Docentes
        listaDocentes.add(new Docente(1, "Shanny Ramos"));
        listaDocentes.add(new Docente(2, "Murat Castro"));
        // ... otros docentes

        // Alumnos (Asignamos a Shanny [ID 1] el alumno William)
        // NOTA: El constructor cambió, ahora pide el ID del docente al final (1)
        Alumno a1 = new Alumno(1, "William Samano", 1);
        a1.inicializarMaterias(listaMaterias);
        listaAlumnos.add(a1);

        // Ejemplo: Un alumno para Murat Castro (ID 2) para probar que no se mezclan
        Alumno a2 = new Alumno(2, "Jorge Lopez", 2);
        a2.inicializarMaterias(listaMaterias);
        listaAlumnos.add(a2);
    }
}