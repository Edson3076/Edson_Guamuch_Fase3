package com.proyecto.GestionCitasExpedientes.Controlador;

import com.proyecto.GestionCitasExpedientes.Servicio.PacienteServicio;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteControlador {
    private final PacienteServicio servicio;
    public PacienteControlador(PacienteServicio servicio) { 
        this.servicio = servicio; }
    
    @GetMapping 
    public ResponseEntity<List<Paciente>> listar() { 
        return ResponseEntity.ok(servicio.listar()); }

    @GetMapping("/{id}") 
    public ResponseEntity<?> obtener(@PathVariable Integer id) {
        return servicio.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping 
    public ResponseEntity<?> crear(@RequestBody Paciente p) {
        Paciente creado = servicio.registrar(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    @PutMapping("/{id}") 
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Paciente p) {
        return ResponseEntity.ok(servicio.actualizar(id, p));
    }
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
