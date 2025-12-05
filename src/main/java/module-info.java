module org.example.calificaciones {
        requires javafx.controls;
        requires javafx.fxml;
        requires java.desktop;


        opens org.example.calificaciones.controllers to javafx.fxml;
        opens org.example.calificaciones.models to javafx.base;

        exports org.example.calificaciones;
}
