package com.proyecto.GestionCitasExpedientes.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mensaje) { 
        super(mensaje); }
}
