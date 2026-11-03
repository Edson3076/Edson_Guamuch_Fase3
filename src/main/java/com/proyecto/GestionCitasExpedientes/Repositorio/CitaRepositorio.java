package com.proyecto.GestionCitasExpedientes.Repositorio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.GestionCitasExpedientes.modelo.Cita;
import com.proyecto.GestionCitasExpedientes.modelo.Medico;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;

public interface CitaRepositorio extends JpaRepository<Cita, Integer>{
    boolean existsByMedicoAndFechaAndHora(Medico medico, LocalDate fecha, LocalTime hora);
    boolean existsByPacienteAndMedicoAndFechaAndConfirmadaTrue(Paciente paciente, Medico medico, LocalDate fecha);
    List<Cita> findByMedicoAndFecha(Medico medico, LocalDate fecha);
}
