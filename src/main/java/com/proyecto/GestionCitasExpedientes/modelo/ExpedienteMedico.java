package com.proyecto.GestionCitasExpedientes.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expediente_medico")
public class ExpedienteMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExpediente;
    private LocalDate fechaCreacion;
    @Column(columnDefinition = "TEXT")
    private String antecedente;

    @OneToOne
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;
    
    @OneToMany(mappedBy = "expediente", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>();
    
    //Getters y Setters

    public Integer getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getAntecedente() {
        return antecedente;
    }

    public void setAntecedente(String antecedente) {
        this.antecedente = antecedente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
    
}
