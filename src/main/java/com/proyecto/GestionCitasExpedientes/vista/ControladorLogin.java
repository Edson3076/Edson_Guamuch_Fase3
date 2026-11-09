package com.proyecto.GestionCitasExpedientes.vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControladorLogin {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    @FXML
    private Button btnIngresar;

    @FXML
    public void initialize() {
        // Valores por defecto sencillos para acceso rápido
        txtUsuario.setText("admin");
        txtPassword.setText("admin");
    }

    @FXML
    private void handleLogin() {
        String usuario = txtUsuario.getText() != null ? txtUsuario.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText().trim() : "";

        if (usuario.isEmpty()) {
            mostrarError("Ingresar usuario.");
            txtUsuario.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mostrarError("Ingresar contraseña.");
            txtPassword.requestFocus();
            return;
        }

        ServicioDatos.getInstancia().setUsuarioActual(usuario);
        ServicioDatos.getInstancia().setRolActual("En línea");

        // Transición directa a la pantalla principal
        Navegador.mostrarPrincipal(usuario, "En línea");
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText("⚠️ " + mensaje);
        lblMensaje.getStyleClass().setAll("alert-error");
        lblMensaje.setVisible(true);
        lblMensaje.setManaged(true);
    }
}
