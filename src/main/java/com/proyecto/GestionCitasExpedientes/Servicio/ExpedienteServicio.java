package com.proyecto.GestionCitasExpedientes.Servicio;

import com.proyecto.GestionCitasExpedientes.modelo.ExpedienteMedico;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import com.proyecto.GestionCitasExpedientes.exception.ResourceNotFoundException;
import com.proyecto.GestionCitasExpedientes.Repositorio.PacienteRepositorio;
import com.proyecto.GestionCitasExpedientes.Repositorio.ExpedienteRepositorio;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ExpedienteServicio {
    private final ExpedienteRepositorio expedienteRepo;
    private final PacienteRepositorio pacienteRepo;

    public ExpedienteServicio(ExpedienteRepositorio expedienteRepo, PacienteRepositorio pacienteRepo) {
        this.expedienteRepo = expedienteRepo;
        this.pacienteRepo = pacienteRepo;
    }
    public Optional<ExpedienteMedico> buscarPorPaciente(Integer idPaciente) {
        Paciente p = pacienteRepo.findById(idPaciente).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));
        return expedienteRepo.findByPaciente(p);
    }
    @Transactional
    public ExpedienteMedico crearParaPaciente(Paciente p) {
        if (p == null || p.getIdPersona() == null) throw new IllegalArgumentException("Paciente inválido");
        ExpedienteMedico exp = new ExpedienteMedico();
        exp.setPaciente(p);
        exp.setFechaCreacion(LocalDate.now());
        return expedienteRepo.save(exp);
    }
}
