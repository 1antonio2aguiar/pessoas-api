package com.pesoas.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(of = "id", callSuper = false)
@Entity(name = "Logradouro")
@Table(name = "logradouros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Data
public class Logradouro implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String nome;
    private String nomeReduzido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "distrito_id", nullable = false)
    private Distrito distrito;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "logradouro_bairros",
        joinColumns = @JoinColumn(name = "logradouro_id"),
        inverseJoinColumns = @JoinColumn(name = "bairro_id")
    )
    private List<Bairro> bairros = new ArrayList<>();

    @OneToMany(mappedBy = "logradouro", fetch = FetchType.LAZY)
    private List<Cep> ceps = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_LOGRADOURO_ID", nullable = false)
    private TipoLogradouro tipoLogradouro;
}