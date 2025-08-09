package com.panda.pandaapp.service;

import com.panda.pandaapp.model.Producto;
import com.panda.pandaapp.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//Elimina esta linea
//import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que contiene la lógica de negocio para operaciones con productos.
 * Implementa la capa de servicio entre el controlador y el repositorio.
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene todos los productos del sistema.
     * @return Lista de todos los productos
     */
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }


    /**
     * Obtiene un producto por su ID.
     * @param id ID del producto
     * @return Optional con el producto encontrado o vacío si no existe
     */
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

   
/**
     * Guarda un nuevo producto.
     * @param producto Producto a guardar
     * @return Producto guardado
     */
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Actualiza un producto existente.
     * @param id ID del producto a actualizar
     * @param productoActualizado Producto con los datos actualizados
     * @return Producto actualizado o null si no se encuentra el producto
     */
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            producto.setNombre_producto(productoActualizado.getNombre_producto());
            producto.setCosto(productoActualizado.getCosto());
            producto.setPrecio_venta(productoActualizado.getPrecio_venta());
            producto.setStock(productoActualizado.getStock());
            
            return productoRepository.save(producto);
        } else {
            return null;
        }
    }

   /**
     * Elimina un producto por su ID.
     * @param id ID del producto a eliminar
     * @return true si el producto fue eliminado, false si no se encontró
     */
    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}