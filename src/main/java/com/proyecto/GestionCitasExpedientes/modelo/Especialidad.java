package com.proyecto.GestionCitasExpedientes.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "especialidad")
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEspecialidad;
    @Column(nullable = false)
    private String nombre;
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    @Column(nullable = false)    
    private double costoConsulta;

    //Getters y Setters

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCostoConsulta() {
        return costoConsulta;
    }

    public void setCostoConsulta(double costoConsulta) {
        this.costoConsulta = costoConsulta;
    }
    
}
