package microservice.ventas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    @Column(nullable = true)
    private int fechaVenta;

    @Column(nullable = true)
    private double totalVenta;

    @Column(nullable = true)
    private double descuentoVenta;

    @Column(nullable = true)
    private String estadoVenta;

    @Column(nullable = false)
    private Long idPerfume;

    @Column(nullable = false)
    private Long idSucursal;

    public Long getIdPerfume() {
    return idPerfume;
}

public void setIdPerfume(Long idPerfume) {
    this.idPerfume = idPerfume;
}

public Long getIdSucursal() {
    return idSucursal;
}

public void setIdSucursal(Long idSucursal) {
    this.idSucursal = idSucursal;
}
}
