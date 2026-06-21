package microservice.ventas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class DescuentoStockRequestTest {

    @Test
    void creaDtoConDatosDeDescuento() {
        DescuentoStockRequest request = new DescuentoStockRequest(1L, 2L, 3L, "VENTA");

        assertEquals(1L, request.getIdProducto());
        assertEquals(2L, request.getIdSucursal());
        assertEquals(3L, request.getCantidad());
        assertEquals("VENTA", request.getMotivo());
    }

    @Test
    void settersActualizanDatosDelDto() {
        DescuentoStockRequest request = new DescuentoStockRequest();

        request.setIdProducto(10L);
        request.setIdSucursal(20L);
        request.setCantidad(5L);
        request.setMotivo("AJUSTE");

        assertEquals(10L, request.getIdProducto());
        assertEquals(20L, request.getIdSucursal());
        assertEquals(5L, request.getCantidad());
        assertEquals("AJUSTE", request.getMotivo());
    }

    @Test
    void metodosGeneradosPorLombokFuncionan() {
        DescuentoStockRequest request = new DescuentoStockRequest(1L, 2L, 3L, "VENTA");
        DescuentoStockRequest mismoRequest = new DescuentoStockRequest(1L, 2L, 3L, "VENTA");
        DescuentoStockRequest otroRequest = new DescuentoStockRequest(4L, 2L, 3L, "VENTA");

        assertEquals(request, mismoRequest);
        assertEquals(request.hashCode(), mismoRequest.hashCode());
        assertNotEquals(request, otroRequest);
        assertNotNull(request.toString());
    }
}
