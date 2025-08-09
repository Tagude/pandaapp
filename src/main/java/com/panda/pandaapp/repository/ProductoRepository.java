package com.panda.pandaapp.repository;

import com.panda.pandaapp.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder a los datos de los productos.
 * Extiende JpaRepository para tener acceso a métodos CRUD básicos.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // No necesitas redefinir estos métodos, ya están disponibles automáticamente:
    // - Optional<Producto> findById(Long id)
    // - boolean existsById(Long id) 
    // - void deleteById(Long id)
    
    // Solo agrega métodos personalizados si los necesitas, por ejemplo:
    // Optional<Producto> findByNombre_producto(String nombre);
    
}