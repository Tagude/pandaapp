package com.panda.pandaapp.repository;

import com.panda.pandaapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Extiende JpaRepository para heredar métodos CRUD básicos como save, findAll, findById, etc.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su dirección de email.
     * @param correo el email del usuario a buscar
     * @return un Optional que puede contener el usuario si se encuentra
     */
    Optional<Usuario> findByCorreo(String correo);
    
    /**
     * Verifica si existe un usuario con el email proporcionado.
     * @param correo el email a verificar
     * @return true si existe un usuario con el email, false en caso contrario
     */
    boolean existsByCorreo(String correo);
    
    /**
     * Busca usuarios por nombre y apellido que contengan el texto proporcionado.
     * @param nombre texto a buscar en el campo nombre
     * @return lista de usuarios que coinciden con los criterios
     */
    java.util.List<Usuario> findByNombreContaining(String nombre);

    Usuario findByUsuario(String usuario);
    
}