package microservice.ventas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.ventas.client.BodegaClient;
import microservice.ventas.client.InventarioClient;
import microservice.ventas.dto.DescuentoStockRequest;
import microservice.ventas.model.Venta;
import microservice.ventas.repository.VentaRepository;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private InventarioClient inventarioClient;

    @Mock
    private BodegaClient bodegaClient;

    @InjectMocks
    private VentaService ventaService;

    @Test
    void crearVentaDescuentaStockYGuardaVenta() {
        Venta venta = ventaValida();
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.crearVenta(venta);

        assertSame(venta, resultado);

        InOrder orden = inOrder(inventarioClient, bodegaClient, ventaRepository);
        orden.verify(inventarioClient).descontarStock(any(DescuentoStockRequest.class));
        orden.verify(bodegaClient).descontarStock(any(DescuentoStockRequest.class));
        orden.verify(ventaRepository).save(venta);
    }

    @Test
    void crearVentaEnviaDatosCorrectosParaDescontarStock() {
        Venta venta = ventaValida();
        when(ventaRepository.save(venta)).thenReturn(venta);
        ArgumentCaptor<DescuentoStockRequest> captor = ArgumentCaptor.forClass(DescuentoStockRequest.class);

        ventaService.crearVenta(venta);

        verify(inventarioClient).descontarStock(captor.capture());
        DescuentoStockRequest request = captor.getValue();
        assertEquals(10L, request.getIdProducto());
        assertEquals(20L, request.getIdSucursal());
        assertEquals(2, request.getCantidad());
        assertEquals("VENTA", request.getMotivo());
    }

    @Test
    void crearVentaNoGuardaSiInventarioFalla() {
        Venta venta = ventaValida();
        org.mockito.Mockito.doThrow(new IllegalStateException("sin stock"))
                .when(inventarioClient)
                .descontarStock(any(DescuentoStockRequest.class));

        assertThrows(IllegalStateException.class, () -> ventaService.crearVenta(venta));

        verify(bodegaClient, never()).descontarStock(any(DescuentoStockRequest.class));
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void crearVentaNoGuardaSiBodegaFalla() {
        Venta venta = ventaValida();
        org.mockito.Mockito.doThrow(new IllegalStateException("sin stock en bodega"))
                .when(bodegaClient)
                .descontarStock(any(DescuentoStockRequest.class));

        assertThrows(IllegalStateException.class, () -> ventaService.crearVenta(venta));

        verify(inventarioClient).descontarStock(any(DescuentoStockRequest.class));
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void obtenerVentaRetornaTodasLasVentas() {
        Venta venta = ventaValida();
        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<Venta> resultado = ventaService.obtenerVenta();

        assertEquals(1, resultado.size());
        assertSame(venta, resultado.get(0));
    }

    @Test
    void obtenerVentaRetornaListaVacia() {
        when(ventaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Venta> resultado = ventaService.obtenerVenta();

        assertEquals(0, resultado.size());
    }

    @Test
    void obtenerventaPorIdRetornaVentaExistente() {
        Venta venta = ventaValida();
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta resultado = ventaService.obtenerventaPorId(1L);

        assertSame(venta, resultado);
    }

    @Test
    void obtenerventaPorIdRetornaNullSiNoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        Venta resultado = ventaService.obtenerventaPorId(99L);

        assertNull(resultado);
    }

    @Test
    void obtenerVentaPorIdRetornaVentaExistente() {
        Venta venta = ventaValida();
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta resultado = ventaService.obtenerVentaPorId(1L);

        assertSame(venta, resultado);
    }

    @Test
    void obtenerVentaPorIdLanzaExcepcionSiNoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ventaService.obtenerVentaPorId(99L));

        assertEquals("Venta no encontrada", exception.getMessage());
    }

    @Test
    void updateVentaActualizaCampos() {
        Venta existente = ventaValida();
        Venta nueva = ventaValida();
        nueva.setEstadoVenta("ANULADA");
        nueva.setCantidad(5L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ventaRepository.save(existente)).thenReturn(existente);

        Venta resultado = ventaService.updateVenta(1L, nueva);

        assertEquals("ANULADA", resultado.getEstadoVenta());
        assertEquals(5, resultado.getCantidad());
        verify(ventaRepository).save(existente);
    }

    @Test
    void updateVentaLanzaExcepcionSiNoExiste() {
        Venta venta = ventaValida();
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ventaService.updateVenta(99L, venta));

        assertEquals("Venta no encontrada", exception.getMessage());
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void eliminarVentaEliminaPorId() {
        ventaService.eliminarVenta(1L);

        verify(ventaRepository).deleteById(1L);
    }

    private Venta ventaValida() {
        Venta venta = new Venta();
        venta.setIdVenta(1L);
        venta.setFechaVenta(LocalDate.of(2026, 6, 12));
        venta.setTotalVenta(59990.0);
        venta.setDescuentoVenta(0.0);
        venta.setEstadoVenta("PAGADA");
        venta.setIdPerfume(10L);
        venta.setIdSucursal(20L);
        venta.setCantidad(2L);
        return venta;
    }
}
