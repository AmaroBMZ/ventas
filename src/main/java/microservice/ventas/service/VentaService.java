package microservice.ventas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.ventas.model.Venta;
import microservice.ventas.repository.VentaRepository;


@Service
@Transactional
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;


        public Venta crearVenta(Venta venta){
        return ventaRepository.save(venta);
    }

    public List<Venta> obtenerVenta() {
        return ventaRepository.findAll();
    }
        public Venta obtenerventaPorId(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public Venta updateVenta(Long id, Venta venta) {
        Venta ventaExistente = ventaRepository.findById(id).orElse(null);
        if (ventaExistente != null) {
            ventaExistente.setFechaVenta(venta.getFechaVenta());
            ventaExistente.setTotalVenta(venta.getTotalVenta());
            ventaExistente.setDescuentoVenta(venta.getDescuentoVenta());
            ventaExistente.setEstadoVenta(venta.getEstadoVenta());
        }
        return ventaRepository.save(ventaExistente);
    }


    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }
}
