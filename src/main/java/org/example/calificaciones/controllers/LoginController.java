package org.example.calificaciones.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Button btnLogin;


    private void hacerLogin() {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            System.out.println("Campos vacíos.");
            return;
        }

        // Aquí pondrás la lógica real
        System.out.println("Usuario: " + usuario);
        System.out.println("Contraseña: " + contrasena);
    }

    @FXML
    private void onLoginClicked(ActionEvent event) {
        hacerLogin();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/principal.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Página Principal");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

