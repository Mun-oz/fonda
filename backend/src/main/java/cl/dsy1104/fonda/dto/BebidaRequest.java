package cl.dsy1104.fonda.dto;

import cl.dsy1104.fonda.model.TipoBebida;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BebidaRequest {

    @NotBlank(message = "no puede estar vacio")
    private String nombre;

    @NotNull(message = "es obligatorio")
    private TipoBebida tipo;

    @Min(value = 100, message = "debe estar entre 100 y 3000")
    @Max(value = 3000, message = "debe estar entre 100 y 3000")
    private int volumenML;

    @Min(value = 0, message = "debe ser mayor o igual a cero")
    private int stock;

    private Double gradosAlcohol;
    private Boolean certificada;
    private Integer azucarPorLitro;
    private boolean ventaRestringida;

}
