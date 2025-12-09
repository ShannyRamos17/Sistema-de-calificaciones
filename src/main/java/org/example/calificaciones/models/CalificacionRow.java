package org.example.calificaciones.models;

import javafx.beans.property.SimpleStringProperty;

public class CalificacionRow {
    private final SimpleStringProperty materia;
    private final SimpleStringProperty p1;
    private final SimpleStringProperty p2;
    private final SimpleStringProperty p3;
    private final SimpleStringProperty total;

    public CalificacionRow(String materia) {
        this.materia = new SimpleStringProperty(materia);
        this.p1 = new SimpleStringProperty("");
        this.p2 = new SimpleStringProperty("");
        this.p3 = new SimpleStringProperty("");
        this.total = new SimpleStringProperty("");
    }

    // Getters y Properties
    public SimpleStringProperty materiaProperty() { return materia; }
    public SimpleStringProperty p1Property() { return p1; }
    public SimpleStringProperty p2Property() { return p2; }
    public SimpleStringProperty p3Property() { return p3; }
    public SimpleStringProperty totalProperty() { return total; }


    public void calcularPromedio() {
        try {
            double n1 = p1.get().isEmpty() ? 0 : Double.parseDouble(p1.get());
            double n2 = p2.get().isEmpty() ? 0 : Double.parseDouble(p2.get());
            double n3 = p3.get().isEmpty() ? 0 : Double.parseDouble(p3.get());

            if (!p1.get().isEmpty() && !p2.get().isEmpty() && !p3.get().isEmpty()) {
                double prom = (n1 + n2 + n3) / 3;
                total.set(String.format("%.1f", prom));
            } else {
                total.set("");
            }
        } catch (NumberFormatException e) {
            total.set("Error");
        }
    }
}
