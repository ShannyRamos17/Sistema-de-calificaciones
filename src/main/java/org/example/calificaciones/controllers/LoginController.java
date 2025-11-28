package org.example.calificaciones.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.calificaciones.models.DatosGlobales;
import org.example.calificaciones.models.Docente;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnLogin;

    @FXML
    private void onLoginClicked(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor ingresa usuario y contraseña.");
            return;
        }

        // 1. Verificar si es Administrador (Director o Josué)
        boolean esDirector = usuario.equalsIgnoreCase("Director");
        boolean esJosue = usuario.equalsIgnoreCase("Josué Gael Aguirre Delgado");
        boolean esAdmin = esDirector || esJosue;

        // 2. Verificar si es un Docente registrado en la base de datos
        boolean esDocente = false;

        // Recorremos la lista global de docentes para ver si el usuario existe
        for (Docente d : DatosGlobales.getInstance().getListaDocentes()) {
            if (d.getNombre().equalsIgnoreCase(usuario)) {
                esDocente = true;
                break;
            }
        }

        // Contraseña universal (para el ejemplo)
        boolean passCorrecta = contrasena.equals("123");

        if ((esAdmin || esDocente) && passCorrecta) {

            // Guardamos el nombre real para mostrarlo en la bienvenida
            String nombreAGuardar = esDirector ? "Director" : usuario;
            DatosGlobales.getInstance().setUsuarioActual(nombreAGuardar);

            // Entramos al sistema según el rol detectado
            ingresarAlSistema(event, esAdmin);

        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Acceso Denegado",
                    "Usuario no encontrado o contraseña incorrecta.");
        }
    }

    private void ingresarAlSistema(ActionEvent event, boolean esAdministrador) {
        try {
            String rutaFXML = esAdministrador ? "/views/PrincipalDirector.fxml" : "/views/Principal.fxml";
            String tituloVentana = esAdministrador ? "Panel de Administración" : "Sistema de Calificaciones - Docente";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(tituloVentana);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la pantalla: " + (esAdministrador ? "Director" : "Docente"));
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}