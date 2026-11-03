package com.proyecto.GestionCitasExpedientes.Servicio;

import com.proyecto.GestionCitasExpedientes.exception.ResourceNotFoundException;
import com.proyecto.GestionCitasExpedientes.exception.BusinessException;
import com.proyecto.GestionCitasExpedientes.Repositorio.EspecialidadRepositorio;
import com.proyecto.GestionCitasExpedientes.Repositorio.MedicoRepositorio;
import com.proyecto.GestionCitasExpedientes.modelo.Especialidad;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EspecialidadServicio {
    private final EspecialidadRepositorio especialidadRepo;
    private final MedicoRepositorio medicoRepo;

    public EspecialidadServicio(EspecialidadRepositorio especialidadRepo, MedicoRepositorio medicoRepo) {
        this.especialidadRepo = especialidadRepo;
        this.medicoRepo = medicoRepo;
    }
    
    public List<Especialidad> listar() { 
        return especialidadRepo.findAll(); }

    public Especialidad buscarPorId(Integer id) { 
        return especialidadRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada")); }
    
    @Transactional
    public Especialidad crear(Especialidad e) {
        if (e.getNombre() == null || e.getNombre().isBlank()) throw new IllegalArgumentException("Nombre requerido");
        if (e.getCostoConsulta() < 0) throw new IllegalArgumentException("Costo inválido");
        return especialidadRepo.save(e);
    }
    
    @Transactional
    public Especialidad actualizar(Integer id, Especialidad datos) {
        Especialidad ex = especialidadRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada"));
        ex.setNombre(datos.getNombre());
        ex.setDescripcion(datos.getDescripcion());
        ex.setCostoConsulta(datos.getCostoConsulta());
        return especialidadRepo.save(ex);
    }
    
    @Transactional
    public void eliminar(Integer id) {
        if (medicoRepo.existsByEspecialidad_IdEspecialidad(id)) throw new BusinessException("No se puede eliminar especialidad con médicos asociados");
        especialidadRepo.deleteById(id);
    }
}
