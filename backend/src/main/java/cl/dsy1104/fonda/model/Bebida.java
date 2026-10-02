package cl.dsy1104.fonda.model;

import java.lang.annotation.Inherited;
import javax.annotation.processing.Generated;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bebida")
public class Bebida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoBebida tipo;

    @Column(nullable = false)
    private int volumenML;

    @Column(nullable = false)
    private int stock;

    private Double gradosAlcohol;
    private Boolean certificada;
    private Interger azucarPorLitro;

    @Column(nullable = false)
    private boolean ventaRestringida;

}
