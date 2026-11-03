package com.proyecto.GestionCitasExpedientes.Servicio;

import com.proyecto.GestionCitasExpedientes.modelo.Medico;
import com.proyecto.GestionCitasExpedientes.modelo.Cita;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import com.proyecto.GestionCitasExpedientes.exception.ResourceNotFoundException;
import com.proyecto.GestionCitasExpedientes.exception.BusinessException;
import com.proyecto.GestionCitasExpedientes.Repositorio.PacienteRepositorio;
import com.proyecto.GestionCitasExpedientes.Repositorio.MedicoRepositorio;
import com.proyecto.GestionCitasExpedientes.Repositorio.CitaRepositorio;
import jakarta.transaction.Transactional;
import java.time.*;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CitaServicio {
    private final CitaRepositorio citaRepo;
    private final MedicoRepositorio medicoRepo;
    private final PacienteRepositorio pacienteRepo;

    public CitaServicio(CitaRepositorio citaRepo, MedicoRepositorio medicoRepo, PacienteRepositorio pacienteRepo) {
        this.citaRepo = citaRepo;
        this.medicoRepo = medicoRepo;
        this.pacienteRepo = pacienteRepo;
    }
    
    public List<Cita> listar() { 
        return citaRepo.findAll(); }
    public List<Cita> listarPorMedicoYFecha(Integer idMedico, LocalDate fecha) {
        Medico m = medicoRepo.findById(idMedico).orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));
        return citaRepo.findByMedicoAndFecha(m, fecha);
    }

    public Cita buscarPorId(Integer id) { 
        return citaRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada")); }
    
    @Transactional
    public Cita programarCita(Cita c) {
        if (c.getFecha() == null || c.getHora() == null) throw new IllegalArgumentException("Fecha y hora requeridas");
        if (c.getFecha().isBefore(LocalDate.now())) throw new IllegalArgumentException("La cita debe ser hoy o futura");
        Medico medico = medicoRepo.findById(c.getMedico().getIdPersona()).orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));
        Paciente paciente = pacienteRepo.findById(c.getPaciente().getIdPersona()).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));
        if (!isDentroHorario(medico.getHorarioAtencion(), c.getHora())) throw new IllegalArgumentException("Hora fuera del horario del médico");
        if (citaRepo.existsByMedicoAndFechaAndHora(medico, c.getFecha(), c.getHora())) throw new BusinessException("Médico ya tiene cita en esa fecha/hora");
        if (citaRepo.existsByPacienteAndMedicoAndFechaAndConfirmadaTrue(paciente, medico, c.getFecha())) throw new BusinessException("Paciente ya tiene cita activa con este médico ese día");
        c.setMedico(medico);
        c.setPaciente(paciente);
        c.setConfirmada(true); // RN07
        if (medico.getEspecialidad() != null) c.setCosto(medico.getEspecialidad().getCostoConsulta()); // RN12
        return citaRepo.save(c);
    }
    
    private boolean isDentroHorario(String horarioAtencion, LocalTime hora) {
        if (horarioAtencion == null || horarioAtencion.isBlank()) return true;
        try {
            String primera = horarioAtencion.split(";")[0];
            String[] parts = primera.split("-");
            LocalTime inicio = LocalTime.parse(parts[0]);
            LocalTime fin = LocalTime.parse(parts[1]);
            return !hora.isBefore(inicio) && !hora.isAfter(fin);
        } catch (Exception e) {
            return true;
        }
    }
    
    @Transactional
    public Cita cancelarCita(Integer id, String motivo, String quien) {
        Cita c = citaRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        if (!c.getConfirmada()) throw new BusinessException("Solo citas confirmadas pueden cancelarse");
        c.setConfirmada(false);
        c.setMotivoCancelacion(motivo + " (cancelado por: " + quien + ")");
        return citaRepo.save(c);
    }
    
    @Transactional
    public Cita confirmarCita(Integer id) {
        Cita c = citaRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        long horas = Duration.between(LocalDateTime.now(), LocalDateTime.of(c.getFecha(), c.getHora())).toHours();
        if (horas < 24) throw new BusinessException("Confirmación requiere al menos 24 horas de anticipación");
        c.setConfirmada(true);
        return citaRepo.save(c);
    }
}
