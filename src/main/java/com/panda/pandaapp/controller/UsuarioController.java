package com.panda.pandaapp.controller;

import com.panda.pandaapp.model.Usuario;
import com.panda.pandaapp.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para manejar las operaciones CRUD de usuarios.
 * Expone endpoints para la gestión de usuarios en la API.
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen - ajustar en producción
public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los usuarios.
     * @return Lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id ID del usuario
     * @return Usuario encontrado o error 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Crea un nuevo usuario.
     * @param usuario Datos del nuevo usuario
     * @return Usuario creado
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Actualiza un usuario existente.
     * @param id ID del usuario a actualizar
     * @param usuario Datos actualizados
     * @return Usuario actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario a eliminar
     * @return Respuesta sin contenido (204) si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para el login de usuarios.
     * @param datosLogin Mapa con usuario y contraseña
     * @return Respuesta con mensaje de éxito o error
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String usuario = credenciales.get("usuario");
        String contrasena = credenciales.get("contrasena");
        
        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("Usuario recibido: " + usuario);
        System.out.println("Contraseña recibida: " + contrasena);
        
        // Buscar usuario en la base de datos
        Usuario usuarioEncontrado = usuarioService.obtenerPorUsuario(usuario);
        
        if (usuarioEncontrado == null) {
            System.out.println("ERROR: Usuario no encontrado en la base de datos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario o contraseña incorrectos"));
        }
        
        System.out.println("Usuario encontrado: " + usuarioEncontrado.getUsuario());
        System.out.println("Contraseña en BD: " + usuarioEncontrado.getContrasena());
        
        // Verificar contraseña
        boolean contrasenaCorrecta = usuarioService.verificarContrasena(contrasena, usuarioEncontrado.getContrasena());
        System.out.println("Contraseña correcta: " + contrasenaCorrecta);
        
        if (contrasenaCorrecta) {
            System.out.println("LOGIN EXITOSO");
            return ResponseEntity.ok(Map.of(
                "mensaje", "Login exitoso",
                "usuario", usuarioEncontrado.getUsuario()
            ));
        } else {
            System.out.println("ERROR: Contraseña incorrecta");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario o contraseña incorrectos"));
        }
    }

    /**
     * Busca usuarios por nombre o apellido.
     * @param query Texto a buscar
     * @return Lista de usuarios que coinciden con la búsqueda
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscarPorNombreOApellido(@RequestParam String query) {
        List<Usuario> usuarios = usuarioService.buscarUsuariosPorNombreOApellido(query);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }



    @GetMapping("/test-hash")
    public ResponseEntity<?> testHash() {
        String contrasenaPlana = "123456";
        String hashGenerado = passwordEncoder.encode(contrasenaPlana);
        
        // Verificar si coincide con el hash de Rosa1
        String hashedinson = "$2a$10$r8kyG0D5NMHi0MyBbofwvu2z2FtFLuNQiCeAjJESZjNn7FaU2xUue";
        boolean coincideConRosa1 = passwordEncoder.matches(contrasenaPlana, hashedinson);
        
        return ResponseEntity.ok(Map.of(
            "contrasenaPlana", contrasenaPlana,
            "hashGenerado", hashGenerado,
            "hashRosa1", hashedinson,
            "coincideConRosa1", coincideConRosa1
        ));
    }

        /**
     * Registra un nuevo usuario desde el frontend Flask.
     * @param datosUsuario Mapa con datos del nuevo usuario
     * @return Respuesta con mensaje de éxito o error
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> datosUsuario) {
        try {
            String nombre = datosUsuario.get("nombre");
            String usuario = datosUsuario.get("usuario");
            String correo = datosUsuario.get("correo");
            String contrasena = datosUsuario.get("contrasena");

            if (usuario == null || contrasena == null || correo == null || nombre == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Faltan datos requeridos"));
            }

            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setUsuario(usuario);
            nuevo.setCorreo(correo);
            nuevo.setContrasena(contrasena);  // Se encripta en el servicio

            Usuario guardado = usuarioService.guardarUsuario(nuevo);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Usuario registrado exitosamente",
                "usuario", guardado.getUsuario()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno: " + e.getMessage()));
        }
    }

}