package com.panda.pandaapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación.
 * Captura excepciones específicas y proporciona respuestas de error consistentes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones IllegalArgumentException lanzadas en la aplicación.
     * @param ex La excepción capturada
     * @param request La solicitud web que generó la excepción
     * @return Respuesta con mensaje de error y estado HTTP 400 (Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> manejarIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", new Date());
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("descripcion", request.getDescription(false));
        
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Maneja excepciones genéricas no capturadas por otros manejadores.
     * @param ex La excepción capturada
     * @param request La solicitud web que generó la excepción
     * @return Respuesta con mensaje de error y estado HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarExcepcionesGlobales(Exception ex, WebRequest request) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", new Date());
        respuesta.put("mensaje", "Ocurrió un error inesperado");
        respuesta.put("descripcion", request.getDescription(false));
        respuesta.put("error", ex.getMessage());
        
        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}