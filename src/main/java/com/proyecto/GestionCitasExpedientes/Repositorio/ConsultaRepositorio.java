package com.proyecto.GestionCitasExpedientes.Repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.GestionCitasExpedientes.modelo.Consulta;
import com.proyecto.GestionCitasExpedientes.modelo.ExpedienteMedico;

public interface ConsultaRepositorio extends JpaRepository<Consulta, Integer>{
    List<Consulta> findByExpediente(ExpedienteMedico expediente);
}
