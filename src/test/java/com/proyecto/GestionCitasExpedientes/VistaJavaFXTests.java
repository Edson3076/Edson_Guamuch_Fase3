package com.proyecto.GestionCitasExpedientes;

import com.proyecto.GestionCitasExpedientes.modelo.*;
import com.proyecto.GestionCitasExpedientes.vista.ServicioDatos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VistaJavaFXTests {

    @Test
    void testRecursosExisten() {
        String[] recursos = {
                "/css/estilos.css",
                "/vistas/Login.fxml",
                "/vistas/Principal.fxml",
                "/vistas/Dashboard.fxml",
                "/vistas/Pacientes.fxml",
                "/vistas/Medicos.fxml",
                "/vistas/Citas.fxml",
                "/vistas/Consultas.fxml"
        };

        for (String r : recursos) {
            URL url = getClass().getResource(r);
            Assertions.assertNotNull(url, "El recurso debe existir en el classpath: " + r);
        }
    }

    @Test
    void testCargaFXMLs() throws Exception {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Ya iniciado
        }

        String[] fxmls = {
                "/vistas/Login.fxml",
                "/vistas/Principal.fxml",
                "/vistas/Dashboard.fxml",
                "/vistas/Pacientes.fxml",
                "/vistas/Medicos.fxml",
                "/vistas/Citas.fxml",
                "/vistas/Consultas.fxml"
        };

        for (String fxml : fxmls) {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(fxml));
            javafx.scene.Parent root = loader.load();
            Assertions.assertNotNull(root, "La vista FXML debe cargar correctamente: " + fxml);
            Assertions.assertNotNull(loader.getController(), "El controlador debe estar asociado: " + fxml);
        }
    }

    @Test
    void testServicioDatosMemoria() {
        ServicioDatos sd = ServicioDatos.getInstancia();

        // 1. Validar lista inicial de especialidades
        List<Especialidad> especialidades = sd.listarEspecialidades();
        Assertions.assertFalse(especialidades.isEmpty(), "Deben existir especialidades semilla.");

        // 2. Validar lista inicial de médicos
        List<Medico> medicos = sd.listarMedicos();
        Assertions.assertFalse(medicos.isEmpty(), "Deben existir médicos semilla.");

        // 3. Validar lista inicial de pacientes y expedientes
        List<Paciente> pacientes = sd.listarPacientes();
        Assertions.assertFalse(pacientes.isEmpty(), "Deben existir pacientes semilla.");
        Assertions.assertNotNull(pacientes.get(0).getExpediente(), "El paciente debe tener expediente asociado.");

        // 4. Validar programación de cita
        Cita nuevaCita = new Cita();
        nuevaCita.setPaciente(pacientes.get(0));
        nuevaCita.setMedico(medicos.get(0));
        nuevaCita.setFecha(LocalDate.now().plusDays(5));
        nuevaCita.setHora(LocalTime.of(16, 0));
        Cita citaRegistrada = sd.programarCita(nuevaCita);
        Assertions.assertNotNull(citaRegistrada.getIdCita(), "La cita debe obtener ID.");
        Assertions.assertTrue(citaRegistrada.getConfirmada(), "La cita debe crearse confirmada.");

        // 5. Validar registro de consulta médica
        Consulta consulta = new Consulta();
        consulta.setCita(citaRegistrada);
        consulta.setSintomas("Fiebre y dolor de garganta");
        consulta.setDiagnostico("Faringitis aguda");
        consulta.setTratamiento("Amoxicilina 500mg cada 8 horas por 7 días");
        Consulta conRegistrada = sd.registrarConsulta(consulta);
        Assertions.assertNotNull(conRegistrada.getIdConsulta(), "La consulta debe obtener ID.");
        Assertions.assertNotNull(conRegistrada.getExpediente(), "La consulta debe vincularse al expediente.");
    }
}
