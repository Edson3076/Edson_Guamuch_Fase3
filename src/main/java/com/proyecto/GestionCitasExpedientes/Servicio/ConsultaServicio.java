package com.proyecto.GestionCitasExpedientes.Servicio;

import com.proyecto.GestionCitasExpedientes.modelo.Consulta;
import com.proyecto.GestionCitasExpedientes.modelo.Cita;
import com.proyecto.GestionCitasExpedientes.modelo.ExpedienteMedico;
import com.proyecto.GestionCitasExpedientes.exception.ResourceNotFoundException;
import com.proyecto.GestionCitasExpedientes.exception.BusinessException;
import com.proyecto.GestionCitasExpedientes.Repositorio.ConsultaRepositorio;
import com.proyecto.GestionCitasExpedientes.Repositorio.ExpedienteRepositorio;
import com.proyecto.GestionCitasExpedientes.Repositorio.CitaRepositorio;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ConsultaServicio {
    private final ConsultaRepositorio consultaRepo;
    private final CitaRepositorio citaRepo;
    private final ExpedienteRepositorio expedienteRepo;

    public ConsultaServicio(ConsultaRepositorio consultaRepo, CitaRepositorio citaRepo, ExpedienteRepositorio expedienteRepo) {
        this.consultaRepo = consultaRepo;
        this.citaRepo = citaRepo;
        this.expedienteRepo = expedienteRepo;
    }
    
    public List<Consulta> listar() { 
        return consultaRepo.findAll(); }
    @Transactional
    public Consulta registrarConsulta(Consulta c) {
        Cita cita = citaRepo.findById(c.getCita().getIdCita()).orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        if (!cita.getConfirmada()) throw new BusinessException("La cita no está confirmada/completada");
        ExpedienteMedico expediente = expedienteRepo.findByPaciente(cita.getPaciente()).orElseThrow(() -> new ResourceNotFoundException("Expediente no encontrado"));
        c.setExpediente(expediente);
        return consultaRepo.save(c);
    }
    public List<Consulta> listarPorExpediente(Integer idExp) {
        ExpedienteMedico exp = expedienteRepo.findById(idExp).orElseThrow(() -> new ResourceNotFoundException("Expediente no encontrado"));
        return consultaRepo.findByExpediente(exp);
    }
}
