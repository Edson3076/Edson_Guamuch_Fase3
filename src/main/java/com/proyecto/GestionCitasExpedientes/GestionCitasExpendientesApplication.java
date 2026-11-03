package com.proyecto.GestionCitasExpedientes;

import com.proyecto.GestionCitasExpedientes.modelo.*;
import com.proyecto.GestionCitasExpedientes.Servicio.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestionCitasExpendientesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionCitasExpendientesApplication.class, args);
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
            /*if (!especialidadServicio.listar().isEmpty()) {
                System.out.println("Datos semilla ya existentes. Omitiendo seedData.");
                return;
            }*/
            
            /*
            // CREATE
            Paciente pac = new Paciente();
            pac.setNombre("Juan Perez");
            pac.setDireccion("zona 2 1-26 san pedro sac");
            pac.setTelefono("55301689");
            pac.setEmail("paciente.prueba@example.com");
            pac.setTipoSangre("O+");
            pac.setGenero("H");
            pac.setEdad(30);
            pac = pacienteServicio.registrar(pac);
            System.out.println("Paciente creado id=" + pac.getIdPersona());
            */
            
            
            // READ
Paciente encontrado = pacienteServicio
        .buscarPorId(2)
        .orElse(null);
            /*
            System.out.println("\n=== LISTADO DE PACIENTES ===");
            pacienteServicio.listar()
            .forEach(p -> System.out.println(
                p.getIdPersona() + " - " + p.getNombre()));
            */
            
            //Actualizar
            if (encontrado != null) {
    encontrado.setNombre("Juan Carlos Perez");
    pacienteServicio.actualizar(
            encontrado.getIdPersona(),
            encontrado);

    System.out.println("Paciente actualizado");
}
            
            /*
            //INSERT
            // --- Especialidad
            Especialidad esp = new Especialidad();
            esp.setNombre("Medicina General");
            esp.setDescripcion("Atención primaria");
            esp.setCostoConsulta(50.0);
            esp = especialidadServicio.crear(esp);
            System.out.println("Especialidad creada id=" + esp.getIdEspecialidad());
            
            // --- Medico
            Medico med = new Medico();
            med.setNombre("Dr. Prueba");
            med.setDireccion("Av. Central 123");
            med.setTelefono("555-0001");
            med.setEmail("dr.prueba@clinica.com");
            med.setColegiado("COL-1000");
            med.setHorarioAtencion("08:00-12:00;14:00-18:00");
            med.setEspecialidad(esp);
            med = medicoServicio.registrar(med);
            System.out.println("Medico creado id=" + med.getIdPersona());
            
            // --- Paciente
            Paciente pac = new Paciente();
            pac.setNombre("Paciente Demo");
            pac.setDireccion("Calle Falsa 123");
            pac.setTelefono("555-0002");
            pac.setEmail("paciente.demo@example.com");
            pac.setTipoSangre("O+");
            pac.setGenero("M");
            pac.setEdad(30);
            pac = pacienteServicio.registrar(pac);
            System.out.println("Paciente creado id=" + pac.getIdPersona());
            
            // --- Expediente (el servicio lo crea automáticamente al registrar el paciente)
            ExpedienteMedico exp = pac.getExpediente();
            if (exp != null) {
                System.out.println("Expediente creado id=" + exp.getIdExpediente());
            }
            
            // --- Cita
            Cita cita = new Cita();
            cita.setPaciente(pac);
            cita.setMedico(med);
            cita.setFecha(LocalDate.now().plusDays(2));
            cita.setHora(LocalTime.of(9, 0));
            cita = citaServicio.programarCita(cita);
            System.out.println("Cita creada id=" + cita.getIdCita());
            
            // --- Consulta
            Consulta consulta = new Consulta();
            consulta.setCita(cita);
            consulta.setSintomas("Dolor de cabeza");
            consulta.setDiagnostico("Migraña leve");
            consulta.setTratamiento("Reposo y analgesia");
            // registrarConsulta valida la cita y enlaza expediente
            consulta = consultaServicio.registrarConsulta(consulta);
            System.out.println("Consulta creada id=" + consulta.getIdConsulta());
            
            System.out.println("Seed data completada");
            */
        };
    }    

}
