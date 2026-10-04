package cl.dsy1104.fonda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class VentaRequest {

    @NotNull(message = "El id de la bebida es obligatorio")
    private Long Id; // o bebidaId
    
    @NotNull(message = "El id de la bebida es obligatorio")
    private Long bebidaId;

    @Min(value = 1, message = "Las unidades deben ser al menos 1")
    private int unidades;
}
