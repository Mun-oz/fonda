package cl.dsy1104.fonda.exception;

import java.util.Map;

public class BadRequestException extends RuntimeException {
    private final String error;
    private final Map<String, String> campos;

    public BadRequestException(String error, Map<String, String> campos) {
        super("Error de validación");
        this.error = error;
        this.campos = campos;
    }

    public String getError() {
        return error;
    }

    public Map<String, String> getCampos() {
        return campos;
    }
}