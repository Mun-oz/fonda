package cl.dsy1104.fonda.dto;

import cl.dsy1104.fonda.model.EstadoVenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponse {
    private Long id;
    private Long bebidaId;
    private String nombre;
    private int unidades;
    private int total;
    private EstadoVenta estado;
    private String motivo;
    private LocalDateTime fecha;
}