package cl.dsy1104.fonda.exception;

import lombok.Getter;
import java.util.Map;

@Getter
public class BadRequestException extends RuntimeException {
    private final String error;
    private final Map<String, String> campos;

    public BadRequestException(String error, Map<String, String> campos) {
        super("Error de validación");
        this.error = error;
        this.campos = campos;
    }
}