package com.panda.pandaapp.repository;

import com.panda.pandaapp.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByProductoId(Long idProducto);

    List<Venta> findByMedioPagoId(Long idMedioPago);

    List<Venta> findByFecha(LocalDate fecha);

    List<Venta> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Venta> findByFechaOrderByFechaDesc(LocalDate fecha);

    @Query("SELECT SUM(v.cantidad * v.precioUnitario) " +
           "FROM Venta v " +
           "WHERE v.producto.id = :idProducto " +
           "AND v.fecha BETWEEN :fechaInicio AND :fechaFin")
    Double getTotalVentasByProductoAndFecha(@Param("idProducto") Long idProducto,
                                           @Param("fechaInicio") LocalDate fechaInicio,
                                           @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT v FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin ORDER BY v.fecha DESC")
    List<Venta> getVentasDelDia(@Param("inicio") LocalDateTime inicio,
                                @Param("fin") LocalDateTime fin);

    @Query("SELECT COALESCE(SUM(v.cantidad), 0) " +
           "FROM Venta v " +
           "WHERE v.producto.id = :idProducto")
    Integer getTotalCantidadVendidaByProducto(@Param("idProducto") Long idProducto);
}