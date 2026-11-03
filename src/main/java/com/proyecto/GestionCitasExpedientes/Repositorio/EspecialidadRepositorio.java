package com.proyecto.GestionCitasExpedientes.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.GestionCitasExpedientes.modelo.Especialidad;

public interface EspecialidadRepositorio extends JpaRepository<Especialidad, Integer>{
    
}
