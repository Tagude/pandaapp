package com.panda.pandaapp.controller;

import com.panda.pandaapp.model.MedioPago;
import com.panda.pandaapp.model.Producto;
import com.panda.pandaapp.model.Venta;
import com.panda.pandaapp.repository.MedioPagoRepository;
import com.panda.pandaapp.repository.ProductoRepository;
import com.panda.pandaapp.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

/**
 * Controller REST para manejar las operaciones de Venta.
 * Proporciona endpoints para CRUD y consultas específicas de ventas.
 */
@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
@Validated
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MedioPagoRepository medioPagoRepository;

    /**
     * Obtiene todas las ventas
     */
    @GetMapping
    @NonNull
    public ResponseEntity<List<Venta>> getAllVentas() {
        try {
            List<Venta> ventas = ventaRepository.findAll();
            if (ventas.isEmpty()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            }
            return new ResponseEntity<>(ventas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene una venta por ID
     */
    @GetMapping("/{id}")
    @NonNull
    public ResponseEntity<Venta> getVentaById(@PathVariable("id") @NotNull Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Optional<Venta> ventaData = ventaRepository.findById(id);
        
        if (ventaData.isPresent()) {
            return new ResponseEntity<>(ventaData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea una nueva venta
     */
    @PostMapping
    @NonNull
    public ResponseEntity<Venta> createVenta(@Valid @RequestBody Venta venta) {
    try {
        // Validar producto
        Optional<Producto> productoOpt = productoRepository.findById(venta.getProducto().getId());
        if (productoOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Producto no existe
        }

        // Validar medio de pago
        Optional<MedioPago> medioPagoOpt = medioPagoRepository.findById(venta.getMedioPago().getId());
        if (medioPagoOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Medio de pago no existe
        }

        Producto producto = productoOpt.get();
        MedioPago medioPago = medioPagoOpt.get();

        // Validar stock disponible
        if (producto.getStock() < venta.getCantidad()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // No hay suficiente stock
        }

        // Descontar stock
        producto.setStock(producto.getStock() - venta.getCantidad());
        productoRepository.save(producto);

        // Asociar objetos completos
        venta.setProducto(producto);
        venta.setMedioPago(medioPago);

        // Si no se especifica fecha, usar la actual
        if (venta.getFecha() == null) {
            venta.setFecha(LocalDate.now());
        }

        Venta nuevaVenta = ventaRepository.save(venta);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);

    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    /**
     * Actualiza una venta existente
     */
    @PutMapping("/{id}")
    @NonNull
    public ResponseEntity<Venta> updateVenta(@PathVariable("id") @NotNull Long id, 
                                           @Valid @RequestBody @NotNull Venta venta) {
        if (id == null || id <= 0 || venta == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Optional<Venta> ventaData = ventaRepository.findById(id);
        
        if (ventaData.isPresent()) {
            Venta ventaActual = ventaData.get();
            ventaActual.setProducto(venta.getProducto());
            ventaActual.setCantidad(venta.getCantidad());
            ventaActual.setPrecioUnitario(venta.getPrecioUnitario());
            ventaActual.setMedioPago(venta.getMedioPago());
            ventaActual.setFecha(venta.getFecha());
            
            return new ResponseEntity<>(ventaRepository.save(ventaActual), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina una venta
     */
    @DeleteMapping("/{id}")
    @NonNull
    public ResponseEntity<HttpStatus> deleteVenta(@PathVariable("id") @NotNull Long id) {
        try {
            if (id == null || id <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            if (!ventaRepository.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            ventaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene ventas por producto
     */
    @GetMapping("/producto/{idProducto}")
    @NonNull
    public ResponseEntity<List<Venta>> getVentasByProducto(@PathVariable("idProducto") @NotNull Long idProducto) {
        try {
            if (idProducto == null || idProducto <= 0) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            
            List<Venta> ventas = ventaRepository.findByProductoId(idProducto);
            return new ResponseEntity<>(ventas != null ? ventas : Collections.emptyList(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene ventas por medio de pago
     */
    @GetMapping("/medio-pago/{idMedioPago}")
    @NonNull
    public ResponseEntity<List<Venta>> getVentasByMedioPago(@PathVariable("idMedioPago") @NotNull Long idMedioPago) {
        try {
            if (idMedioPago == null || idMedioPago <= 0) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            
            List<Venta> ventas = ventaRepository.findByMedioPagoId(idMedioPago);
            return new ResponseEntity<>(ventas != null ? ventas : Collections.emptyList(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene ventas por fecha específica
     */
    @GetMapping("/fecha/{fecha}")
    @NonNull
    public ResponseEntity<List<Venta>> getVentasByFecha(
            @PathVariable("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate fecha) {
        try {
            if (fecha == null) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            
            List<Venta> ventas = ventaRepository.findByFecha(fecha);
            return new ResponseEntity<>(ventas != null ? ventas : Collections.emptyList(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene ventas entre un rango de fechas
     */
    @GetMapping("/rango-fechas")
    @NonNull
    public ResponseEntity<List<Venta>> getVentasByRangoFechas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate fechaFin) {
        try {
            if (fechaInicio == null || fechaFin == null) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            
            if (fechaInicio.isAfter(fechaFin)) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            
            List<Venta> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
            return new ResponseEntity<>(ventas != null ? ventas : Collections.emptyList(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene las ventas del día actual
     */
    @GetMapping("/hoy")
    @NonNull
    public ResponseEntity<List<Venta>> getVentasDelDia() {
        try {
            LocalDate hoy = LocalDate.now(ZoneId.of("America/Bogota")); // Cambia la zona si es distinta
            LocalDateTime inicio = hoy.atStartOfDay();
            LocalDateTime fin = hoy.atTime(LocalTime.MAX);

            List<Venta> ventas = ventaRepository.getVentasDelDia(inicio, fin);
            return new ResponseEntity<>(ventas != null ? ventas : Collections.emptyList(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene el total de ventas por producto en un rango de fechas
     */
    @GetMapping("/total-producto/{idProducto}")
    @NonNull
    public ResponseEntity<Double> getTotalVentasByProducto(
            @PathVariable("idProducto") @NotNull Long idProducto,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate fechaFin) {
        try {
            if (idProducto == null || idProducto <= 0 || fechaInicio == null || fechaFin == null) {
                return new ResponseEntity<>(0.0, HttpStatus.BAD_REQUEST);
            }
            
            if (fechaInicio.isAfter(fechaFin)) {
                return new ResponseEntity<>(0.0, HttpStatus.BAD_REQUEST);
            }
            
            Double total = ventaRepository.getTotalVentasByProductoAndFecha(idProducto, fechaInicio, fechaFin);
            return new ResponseEntity<>(total != null ? total : 0.0, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0.0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene la cantidad total vendida de un producto
     */
    @GetMapping("/cantidad-producto/{idProducto}")
    @NonNull
    public ResponseEntity<Integer> getCantidadVendidaByProducto(@PathVariable("idProducto") @NotNull Long idProducto) {
        try {
            if (idProducto == null || idProducto <= 0) {
                return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
            }
            
            Integer cantidad = ventaRepository.getTotalCantidadVendidaByProducto(idProducto);
            return new ResponseEntity<>(cantidad != null ? cantidad : 0, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}