package com.panda.pandaapp.repository;

import com.panda.pandaapp.model.MedioPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder a los datos de los medios de pago.
 * Extiende JpaRepository para tener acceso a métodos CRUD básicos.
 */
@Repository
public interface MedioPagoRepository extends JpaRepository<MedioPago, Long> {
    
    // No necesitas redefinir estos métodos, ya están disponibles automáticamente:
    // - Optional<MedioPago> findById(Long id)
    // - boolean existsById(Long id) 
    // - void deleteById(Long id)
    
    // Solo agrega métodos personalizados si los necesitas, por ejemplo:
    // Optional<tipoMedioPago> findByNombre_tipoMedioPago(String nombre);
    
}