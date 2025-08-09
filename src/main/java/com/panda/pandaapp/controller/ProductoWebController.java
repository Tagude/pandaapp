package com.panda.pandaapp.controller;

import com.panda.pandaapp.model.Producto;
import com.panda.pandaapp.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador web para la gestión de productos con vistas Thymeleaf.
 */
@Controller
@RequestMapping("/productos")
public class ProductoWebController {

    private final ProductoService productoService;

    @Autowired
    public ProductoWebController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Muestra la lista de todos los productos.
     */
    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        model.addAttribute("productos", productos);
        return "lista-productos";
    }

    /**
     * Muestra el formulario para crear un nuevo producto.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "nuevo-producto";
    }

    /**
     * Guarda un nuevo producto.
     */
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto, RedirectAttributes redirectAttributes) {
        productoService.guardarProducto(producto);
        redirectAttributes.addFlashAttribute("mensaje", "Producto guardado con éxito");
        return "redirect:/productos";
    }

    /**
     * Muestra el formulario para editar un producto existente.
     */
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id);

        if (producto != null) {
            model.addAttribute("producto", producto);
            return "editar-producto";
        } else {
            return "redirect:/productos";
        }
    }

    /**
     * Actualiza un producto existente.
     */
    @PostMapping("/{id}/actualizar")
    public String actualizarProducto(@PathVariable("id") Long id, @ModelAttribute("producto") Producto producto, 
                                   RedirectAttributes redirectAttributes) {
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        if (productoActualizado != null) {
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado con éxito");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el producto");
        }
        return "redirect:/productos";
    }

    /**
     * Elimina un producto existente.
     */
    @GetMapping("/{id}/eliminar")
    public String eliminarProducto(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean eliminado = productoService.eliminarProducto(id);
        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado con éxito");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el producto");
        }
        return "redirect:/productos";
    }
}