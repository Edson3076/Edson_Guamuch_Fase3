package com.proyecto.GestionCitasExpedientes.Repositorio;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.GestionCitasExpedientes.modelo.ExpedienteMedico;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;

public interface ExpedienteRepositorio extends JpaRepository<ExpedienteMedico, Integer>{
    Optional<ExpedienteMedico> findByPaciente(Paciente paciente);
}
