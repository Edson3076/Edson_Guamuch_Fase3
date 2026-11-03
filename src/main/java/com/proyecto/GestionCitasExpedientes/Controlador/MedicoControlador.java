package com.proyecto.GestionCitasExpedientes.Controlador;

import com.proyecto.GestionCitasExpedientes.Servicio.MedicoServicio;
import com.proyecto.GestionCitasExpedientes.modelo.Medico;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoControlador {
    private final MedicoServicio servicio;
    public MedicoControlador(MedicoServicio servicio) { this.servicio = servicio; } 
    
    @GetMapping 
    public ResponseEntity<List<Medico>> listar() { 
        return ResponseEntity.ok(servicio.listar()); }
    
    @GetMapping("/{id}") 
    public ResponseEntity<Medico> obtener(@PathVariable Integer id) { 
        return ResponseEntity.ok(servicio.buscarPorId(id)); }
    
    @PostMapping 
    public ResponseEntity<Medico> crear(@RequestBody Medico m) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.registrar(m)); }
    
    @PutMapping("/{id}") 
    public ResponseEntity<Medico> actualizar(@PathVariable Integer id, @RequestBody Medico m) { 
        return ResponseEntity.ok(servicio.actualizar(id, m)); }
    
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) { servicio.eliminar(id); 
    return ResponseEntity.noContent().build(); }
}
