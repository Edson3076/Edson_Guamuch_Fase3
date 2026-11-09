package com.proyecto.GestionCitasExpedientes.vista;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Gestor centralizado de navegación y vistas de la aplicación JavaFX.
 * Controla el flujo entre la pantalla inicial de Login y el panel principal
 * con sus 5 pantallas modulares.
 */
public class Navegador {

    private static Stage stagePrincipal;
    private static ControladorPrincipal controladorPrincipal;

    public static void setStage(Stage stage) {
        stagePrincipal = stage;
    }

    public static Stage getStage() {
        return stagePrincipal;
    }

    public static void setControladorPrincipal(ControladorPrincipal controlador) {
        controladorPrincipal = controlador;
    }

    public static ControladorPrincipal getControladorPrincipal() {
        return controladorPrincipal;
    }

    /**
     * Muestra la pantalla inicial de Login.
     */
    public static void mostrarLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource("/vistas/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 480, 520);
            scene.getStylesheets().add(Navegador.class.getResource("/css/estilos.css").toExternalForm());

            stagePrincipal.setTitle("Sistema de Citas y Expedientes - Inicio de Sesión");
            stagePrincipal.setScene(scene);
            stagePrincipal.setResizable(false);
            stagePrincipal.centerOnScreen();
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Carga", "No se pudo cargar la vista de Login", e.getMessage());
        }
    }

    /**
     * Muestra la ventana principal con su barra lateral de navegación y área de contenido.
     */
    public static void mostrarPrincipal(String usuario, String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource("/vistas/Principal.fxml"));
            Parent root = loader.load();
            controladorPrincipal = loader.getController();
            controladorPrincipal.setDatosUsuario(usuario, rol);

            Scene scene = new Scene(root, 1200, 750);
            scene.getStylesheets().add(Navegador.class.getResource("/css/estilos.css").toExternalForm());

            stagePrincipal.setTitle("Sistema Clínico - Citas y Expedientes Médicos");
            stagePrincipal.setScene(scene);
            stagePrincipal.setResizable(true);
            stagePrincipal.setMinWidth(1080);
            stagePrincipal.setMinHeight(650);
            stagePrincipal.centerOnScreen();
            stagePrincipal.show();

            // Cargar Dashboard por defecto (Pantalla 1)
            controladorPrincipal.navegarADashboard();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la pantalla principal", e.getMessage());
        }
    }

    /**
     * Carga una vista modular FXML.
     */
    public static <T> VistaCargada<T> cargarVista(String rutaFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(rutaFxml));
        Parent nodo = loader.load();
        T controlador = loader.getController();
        return new VistaCargada<>(nodo, controlador);
    }

    public static class VistaCargada<T> {
        private final Parent nodo;
        private final T controlador;

        public VistaCargada(Parent nodo, T controlador) {
            this.nodo = nodo;
            this.controlador = controlador;
        }

        public Parent getNodo() {
            return nodo;
        }

        public T getControlador() {
            return controlador;
        }
    }

    public static void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.initOwner(stagePrincipal);
        alert.showAndWait();
    }

    public static boolean confirmar(String titulo, String encabezado, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.initOwner(stagePrincipal);
        Optional<ButtonType> res = alert.showAndWait();
        return res.isPresent() && res.get() == ButtonType.OK;
    }
}
