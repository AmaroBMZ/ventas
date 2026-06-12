package microservice.ventas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.ventas.client.BodegaClient;
import microservice.ventas.client.InventarioClient;
import microservice.ventas.dto.DescuentoStockRequest;
import microservice.ventas.model.Venta;
import microservice.ventas.repository.VentaRepository;


@Service
@Transactional
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private InventarioClient inventarioClient;

    @Autowired
    private BodegaClient bodegaClient;

    public Venta crearVenta(Venta venta) {
        descontarStockEnMicroservicios(venta);
        return ventaRepository.save(venta);
    }

    public List<Venta> obtenerVenta() {
        return ventaRepository.findAll();
    }
        public Venta obtenerventaPorId(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public Venta updateVenta(Long id, Venta venta) {
        Venta ventaExistente = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        ventaExistente.setFechaVenta(venta.getFechaVenta());
        ventaExistente.setTotalVenta(venta.getTotalVenta());
        ventaExistente.setDescuentoVenta(venta.getDescuentoVenta());
        ventaExistente.setEstadoVenta(venta.getEstadoVenta());
        ventaExistente.setIdPerfume(venta.getIdPerfume());
        ventaExistente.setIdSucursal(venta.getIdSucursal());
        ventaExistente.setCantidad(venta.getCantidad());

        return ventaRepository.save(ventaExistente);
    }


    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }

    public Venta obtenerVentaPorId(Long id) {

    return ventaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
}

    private void descontarStockEnMicroservicios(Venta venta) {
        DescuentoStockRequest request = new DescuentoStockRequest(
                venta.getIdPerfume(),
                venta.getIdSucursal(),
                venta.getCantidad(),
                "VENTA");

        inventarioClient.descontarStock(request);
        bodegaClient.descontarStock(request);
    }

}
