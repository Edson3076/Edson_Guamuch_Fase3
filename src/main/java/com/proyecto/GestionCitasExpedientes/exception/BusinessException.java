package com.proyecto.GestionCitasExpedientes.exception;

public class BusinessException extends RuntimeException{
    public BusinessException (String mensaje) { super(mensaje); }
}
