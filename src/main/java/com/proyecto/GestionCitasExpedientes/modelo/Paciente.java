package com.proyecto.GestionCitasExpedientes.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.*;

@Entity
@Table(name = "paciente")
public class Paciente extends Persona{
    private String tipoSangre;
    private String genero;
    private Integer edad;
    @Column(columnDefinition = "TEXT")
    private String historial;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExpedienteMedico expediente;
    
    // Métodos

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getHistorial() {
        return historial;
    }

    public void setHistorial(String historial) {
        this.historial = historial;
    }

    public ExpedienteMedico getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedienteMedico expediente) {
        this.expediente = expediente;
    }
    
    
}
