package cl.dsy1104.fonda.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private String error;
    private String mensaje;
    private Map<String, String> campos;

    // Constructor para errores generales (ej. 404 Not Found, 409 Conflict)
    public ErrorResponse(String error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    // Constructor para errores de validación (ej. 400 Bad Request)
    public ErrorResponse(String error, Map<String, String> campos) {
        this.error = error;
        this.campos = campos;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Map<String, String> getCampos() { return campos; }
    public void setCampos(Map<String, String> campos) { this.campos = campos; }
}