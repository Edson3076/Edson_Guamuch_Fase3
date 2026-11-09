package com.proyecto.GestionCitasExpedientes.vista;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ControladorPrincipal {

    @FXML
    private Label lblUsuarioNombre;

    @FXML
    private Label lblUsuarioRol;

    @FXML
    private Label lblEstadoBD;

    @FXML
    private Label lblTituloSeccion;

    @FXML
    private Label lblSubtituloSeccion;

    @FXML
    private Label lblFechaHora;

    @FXML
    private Label lblBadgeEstado;

    @FXML
    private Button btnNavDashboard;

    @FXML
    private Button btnNavPacientes;

    @FXML
    private Button btnNavMedicos;

    @FXML
    private Button btnNavCitas;

    @FXML
    private Button btnNavConsultas;

    @FXML
    private StackPane contenedorContenido;

    // Caché de vistas cargadas
    private final Map<String, Parent> cacheVistas = new HashMap<>();
    private final Map<String, Object> cacheControladores = new HashMap<>();

    @FXML
    public void initialize() {
        iniciarReloj();
        actualizarEstadoBD();
    }

    public void setDatosUsuario(String usuario, String rol) {
        lblUsuarioNombre.setText(usuario != null && !usuario.isBlank() ? usuario : "Usuario");
        lblUsuarioRol.setText(rol != null && !rol.isBlank() ? rol : "● Conectado");
    }
    
    private void actualizarEstadoBD() {
        boolean enBD = ServicioDatos.getInstancia().isModoBaseDatos();
        if (enBD) {
            lblEstadoBD.setText("●"); //Comentario: Base de Datos Conectada
            lblEstadoBD.setStyle("-fx-text-fill: #34d399; -fx-font-size: 11px; -fx-font-weight: bold;");
            lblBadgeEstado.setText("MySQL Activo");
            lblBadgeEstado.getStyleClass().setAll("status-badge-online");
        } else {
            lblEstadoBD.setText("● Modo Local (En Memoria)");
            lblEstadoBD.setStyle("-fx-text-fill: #fbbf24; -fx-font-size: 11px; -fx-font-weight: bold;");
            lblBadgeEstado.setText("Modo Local Activo");
            lblBadgeEstado.getStyleClass().setAll("status-badge-memory");
        }
    }
    
    private void iniciarReloj() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy • HH:mm:ss");
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            lblFechaHora.setText(LocalDateTime.now().format(dtf));
        }), new KeyFrame(Duration.seconds(1)));
        //clock.setCycleCount(Animation.INDEFINITE);
        //clock.play();
    }
    
    private void destacarBoton(Button botonActivo) {
        Button[] botones = {btnNavDashboard, btnNavPacientes, btnNavMedicos, btnNavCitas, btnNavConsultas};
        for (Button btn : botones) {
            if (btn == botonActivo) {
                btn.getStyleClass().remove("sidebar-nav-btn");
                if (!btn.getStyleClass().contains("sidebar-nav-btn-active")) {
                    btn.getStyleClass().add("sidebar-nav-btn-active");
                }
            } else {
                btn.getStyleClass().remove("sidebar-nav-btn-active");
                if (!btn.getStyleClass().contains("sidebar-nav-btn")) {
                    btn.getStyleClass().add("sidebar-nav-btn");
                }
            }
        }
    }

    private void cargarOActualizarVista(String rutaFxml, String titulo, String subtitulo, Button botonNav, Runnable refrescoCallback) {
        destacarBoton(botonNav);
        lblTituloSeccion.setText(titulo);
        lblSubtituloSeccion.setText(subtitulo);

        try {
            // Siempre recrear o actualizar para mantener los datos sincronizados entre pantallas
            Navegador.VistaCargada<Object> vc = Navegador.cargarVista(rutaFxml);
            contenedorContenido.getChildren().setAll(vc.getNodo());
            if (refrescoCallback != null) {
                refrescoCallback.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Navegador.mostrarAlerta(javafx.scene.control.Alert.AlertType.ERROR, "Error al cargar vista", "No se pudo cargar: " + rutaFxml, e.getMessage());
        }
    }

    @FXML
    public void navegarADashboard() {
        cargarOActualizarVista(
                "/vistas/Dashboard.fxml",
                "Panel de Control y Resumen Operativo",
                "Métricas en tiempo real, citas del día y accesos rápidos",
                btnNavDashboard,
                null
        );
    }

    @FXML
    public void navegarAPacientes() {
        cargarOActualizarVista(
                "/vistas/Pacientes.fxml",
                "Gestión de Pacientes y Expedientes Clínicos",
                "Registro, consulta, actualización y control de expedientes médicos",
                btnNavPacientes,
                null
        );
    }

    @FXML
    public void navegarAMedicos() {
        cargarOActualizarVista(
                "/vistas/Medicos.fxml",
                "Directorio Médico y Especialidades",
                "Control de profesionales de la salud, horarios de atención y especialidades",
                btnNavMedicos,
                null
        );
    }

    @FXML
    public void navegarACitas() {
        cargarOActualizarVista(
                "/vistas/Citas.fxml",
                "Agenda y Programación de Citas Médicas",
                "Control de turnos, confirmación, costos y cancelación de citas",
                btnNavCitas,
                null
        );
    }

    @FXML
    public void navegarAConsultas() {
        cargarOActualizarVista(
                "/vistas/Consultas.fxml",
                "Consultas Médicas y Diagnósticos",
                "Atención clínica, registro de síntomas, diagnósticos y prescripciones",
                btnNavConsultas,
                null
        );
    }

    @FXML
    private void handleCerrarSesion() {
        boolean confirmar = Navegador.confirmar(
                "Cerrar Sesión",
                "¿Desea cerrar la sesión actual?",
                "Regresará a la pantalla inicial de autenticación."
        );
        if (confirmar) {
            Navegador.mostrarLogin();
        }
    }
}
