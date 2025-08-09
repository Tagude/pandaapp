package com.panda.pandaapp.model;

import jakarta.persistence.*;


/**
 * Entidad que representa un producto en el sistema.
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;
    
    @Column(nullable = false)
    private String nombre_producto;
    
    private int costo;
   
    private int precio_venta;
    
    private int stock;

    public Producto() {
    }

    public Producto(Long id, String nombre_producto, int costo, int precio_venta, int stock) {
        this.id = id;
        this.nombre_producto = nombre_producto;
        this.costo = costo;
        this.precio_venta = precio_venta;
        this.stock = stock;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre_producto() {
        return this.nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getCosto() {
        return this.costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public int getPrecio_venta() {
        return this.precio_venta;
    }

    public void setPrecio_venta(int precio_venta) {
        this.precio_venta = precio_venta;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
       
}