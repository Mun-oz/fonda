package cl.dsy1104.fonda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class VentaRequest {
    
    @NotNull(message = "El id de la bebida es obligatorio")
    private Long bebidaId;

    @Min(value = 1, message = "Las unidades deben ser al menos 1")
    private int unidades;

    public VentaRequest() {}

    public Long getBebidaId() { return bebidaId; }
    public void setBebidaId(Long bebidaId) { this.bebidaId = bebidaId; }
    public int getUnidades() { return unidades; }
    public void setUnidades(int unidades) { this.unidades = unidades; }
}