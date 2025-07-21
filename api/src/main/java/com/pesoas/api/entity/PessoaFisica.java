package com.pesoas.api.entity;

import com.pesoas.api.entity.enuns.EstadoCivil;
import com.pesoas.api.entity.enuns.Situacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "dados_pessoa_fisica") // Mapeia para a TABELA BASE 'dados_pessoa_fisica'
@DiscriminatorValue("F") // Valor que será armazenado na DiscriminatorColumn da tabela 'pessoas'
@Getter
@Setter
@NoArgsConstructor
// @EqualsAndHashCode(callSuper = true) // Se Pessoas tiver @EqualsAndHashCode e você quiser incluir campos da superclasse
public class PessoaFisica extends Pessoa {
    private static final long serialVersionUID = 1L;

    // O @Id é herdado da classe Pessoa.
    // A tabela 'dados_pessoa_fisica' terá uma coluna 'id' que é PK e FK para 'pessoas.id'.

    @Column(length = 11, unique = true) // CPF deve ser único
    private String cpf;

    @Column(length = 1)
    private String sexo;

    @NotNull // Se o estado civil é obrigatório na entidade
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "estado_civil", nullable = false)
    private EstadoCivil estadoCivil;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "nome_mae", length = 100)
    private String nomeMae;

    @Column(name = "nome_pai", length = 100)
    private String nomePai;

    public PessoaFisica(String nome, String observacao, Situacao situacao, TiposPessoas tiposPessoas,
                        String cpf, String sexo, String estadoCivil, LocalDate dataNascimento,
                        String nomeMae, String nomePai) {
        super(nome, observacao, situacao, tiposPessoas); // Chama o construtor da classe pai
        // ...
    }

}