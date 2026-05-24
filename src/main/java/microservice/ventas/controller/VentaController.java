package microservice.ventas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservice.ventas.model.Venta;
import microservice.ventas.service.VentaService;


@RestController
@RequestMapping("api/v1/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public Venta postVentas(@RequestBody Venta venta) {
        return ventaService.crearVenta(venta);
    }

    @GetMapping
    public List<Venta> getVenta() {
        return ventaService.obtenerVenta();
    }

    @GetMapping("{id}")
    public Venta getVentaById(@PathVariable Long id) {
        return ventaService.obtenerVentaPorId(id);
    }

    @PutMapping("{id}")
    public Venta putVenta(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.updateVenta(id, venta);
    }

    @DeleteMapping("{id}")
    public void eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
    }
}