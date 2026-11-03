package com.proyecto.GestionCitasExpedientes.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consulta")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConsulta;
    @Column(columnDefinition = "TEXT")
    private String sintomas;
    @Column(columnDefinition = "TEXT")
    private String diagnostico;
    @Column(columnDefinition = "TEXT")
    private String tratamiento;
    private LocalDateTime fechaConsulta = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "expediente_id")
    private ExpedienteMedico expediente;

    //Getters y Setters

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public ExpedienteMedico getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedienteMedico expediente) {
        this.expediente = expediente;
    }
    
}
