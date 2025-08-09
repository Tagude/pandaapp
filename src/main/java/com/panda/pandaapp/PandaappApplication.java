package com.panda.pandaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Clase principal que inicia la aplicación Spring Boot.
 * Contiene el método main que inicia el servidor y configura el contexto de Spring.
 */
@SpringBootApplication
@RestController
public class PandaappApplication {

    /**
     * Método principal que inicia la aplicación.
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(PandaappApplication.class, args);
    }
    
    /**
     * Endpoint simple para verificar que la aplicación está funcionando.
     * @return Mensaje de bienvenida
     */
    @GetMapping("/")
    public String home() {
        return "Bienvenido a la API de Panda - Sistema de Facturación e Inventario";
    }
}