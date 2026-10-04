package cl.dsy1104.fonda.service;

import cl.dsy1104.fonda.dto.BebidaRequest;
import cl.dsy1104.fonda.dto.BebidaResponse;
import cl.dsy1104.fonda.exception.BadRequestException;
import cl.dsy1104.fonda.exception.ResourceNotFoundException;
import cl.dsy1104.fonda.model.Bebida;
import cl.dsy1104.fonda.model.TipoBebida;
import cl.dsy1104.fonda.repository.BebidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BebidaService {

    private final BebidaRepository bebidaRepository;

    public BebidaService(BebidaRepository bebidaRepository) {
        this.bebidaRepository = bebidaRepository;
    }

    public List<BebidaResponse> listar(String nombre) {
        List<Bebida> bebidas;
        if (nombre != null && !nombre.isBlank()) {
            bebidas = bebidaRepository.findByNombreContainingIgnoreCase(nombre);
        } else {
            bebidas = bebidaRepository.findAll();
        }
        return bebidas.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public BebidaResponse obtenerPorId(Long id) {
        Bebida b = bebidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bebida no encontrada con id: " + id));
        return toResponse(b);
    }

    @Transactional
    public BebidaResponse crear(BebidaRequest request) {
        validarReglasEspecificasTipo(request);

        Bebida bebida = new Bebida();
        mapearRequestAEntidad(request, bebida);
        bebidaRepository.save(bebida);

        return toResponse(bebida);
    }

    @Transactional
    public BebidaResponse actualizar(Long id, BebidaRequest request) {
        validarReglasEspecificasTipo(request);

        Bebida bebida = bebidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bebida no encontrada con id: " + id));

        mapearRequestAEntidad(request, bebida);
        bebidaRepository.save(bebida);

        return toResponse(bebida);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!bebidaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bebida no encontrada con id: " + id);
        }
        bebidaRepository.deleteById(id);
    }

    @Transactional
    public BebidaResponse cambiarRestriccion(Long id) {
        Bebida bebida = bebidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bebida no encontrada con id: " + id));

        bebida.setVentaRestringida(!bebida.isVentaRestringida());
        bebidaRepository.save(bebida);

        return toResponse(bebida);
    }

    public int calcularPrecio(Bebida bebida) {
        if (bebida.getTipo() == TipoBebida.ALCOHOLICA) {
            int base = 3500;
            if (bebida.getCertificada() != null && !bebida.getCertificada()) {
                return (int) Math.round(base * 1.20); // $4.200
            }
            return base;
        } else {
            int base = 2000;
            if (bebida.getAzucarPorLitro() != null && bebida.getAzucarPorLitro() > 80) {
                return (int) Math.round(base * 1.10); // $2.200
            }
            return base;
        }
    }

    private void validarReglasEspecificasTipo(BebidaRequest req) {
        Map<String, String> errores = new HashMap<>();

        if (req.getTipo() == TipoBebida.ALCOHOLICA) {
            if (req.getGradosAlcohol() == null || req.getGradosAlcohol() < 0.5 || req.getGradosAlcohol() > 45.0) {
                errores.put("gradosAlcohol", "debe estar entre 0.5 y 45 para bebidas alcoholicas");
            }
            if (req.getAzucarPorLitro() != null) {
                errores.put("azucarPorLitro", "debe ser nulo para bebidas alcoholicas");
            }
        } else if (req.getTipo() == TipoBebida.SIN_ALCOHOL) {
            if (req.getAzucarPorLitro() == null || req.getAzucarPorLitro() < 0) {
                errores.put("azucarPorLitro", "debe ser mayor o igual a cero para bebidas sin alcohol");
            }
            if (req.getGradosAlcohol() != null) {
                errores.put("gradosAlcohol", "debe ser nulo para bebidas sin alcohol");
            }
        }

        if (!errores.isEmpty()) {
            throw new BadRequestException("VALIDACION", errores);
        }
    }

    private void mapearRequestAEntidad(BebidaRequest req, Bebida b) {
        b.setNombre(req.getNombre());
        b.setTipo(req.getTipo());
        b.setVolumenML(req.getVolumenML());
        b.setStock(req.getStock());
        b.setGradosAlcohol(req.getTipo() == TipoBebida.ALCOHOLICA ? req.getGradosAlcohol() : null);
        b.setCertificada(req.getTipo() == TipoBebida.ALCOHOLICA ? (req.getCertificada() != null ? req.getCertificada() : false) : null);
        b.setAzucarPorLitro(req.getTipo() == TipoBebida.SIN_ALCOHOL ? req.getAzucarPorLitro() : null);
        b.setVentaRestringida(req.isVentaRestringida());
    }

    public BebidaResponse toResponse(Bebida b) {
        return new BebidaResponse(
                b.getId(),
                b.getNombre(),
                b.getTipo(),
                b.getVolumenML(),
                b.getStock(),
                b.getGradosAlcohol(),
                b.getCertificada(),
                b.getAzucarPorLitro(),
                b.isVentaRestringida(),
                calcularPrecio(b)
        );
    }
}