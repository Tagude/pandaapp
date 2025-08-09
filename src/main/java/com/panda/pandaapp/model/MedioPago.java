package com.panda.pandaapp.model;

import jakarta.persistence.*;

/**
 * Entidad que representa un medio de pago en el sistema.
 */
@Entity
@Table(name = "MedioPago")
public class MedioPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_MedioPago")
    private Long id;


    @Column(nullable = false)
    private String tipoMedioPago;

    public MedioPago() {
    }

    public MedioPago(Long id, String tipoMedioPago) {
        this.id = id;
        this.tipoMedioPago = tipoMedioPago;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre_MedioPago() {
        return this.tipoMedioPago;
    }

    public void setNombre_MedioPago(String tipoMedioPago) {
        this.tipoMedioPago = tipoMedioPago;
    }
}