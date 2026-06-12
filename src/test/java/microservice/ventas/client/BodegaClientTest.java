package microservice.ventas.client;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import microservice.ventas.dto.DescuentoStockRequest;

class BodegaClientTest {

    private static final String URL = "http://localhost:8093/api/v1/bodega/descontar-stock";

    private final RestTemplate restTemplate = org.mockito.Mockito.mock(RestTemplate.class);
    private final BodegaClient bodegaClient = new BodegaClient(restTemplate, URL);

    @Test
    void descontarStockLlamaEndpointConfigurado() {
        DescuentoStockRequest request = new DescuentoStockRequest(1L, 1L, 2, "VENTA");

        bodegaClient.descontarStock(request);

        verify(restTemplate).put(URL, request);
    }

    @Test
    void descontarStockLanzaExcepcionCuandoBodegaFalla() {
        DescuentoStockRequest request = new DescuentoStockRequest(1L, 1L, 2, "VENTA");
        doThrow(new RestClientException("error")).when(restTemplate).put(URL, request);

        assertThrows(IllegalStateException.class, () -> bodegaClient.descontarStock(request));
    }
}
