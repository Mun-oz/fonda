package cl.dsy1104.fonda.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja errores de anotaciones @Valid de Spring (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            campos.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("error", "VALIDACION");
        body.put("campos", campos);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Maneja errores de validación condicional lógica (400 Bad Request)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getError());
        body.put("campos", ex.getCampos());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Maneja rechazos de venta (409 Conflict)
    @ExceptionHandler(VentaRechazadaException.class)
    public ResponseEntity<Map<String, String>> handleVentaRechazada(VentaRechazadaException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getError());
        body.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // Maneja recursos no encontrados (404 Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "NOT_FOUND");
        body.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}