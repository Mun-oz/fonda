package cl.dsy1104.fonda.exception;

import lombok.Getter;

@Getter
public class VentaRechazadaException extends RuntimeException {
    private final String error;

    public VentaRechazadaException(String error, String mensaje) {
        super(mensaje);
        this.error = error;
    }
}