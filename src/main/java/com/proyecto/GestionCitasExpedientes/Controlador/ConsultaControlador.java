package com.proyecto.GestionCitasExpedientes.Controlador;

import com.proyecto.GestionCitasExpedientes.Servicio.ConsultaServicio;
import com.proyecto.GestionCitasExpedientes.modelo.Consulta;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaControlador {
    private final ConsultaServicio consultaServicio;

    public ConsultaControlador(ConsultaServicio consultaServicio) {
        this.consultaServicio = consultaServicio;
    }
    
    @GetMapping 
    public ResponseEntity<List<Consulta>> listar() { 
        return ResponseEntity.ok(consultaServicio.listar()); }  
    @PostMapping public ResponseEntity<?> crear(@RequestBody Consulta c) {
        Consulta creado = consultaServicio.registrarConsulta(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    @GetMapping("/expediente/{id}") 
    public ResponseEntity<List<Consulta>> porExpediente(@PathVariable Integer id) {
        return ResponseEntity.ok(consultaServicio.listarPorExpediente(id));
    }
    
}
