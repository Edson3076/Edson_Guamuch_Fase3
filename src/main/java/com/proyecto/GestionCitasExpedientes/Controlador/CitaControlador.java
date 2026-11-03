package com.proyecto.GestionCitasExpedientes.Controlador;

import com.proyecto.GestionCitasExpedientes.Servicio.CitaServicio;
import com.proyecto.GestionCitasExpedientes.modelo.Cita;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/citas")
public class CitaControlador {
    private final CitaServicio citaServicio;

    public CitaControlador(CitaServicio citaServicio) {
        this.citaServicio = citaServicio;
    }
    
    @GetMapping 
    public ResponseEntity<List<Cita>> listar() { 
        return ResponseEntity.ok(citaServicio.listar()); }
    @GetMapping("/{id}") 
    public ResponseEntity<Cita> obtener(@PathVariable Integer id) { 
        return ResponseEntity.ok(citaServicio.buscarPorId(id)); }
    @PostMapping public ResponseEntity<?> crear(@RequestBody Cita c) {
        Cita creado = citaServicio.programarCita(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Integer id, @RequestParam String motivo, @RequestParam String quien) {
        return ResponseEntity.ok(citaServicio.cancelarCita(id, motivo, quien));
    }
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmar(@PathVariable Integer id) {
        return ResponseEntity.ok(citaServicio.confirmarCita(id));
    }
    @GetMapping("/medico/{id}/fecha/{fecha}")
    public ResponseEntity<List<Cita>> porMedicoFecha(@PathVariable Integer id, @PathVariable String fecha) {
        return ResponseEntity.ok(citaServicio.listarPorMedicoYFecha(id, LocalDate.parse(fecha)));
    }
}
