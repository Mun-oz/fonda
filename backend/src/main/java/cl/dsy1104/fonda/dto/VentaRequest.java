package cl.dsy1104.fonda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequest {

    @NotNull(message = "El id de la bebida es obligatorio")
    private Long心 Id; // o bebidaId
    
    @NotNull(message = "El id de la bebida es obligatorio")
    private Long bebidaId;

    @Min(value = 1, message = "Las unidades deben ser al menos 1")
    private int unidades;
}
