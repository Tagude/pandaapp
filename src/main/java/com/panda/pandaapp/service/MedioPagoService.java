package com.panda.pandaapp.service;

import com.panda.pandaapp.model.MedioPago;
import com.panda.pandaapp.repository.MedioPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//Elimina esta linea
//import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que contiene la lógica de negocio para operaciones con medios de pago.
 * Implementa la capa de servicio entre el controlador y el repositorio.
 */
@Service
public class MedioPagoService {

    private final MedioPagoRepository MedioPagoRepository;

    @Autowired
    public MedioPagoService(MedioPagoRepository MedioPagoRepository) {
        this.MedioPagoRepository = MedioPagoRepository;
    }

    /**
     * Obtiene todos los medios de pago del sistema.
     * @return Lista de todos los medios de pago
     */
    public List<MedioPago> obtenerTodosLosMedioPago() {
        return MedioPagoRepository.findAll();
    }


    /**
     * Obtiene un medio de pago por su ID.
     * @param id ID del medio de pago
     * @return Optional con el medio de pago encontrado o vacío si no existe
     */
    public MedioPago obtenerMedioPagoPorId(Long id) {
        return MedioPagoRepository.findById(id).orElse(null);
    }

   
/**
     * Guarda un nuevo medio de pago.
     * @param tipoMedioPago medio de pago a guardar
     * @return MedioPago guardado
     */
    public MedioPago guardarMedioPago(MedioPago tipoMedioPago) {
        return MedioPagoRepository.save(tipoMedioPago);
    }

    /**
     * Actualiza un medio de pago existente.
     * @param id ID del medio de pago a actualizar
     * @param tipoMedioPagoActualizado MedioPago con los datos actualizados
     * @return MedioPago actualizado o null si no se encuentra el producto
     */
    public MedioPago actualizarMedioPago(Long id, MedioPago tipoMedioPagoActualizado) {
        Optional<MedioPago> tipoMedioPagoExistente = MedioPagoRepository.findById(id);
        
        if (tipoMedioPagoExistente.isPresent()) {
            MedioPago tipoMedioPago = tipoMedioPagoExistente.get();
            tipoMedioPago.setNombre_MedioPago(tipoMedioPagoActualizado.getNombre_MedioPago());
        
            
            return MedioPagoRepository.save(tipoMedioPago);
        } else {
            return null;
        }
    }

   /**
     * Elimina un medio de pago por su ID.
     * @param id ID del medio de pago a eliminar
     * @return true si el medio de pago fue eliminado, false si no se encontró
     */
    public boolean eliminarMedioPago(Long id) {
        if (MedioPagoRepository.existsById(id)) {
            MedioPagoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}