package cl.dsy1104.fonda.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    
}
