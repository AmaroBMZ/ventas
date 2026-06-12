package microservice.ventas.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import microservice.ventas.dto.DescuentoStockRequest;

@Component
public class BodegaClient {

    private final RestTemplate restTemplate;
    private final String descontarStockUrl;

    public BodegaClient(
            RestTemplate restTemplate,
            @Value("${microservices.bodega.descontar-stock-url}") String descontarStockUrl) {
        this.restTemplate = restTemplate;
        this.descontarStockUrl = descontarStockUrl;
    }

    public void descontarStock(DescuentoStockRequest request) {
        try {
            restTemplate.put(descontarStockUrl, request);
        } catch (RestClientException ex) {
            throw new IllegalStateException("No se pudo descontar stock en bodega", ex);
        }
    }
}
