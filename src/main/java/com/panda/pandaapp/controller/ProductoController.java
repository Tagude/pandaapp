package com.panda.pandaapp.controller;

import com.panda.pandaapp.model.Producto;
import com.panda.pandaapp.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para manejar las operaciones CRUD de productos.
 * Expone endpoints para la gestión de productos en la API.
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen - ajustar en producción
public class ProductoController {

    @Autowired
    ProductoService productoService;

    
    /**
     * Obtiene todos los productos.
     * @param soloActivos Parámetro opcional para filtrar solo productos activos
     * @return Lista de productos
     */
    @SuppressWarnings("null")
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
       try {
        List<Producto> obtenerProductos=productoService.obtenerTodosLosProductos();
        return new ResponseEntity<>(obtenerProductos, HttpStatus.OK);
       } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

  /*
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(producto -> new ResponseEntity<>(producto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if(producto != null){
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }

    }


    /**
     * Crea un nuevo producto.
     * @param producto Datos del nuevo producto
     * @return Producto creado
     */
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.guardarProducto(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Actualiza solo el stock de un producto.
     * @param id ID del producto
     * @param stock Datos del stock a actualizar
     * @return Producto con stock actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProductos(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        
        if (productoActualizado != null) {
            return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un producto (borrado lógico).
     * @param id ID del producto a eliminar
     * @return Mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        boolean eliminado = productoService.eliminarProducto((long) id);
        
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
