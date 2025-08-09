package com.panda.pandaapp.controller;

import com.panda.pandaapp.model.MedioPago;
import com.panda.pandaapp.service.MedioPagoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para manejar las operaciones CRUD de productos.
 * Expone endpoints para la gestión de medios de pago en la API.
 */
@RestController
@RequestMapping("/api/MedioPago")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen - ajustar en producción
public class MedioPagoController {

    @Autowired
    MedioPagoService MedioPagoService;

    
    /**
     * Obtiene todos los medios de pago.
     * @param soloActivos Parámetro opcional para filtrar solo medios de pago activos
     * @return Lista de medios de pago
     */
    @SuppressWarnings("null")
    @GetMapping
    public ResponseEntity<List<MedioPago>> obtenerTodos() {
       try {
        List<MedioPago> obtenerMedioPago = MedioPagoService.obtenerTodosLosMedioPago();
        return new ResponseEntity<>(obtenerMedioPago, HttpStatus.OK);
       } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

  /*
    @GetMapping("/{id}")
    public ResponseEntity<MedioPago> obtenerPorId(@PathVariable Long id) {
        return tipoMedioPagoService.obtenerMedioPagoPorId(id)
                .map(tipoMedioPago -> new ResponseEntity<>(tipoMedioPago, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<MedioPago> obtenerMedioPagoPorId(@PathVariable Long id) {
        MedioPago tipoMedioPago = MedioPagoService.obtenerMedioPagoPorId(id);
        if(tipoMedioPago != null){
            return ResponseEntity.ok(tipoMedioPago);
        } else {
            return ResponseEntity.notFound().build();
        }

    }


    /**
     * Crea un nuevo medio de pago.
     * @param tipoMedioPago Datos del nuevo medio de pago
     * @return MedioPago creado
     */
    @PostMapping
    public ResponseEntity<?> crearMedioPago(@RequestBody MedioPago tipoMedioPago) {
        try {
            MedioPago nuevoMedioPago = MedioPagoService.guardarMedioPago(tipoMedioPago);
            return new ResponseEntity<>(nuevoMedioPago, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Actualiza un medio de pago creado.
     * @param id ID del medio de pago a actualizar
     * @param stock Datos del medio de pago actualizado
     * @return Medio de pago actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedioPago> actualizarMedioPago(@PathVariable Long id, @RequestBody MedioPago tipoMedioPago) {
        MedioPago tipoMedioPagoActualizado = MedioPagoService.actualizarMedioPago(id, tipoMedioPago);
        
        if (tipoMedioPagoActualizado != null) {
            return new ResponseEntity<>(tipoMedioPagoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un medio de pago (borrado lógico).
     * @param id ID del medio de pago a eliminar
     * @return Mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedioPago(@PathVariable Long id) {
        boolean eliminado = MedioPagoService.eliminarMedioPago((long) id);
        
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
