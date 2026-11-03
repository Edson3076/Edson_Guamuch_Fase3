package com.proyecto.GestionCitasExpedientes.Controlador;

import com.proyecto.GestionCitasExpedientes.Servicio.EspecialidadServicio;
import com.proyecto.GestionCitasExpedientes.modelo.Especialidad;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadControlador {
    private final EspecialidadServicio especialidadServicio;

    public EspecialidadControlador(EspecialidadServicio especialidadServicio) {
        this.especialidadServicio = especialidadServicio;}
    
    @GetMapping 
    public ResponseEntity<List<Especialidad>> listar() { 
        return ResponseEntity.ok(especialidadServicio.listar()); }
    
    @GetMapping("/{id}") 
    public ResponseEntity<Especialidad> obtener(@PathVariable Integer id) { 
        return ResponseEntity.ok(especialidadServicio.buscarPorId(id)); }
    
    @PostMapping 
    public ResponseEntity<Especialidad> crear(@RequestBody Especialidad e) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadServicio.crear(e)); }
    
    @PutMapping("/{id}") 
    public ResponseEntity<Especialidad> actualizar(@PathVariable Integer id, @RequestBody Especialidad e) { 
        return ResponseEntity.ok(especialidadServicio.actualizar(id, e)); }
    
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) { especialidadServicio.eliminar(id); 
    return ResponseEntity.noContent().build(); }
    
}
