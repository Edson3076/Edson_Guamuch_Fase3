package com.proyecto.GestionCitasExpedientes.Servicio;

import com.proyecto.GestionCitasExpedientes.modelo.Consulta;
import com.proyecto.GestionCitasExpedientes.modelo.ExpedienteMedico;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import com.proyecto.GestionCitasExpedientes.exception.ResourceNotFoundException;
import com.proyecto.GestionCitasExpedientes.exception.BusinessException;
import com.proyecto.GestionCitasExpedientes.Repositorio.ConsultaRepositorio;
import com.proyecto.GestionCitasExpedientes.Repositorio.PacienteRepositorio;
import com.proyecto.GestionCitasExpedientes.Repositorio.ExpedienteRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PacienteServicio {
    private final PacienteRepositorio pacienteRepo;
    private final ExpedienteRepositorio expedienteRepo;
    private final ConsultaRepositorio consultaRepo;

    public PacienteServicio(PacienteRepositorio pacienteRepo, ExpedienteRepositorio expedienteRepo, ConsultaRepositorio consultaRepo) {
        this.pacienteRepo = pacienteRepo;
        this.expedienteRepo = expedienteRepo;
        this.consultaRepo = consultaRepo;
    }
    
    public List<Paciente> listar() { 
        return pacienteRepo.findAll(); }

    public Optional<Paciente> buscarPorId(Integer id) { 
        return pacienteRepo.findById(id); }
    
    @Transactional
    public Paciente registrar(Paciente p) {
        if (p.getNombre() == null || p.getNombre().isBlank()) throw new IllegalArgumentException("Nombre requerido");
        if (p.getEmail() == null || p.getEmail().isBlank()) throw new IllegalArgumentException("Email requerido");
        if (pacienteRepo.existsByEmail(p.getEmail())) throw new BusinessException("Email ya registrado");
        ExpedienteMedico exp = new ExpedienteMedico();
        exp.setFechaCreacion(LocalDate.now());
        exp.setAntecedente(p.getHistorial());
        exp.setPaciente(p);
        p.setExpediente(exp);
        Paciente creado = pacienteRepo.save(p);
        expedienteRepo.save(exp);
        return creado;
    }
    @Transactional
    public Paciente actualizar(Integer id, Paciente datos) {
        Paciente ex = pacienteRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));
        ex.setNombre(datos.getNombre());
        ex.setDireccion(datos.getDireccion());
        ex.setTelefono(datos.getTelefono());
        ex.setEdad(datos.getEdad());
        return pacienteRepo.save(ex);
    }
    @Transactional
    public void eliminar(Integer id) {
        Paciente p = pacienteRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));
        expedienteRepo.findByPaciente(p).ifPresent(exp -> {
            List<Consulta> consultas = consultaRepo.findByExpediente(exp);
            if (!consultas.isEmpty()) throw new BusinessException("No se puede eliminar paciente con consultas registradas");
        });
        pacienteRepo.deleteById(id);
    }
}
