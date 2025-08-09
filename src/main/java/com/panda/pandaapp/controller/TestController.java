package com.panda.pandaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/test-hash")
    public String testHash() {
        // Crear un hash con Spring Boot para comparar
        String testPassword = "1234";
        String springBootHash = passwordEncoder.encode(testPassword);
        String flaskHash = "$2a$10$r8kyG0D5NMHi0MyBbofwvu2z2FtFLuNQiCeAjJESZjNn7FaU2xUue";
        
        StringBuilder result = new StringBuilder();
        result.append("<h3>Comparación Flask vs Spring Boot</h3>");
        result.append("Contraseña de prueba: ").append(testPassword).append("<br><br>");
        
        result.append("<b>Hash de Flask:</b><br>").append(flaskHash).append("<br>");
        result.append("Verificación con Spring Boot: ").append(passwordEncoder.matches(testPassword, flaskHash) ? "✅" : "❌").append("<br><br>");
        
        result.append("<b>Hash de Spring Boot:</b><br>").append(springBootHash).append("<br>");
        result.append("Verificación con Spring Boot: ").append(passwordEncoder.matches(testPassword, springBootHash) ? "✅" : "❌").append("<br><br>");
        
        result.append("<b>¿Son iguales los hashes?</b> ").append(flaskHash.equals(springBootHash) ? "Sí" : "No").append("<br>");
        
        return result.toString();
    }
}