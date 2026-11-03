package com.proyecto.GestionCitasExpedientes.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.GestionCitasExpedientes.modelo.Medico;

public interface MedicoRepositorio extends JpaRepository<Medico, Integer>{
    boolean existsByColegiado(String colegiado);
    boolean existsByEspecialidad_IdEspecialidad(Integer idEspecialidad);
}
