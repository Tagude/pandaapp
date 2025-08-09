package com.panda.pandaapp.service;

import com.panda.pandaapp.model.Usuario;
import com.panda.pandaapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que contiene la lógica de negocio para operaciones con usuarios.
 * Implementa la capa de servicio entre el controlador y el repositorio.
 */
@Service
public class UsuarioService {

    // Inyección del repositorio de usuarios
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Obtiene todos los usuarios del sistema.
     * @return Lista de todos los usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su ID.
     * @param id_usuario ID del usuario a buscar
     * @return Optional que puede contener el usuario si se encuentra
     */
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su email.
     * @param correo Email del usuario a buscar
     * @return Optional que puede contener el usuario si se encuentra
     */
    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    /**
     * Guarda un nuevo usuario en la base de datos.
     * @param usuario Usuario a guardar
     * @return El usuario guardado
     * @throws IllegalArgumentException si ya existe un usuario con el mismo email
     */
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        // Verificar si ya existe un usuario con el mismo email
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getCorreo());
        }
        
        // Aquí se podría agregar lógica para encriptar la contraseña antes de guardar
        String passwordEncriptada = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(passwordEncriptada);
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente.
     * @param id_usuario ID del usuario a actualizar
     * @param usuarioActualizado Datos actualizados del usuario
     * @return El usuario actualizado
     * @throws IllegalArgumentException si el usuario no existe
     */
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
            .map(usuarioExistente -> {
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
              
                // Si se proporciona un nuevo email, verificar que no exista ya
                if (!usuarioExistente.getCorreo().equals(usuarioActualizado.getCorreo())) {
                    if (usuarioRepository.existsByCorreo(usuarioActualizado.getCorreo())) {
                        throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuarioActualizado.getCorreo());
                    }
                    usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
                }
                
                // Si se proporciona una nueva contraseña, actualizarla
                // Aquí se podría agregar lógica para encriptar la contraseña
                if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isEmpty()) {
                    String nuevaPasswordEncriptada = passwordEncoder.encode(usuarioActualizado.getContrasena());
                    usuarioExistente.setContrasena(nuevaPasswordEncriptada); // ✅ Usas el hash encriptado
                }
                
                return usuarioRepository.save(usuarioExistente);
            })
            .orElseThrow(() -> new IllegalArgumentException("No se encontró usuario con ID: " + id));
    }

    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario a eliminar
     * @throws IllegalArgumentException si el usuario no existe
     */
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró usuario con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Busca usuarios por nombre.
     * @param texto Texto a buscar en nombre
     * @return Lista de usuarios que coinciden con la búsqueda
     */
    public List<Usuario> buscarUsuariosPorNombreOApellido(String texto) {
        return usuarioRepository.findByNombreContaining(texto);
    }

    /**
     * Verifica si la contraseña proporcionada coincide con la almacenada.
     * @param contrasena Proporcionada por el usuario
     * @param contrasenaAlmacenada Almacenada en la base de datos
     * @return true si coinciden, false en caso contrario
     */
    public Usuario obtenerPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }

    public boolean verificarContrasena(String contrasenaPlana, String contrasenaHasheada) {
        return passwordEncoder.matches(contrasenaPlana, contrasenaHasheada);
    }
}