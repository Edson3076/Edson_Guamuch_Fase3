package com.proyecto.GestionCitasExpedientes;

import com.proyecto.GestionCitasExpedientes.modelo.*;
import com.proyecto.GestionCitasExpedientes.Servicio.*;
import com.proyecto.GestionCitasExpedientes.vista.AppJavaFX;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestionCitasExpendientesApplication {

    public static void main(String[] args) {
        // Iniciar la aplicación gráfica JavaFX
        AppJavaFX.lanzar(args);
    }
        
    @Bean
    CommandLineRunner seedData(
            EspecialidadServicio especialidadServicio,
            MedicoServicio medicoServicio,
            PacienteServicio pacienteServicio,
            ExpedienteServicio expedienteServicio,
            CitaServicio citaServicio,
            ConsultaServicio consultaServicio) {
        return args -> {
            try {
                
                Paciente encontrado = pacienteServicio.buscarPorId(2).orElse(null);
                if (encontrado != null) {
                    encontrado.setNombre("Juan Carlos Perez");
                    pacienteServicio.actualizar(encontrado.getIdPersona(), encontrado);
                }
            } catch (Exception e) {
                // Silencioso si la base de datos está vacía o recién inicializada
            }
        };
    }    

}
