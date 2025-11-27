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

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnLogin;

    @FXML
    private void onLoginClicked(ActionEvent event) {
        // Usamos trim() para quitar espacios accidentales al inicio o final
        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor ingresa usuario y contraseña.");
            return;
        }

        // ⭐ VALIDACIÓN DE USUARIOS PERMITIDOS
        // Aceptamos "Director" O "Jazmin Rogel" (ignorando mayúsculas)
        boolean esDirector = usuario.equalsIgnoreCase("Director");
        boolean esJazmin = usuario.equalsIgnoreCase("Jazmin Rogel");

        // La contraseña sigue siendo "123"
        boolean passCorrecta = contrasena.equals("123");

        if ((esDirector || esJazmin) && passCorrecta) {

            // 1. Guardamos el usuario (para mostrarlo en la pantalla principal)
            // Si escribió "director" en minúsculas, guardamos "Director" bonito, o el nombre tal cual
            String nombreAGuardar = esDirector ? "Director" : usuario;
            DatosGlobales.getInstance().setUsuarioActual(nombreAGuardar);

            // 2. Entramos al sistema
            ingresarAlSistema(event);

        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Acceso Denegado",
                    "Usuario no registrado o contraseña incorrecta.\nSolo pueden ingresar: Director o Jazmin Rogel.");
        }
    }

    private void ingresarAlSistema(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Principal.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            // Personalizamos el título de la ventana
            String titulo = "Sistema de Calificaciones - " + DatosGlobales.getInstance().getUsuarioActual();
            stage.setTitle(titulo);

            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la pantalla principal.");
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