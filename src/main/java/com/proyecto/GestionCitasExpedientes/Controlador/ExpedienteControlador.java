package com.proyecto.GestionCitasExpedientes.Controlador;

import com.proyecto.GestionCitasExpedientes.modelo.Consulta;
import com.proyecto.GestionCitasExpedientes.modelo.ExpedienteMedico;
import com.proyecto.GestionCitasExpedientes.exception.ResourceNotFoundException;
import com.proyecto.GestionCitasExpedientes.Servicio.ExpedienteServicio;
import com.proyecto.GestionCitasExpedientes.Servicio.ConsultaServicio;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/expedientes")
public class ExpedienteControlador {
    private final ExpedienteServicio expedienteServicio;
    private final ConsultaServicio consultaServicio;

    public ExpedienteControlador(ExpedienteServicio expedienteServicio, ConsultaServicio consultaServicio) {
        this.expedienteServicio = expedienteServicio;
        this.consultaServicio = consultaServicio;
    }
    
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<ExpedienteMedico> obtenerPorPaciente(@PathVariable Integer idPaciente) {
        return expedienteServicio.buscarPorPaciente(idPaciente)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Expediente no encontrado para paciente " + idPaciente));
    }
    @PostMapping
    public ResponseEntity<ExpedienteMedico> crear(@Valid @RequestBody ExpedienteMedico expediente) {
        ExpedienteMedico creado = expedienteServicio.crearParaPaciente(expediente.getPaciente());
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    /* Para no permitir modificar el historial medico
    @PutMapping("/{id}")
    public ResponseEntity<ExpedienteMedico> actualizar(@PathVariable Integer id, @Valid @RequestBody ExpedienteMedico datos) {
        ExpedienteMedico exp = expedienteServicio.buscarPorPaciente(datos.getPaciente().getIdPersona())
            .orElseThrow(() -> new ResourceNotFoundException("Expediente no encontrado"));
        exp.setAntecedente(datos.getAntecedente());
        // no sobrescribir historial de consultas; solo actualizar campos permitidos
        ExpedienteMedico actualizado = expedienteServicio.getExpedienteRepo().save(exp); // o crear método en servicio
        return ResponseEntity.ok(actualizado);
    }*/
    @GetMapping("/{id}/consultas")
        public ResponseEntity<List<Consulta>> listarConsultas(@PathVariable Integer id) {
        List<Consulta> consultas = consultaServicio.listarPorExpediente(id);
        return ResponseEntity.ok(consultas);
    }
}
