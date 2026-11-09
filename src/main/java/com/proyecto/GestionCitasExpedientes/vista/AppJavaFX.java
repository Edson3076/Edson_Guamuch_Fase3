package com.proyecto.GestionCitasExpedientes.vista;

import com.proyecto.GestionCitasExpedientes.GestionCitasExpendientesApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Clase principal de JavaFX que inicializa el ciclo de vida de la aplicación,
 * orquesta el contexto de Spring Boot de forma no bloqueante y despliega la pantalla
 * de Login como punto de entrada inicial.
 */
public class AppJavaFX extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        try {
            // Inicializar contexto Spring Boot
            springContext = new SpringApplicationBuilder(GestionCitasExpendientesApplication.class)
                    .headless(false)
                    .run(getParameters().getRaw().toArray(new String[0]));
            ServicioDatos.getInstancia().inicializarConSpring(springContext);
        } catch (Exception e) {
            System.err.println("Aviso: Spring Boot / MySQL no se conectó en este entorno: " + e.getMessage());
            System.out.println("Iniciando en modo memoria local pre-cargado para permitir uso total interactivo de la interfaz.");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Navegador.setStage(primaryStage);
        Navegador.mostrarLogin();
    }

    @Override
    public void stop() {
        if (springContext != null && springContext.isActive()) {
            try {
                springContext.close();
            } catch (Exception ignored) {}
        }
        Platform.exit();
    }

    public static void lanzar(String[] args) {
        launch(args);
    }
}
