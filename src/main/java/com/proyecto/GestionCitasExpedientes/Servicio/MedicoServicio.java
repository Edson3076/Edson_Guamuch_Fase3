package com.proyecto.GestionCitasExpedientes.Servicio;

import com.proyecto.GestionCitasExpedientes.modelo.Medico;
import com.proyecto.GestionCitasExpedientes.modelo.Especialidad;
import com.proyecto.GestionCitasExpedientes.exception.ResourceNotFoundException;
import com.proyecto.GestionCitasExpedientes.Repositorio.MedicoRepositorio;
import com.proyecto.GestionCitasExpedientes.Repositorio.EspecialidadRepositorio;
import jakarta.transaction.Transactional;
import java.util.List;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class MedicoServicio {
    private final MedicoRepositorio medicoRepositorio;
    private final EspecialidadRepositorio especialidadRepositorio;

    public MedicoServicio(MedicoRepositorio medicoRepositorio, EspecialidadRepositorio especialidadRepositorio) {
        this.medicoRepositorio = medicoRepositorio;
        this.especialidadRepositorio = especialidadRepositorio;
    }
    
    public List<Medico> listar() { return medicoRepositorio.findAll(); }
    
    public Medico buscarPorId(Integer id) { return medicoRepositorio.findById(id).orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado")); }
    
    @Transactional
    public Medico registrar(Medico m) {
        if (m.getColegiado() == null || m.getColegiado().isBlank()) throw new IllegalArgumentException("Número de colegiado requerido");
        if (medicoRepositorio.existsByColegiado(m.getColegiado())) throw new IllegalStateException("Colegiado ya registrado");
        if (m.getEspecialidad() == null || m.getEspecialidad().getIdEspecialidad() == null)
            throw new IllegalArgumentException("Especialidad requerida");
        Especialidad e = especialidadRepositorio.findById(m.getEspecialidad().getIdEspecialidad())
            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        m.setEspecialidad(e);
        return medicoRepositorio.save(m);
    }
    @Transactional      
    public Medico actualizar(Integer id, Medico datos) {
        Medico ex = medicoRepositorio.findById(id).orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));
        ex.setNombre(datos.getNombre());
        ex.setDireccion(datos.getDireccion());
        ex.setTelefono(datos.getTelefono());
        ex.setHorarioAtencion(datos.getHorarioAtencion());
        return medicoRepositorio.save(ex);
    }
    
    @Transactional
    public void eliminar(Integer id) {
        // validar citas activas antes de eliminar
        medicoRepositorio.deleteById(id);
    }
}
