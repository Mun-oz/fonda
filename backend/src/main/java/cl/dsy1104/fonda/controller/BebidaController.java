package cl.dsy1104.fonda.controller;

import cl.dsy1104.fonda.dto.BebidaRequest;
import cl.dsy1104.fonda.dto.BebidaResponse;
import cl.dsy1104.fonda.service.BebidaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bebidas")
public class BebidaController {

    private final BebidaService bebidaService;

    public BebidaController(BebidaService bebidaService) {
        this.bebidaService = bebidaService;
    }

    @GetMapping
    public ResponseEntity<List<BebidaResponse>> listar(@RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(bebidaService.listar(nombre));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BebidaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bebidaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<BebidaResponse> crear(@Valid @RequestBody BebidaRequest request) {
        BebidaResponse creada = bebidaService.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BebidaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody BebidaRequest request) {
        return ResponseEntity.ok(bebidaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bebidaService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PatchMapping("/{id}/restriccion")
    public ResponseEntity<BebidaResponse> cambiarRestriccion(@PathVariable Long id) {
        return ResponseEntity.ok(bebidaService.cambiarRestriccion(id));
    }
}