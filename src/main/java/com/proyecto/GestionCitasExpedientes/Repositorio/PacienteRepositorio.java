package com.proyecto.GestionCitasExpedientes.Repositorio;

import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepositorio extends JpaRepository<Paciente, Integer>{
    boolean existsByEmail(String email);
}
