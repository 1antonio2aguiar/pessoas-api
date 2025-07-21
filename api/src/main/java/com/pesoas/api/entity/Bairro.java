package com.pesoas.api.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Bairro")
@Table(name = "bairros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bairro implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String nome;
    private String nomeAbreviado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distrito_id", nullable = false)
    private Distrito distrito;

}