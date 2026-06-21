package microservice.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoStockRequest {

    private Long idProducto;
    private Long idSucursal;
    private Long cantidad;
    private String motivo;
}
