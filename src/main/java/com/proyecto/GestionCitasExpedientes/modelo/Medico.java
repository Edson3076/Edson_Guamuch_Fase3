package com.proyecto.GestionCitasExpedientes.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "medico")
public class Medico extends Persona{
    @Column(nullable = false, unique = true)
    private String colegiado;
    //Formato sugerido: "08:00-12:00;14:00-18:00"
    private String horarioAtencion;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;
    
    //Getters y Setters

    public String getColegiado() {
        return colegiado;
    }

    public void setColegiado(String colegiado) {
        this.colegiado = colegiado;
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
    
}
