package cl.dsy1104.fonda.service;

import cl.dsy1104.fonda.dto.VentaRequest;
import cl.dsy1104.fonda.dto.VentaResponse;
import cl.dsy1104.fonda.exception.ResourceNotFoundException;
import cl.dsy1104.fonda.exception.VentaRechazadaException;
import cl.dsy1104.fonda.model.Bebida;
import cl.dsy1104.fonda.model.EstadoVenta;
import cl.dsy1104.fonda.model.TipoBebida;
import cl.dsy1104.fonda.model.Venta;
import cl.dsy1104.fonda.repository.BebidaRepository;
import cl.dsy1104.fonda.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final BebidaRepository bebidaRepository;
    private final BebidaService bebidaService;

    @Value("${fonda.limite-unidades-por-cliente:3}")
    private int limiteAlcohol;

    public VentaService(VentaRepository ventaRepository, BebidaRepository bebidaRepository, BebidaService bebidaService) {
        this.ventaRepository = ventaRepository;
        this.bebidaRepository = bebidaRepository;
        this.bebidaService = bebidaService;
    }

    public List<VentaResponse> listarHistorial() {
        return ventaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public VentaResponse registrarVenta(VentaRequest request) {
        Bebida bebida = bebidaRepository.findById(request.getBebidaId())
                .orElseThrow(() -> new ResourceNotFoundException("Bebida no encontrada con id: " + request.getBebidaId()));

        // Regla 1: Venta restringida
        if (bebida.isVentaRestringida()) {
            guardarVentaRechazada(bebida, request.getUnidades(), "VENTA_RESTRINGIDA");
            throw new VentaRechazadaException("VENTA_RESTRINGIDA", "La bebida se encuentra con venta restringida.");
        }

        // Regla 2: Límite por cliente para alcohólicas
        if (bebida.getTipo() == TipoBebida.ALCOHOLICA && request.getUnidades() > limiteAlcohol) {
            guardarVentaRechazada(bebida, request.getUnidades(), "LIMITE_EXCEDIDO");
            throw new VentaRechazadaException("LIMITE_EXCEDIDO", 
                    request.getUnidades() + " unidades superan el limite de " + limiteAlcohol + " por cliente.");
        }

        // Regla 3: Stock insuficiente
        if (bebida.getStock() < request.getUnidades()) {
            guardarVentaRechazada(bebida, request.getUnidades(), "STOCK_INSUFICIENTE");
            throw new VentaRechazadaException("STOCK_INSUFICIENTE", "Stock insuficiente para realizar la venta.");
        }

        // Regla 4: Autorización y descuento de stock
        bebida.setStock(bebida.getStock() - request.getUnidades());
        bebidaRepository.save(bebida);

        int total = bebidaService.calcularPrecio(bebida) * request.getUnidades();

        Venta venta = new Venta();
        venta.setBebida(bebida);
        venta.setUnidades(request.getUnidades());
        venta.setTotal(total);
        venta.setEstado(EstadoVenta.AUTORIZADA);
        venta.setFecha(LocalDateTime.now());

        Venta guardada = ventaRepository.save(venta);
        return toResponse(guardada);
    }

    private void guardarVentaRechazada(Bebida bebida, int unidades, String motivo) {
        Venta venta = new Venta();
        venta.setBebida(bebida);
        venta.setUnidades(unidades);
        venta.setTotal(0);
        venta.setEstado(EstadoVenta.RECHAZADA);
        venta.setMotivo(motivo);
        venta.setFecha(LocalDateTime.now());
        ventaRepository.save(venta);
    }

    private VentaResponse toResponse(Venta v) {
        return new VentaResponse(
                v.getId(),
                v.getBebida().getId(),
                v.getBebida().getNombre(),
                v.getUnidades(),
                v.getTotal(),
                v.getEstado(),
                v.getMotivo(),
                v.getFecha()
        );
    }
}