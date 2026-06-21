package microservice.ventas.client;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import microservice.ventas.dto.DescuentoStockRequest;

class InventarioClientTest {

    private static final String URL = "http://localhost:8092/api/v1/inventario/descontar-stock";

    private final RestTemplate restTemplate = org.mockito.Mockito.mock(RestTemplate.class);
    private final InventarioClient inventarioClient = new InventarioClient(restTemplate, URL);

    @Test
    void descontarStockLlamaEndpointConfigurado() {
        DescuentoStockRequest request = new DescuentoStockRequest(1L, 1L, 2L, "VENTA");

        inventarioClient.descontarStock(request);

        verify(restTemplate).put(URL, request);
    }

    @Test
    void descontarStockLanzaExcepcionCuandoInventarioFalla() {
        DescuentoStockRequest request = new DescuentoStockRequest(1L, 1L, 2L, "VENTA");
        doThrow(new RestClientException("error")).when(restTemplate).put(URL, request);

        assertThrows(IllegalStateException.class, () -> inventarioClient.descontarStock(request));
    }
}
