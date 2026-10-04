package cl.dsy1104.fonda.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bebida_id", nullable = false)
    private Bebida bebida;

    @Column(nullable = false)
    private int unidades;

    @Column(nullable = false)
    private int total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado;

    private String motivo;

    @Column(nullable = false)
    private LocalDateTime fecha;
    
    public Venta() {}

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bebida getBebida() { return bebida; }
    public void setBebida(Bebida bebida) { this.bebida = bebida; }
    public int getUnidades() { return unidades; }
    public void setUnidades(int unidades) { this.unidades = unidades; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public EstadoVenta getEstado() { return estado; }
    public void setEstado(EstadoVenta estado) { this.estado = estado; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
