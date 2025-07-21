package com.pesoas.api.entity;

import com.pesoas.api.entity.enuns.TipoContato;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Contato")
@Table(name = "contatos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Contato implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contato;
    private TipoContato tipoContato;
    private String complemento;
    private String principal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private PessoaFisica pessoaFisica;
}