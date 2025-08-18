package com.pesoas.api.entity;

import com.pesoas.api.entity.enuns.TipoEndereco;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Endereco")
@Table(name = "enderecos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Endereco implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long numero;
    private TipoEndereco tipoEndereco;
    private String complemento;
    private String principal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cep_id", nullable = false)
    private Cep cep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logradouro_id", nullable = false)
    private Logradouro logradouro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;

}