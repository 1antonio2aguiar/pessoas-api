package com.pesoas.api.entity;

import com.pesoas.api.entity.enuns.Situacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pessoas")
@Inheritance(strategy = InheritanceType.JOINED) // Estratégia de herança
@DiscriminatorColumn(name = "fisica_juridica",
        discriminatorType = DiscriminatorType.STRING,
        length = 1)
@Getter
@Setter
@NoArgsConstructor // Necessário para JPA
public abstract class Pessoa implements Serializable { // Tornar abstrata
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    // Este campo será gerenciado pelo @DiscriminatorColumn e @DiscriminatorValue
    // mas pode ser útil tê-lo se você precisa consultá-lo diretamente.
    // Se o JPA gerenciar completamente, ele pode ser inferido.
    // Para controle explícito, mantenha-o, mas pode ser redundante.
    //@Column(name = "fisica_juridica", length = 1, nullable = false, updatable = false, insertable = false)
    //private String fisicaJuridica; // O valor será 'F' ou 'J' (definido nos @DiscriminatorValue)

    @Column(length = 500)
    private String observacao;

    @Enumerated(EnumType.ORDINAL) // Ou EnumType.STRING se preferir salvar "ATIVO", "INATIVO"
    @Column(nullable = false)
    private Situacao situacao;

    // Relacionamento com TiposPessoas (se ainda necessário)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_pessoa_id") // Nome da coluna FK na tabela 'pessoas'
    private TiposPessoas tiposPessoas;

    // RELACIONAMENTOS BIDIRECIONAIS COM CASCADE
    // mappedBy="pessoa": Diz ao JPA que este lado do relacionamento é o "inverso".
    // A FK já está definida pelo campo "pessoa" na classe Contato.
    // cascade=CascadeType.ALL: A MÁGICA. Qualquer operação (persist, merge, REMOVE)
    // na Pessoa será replicada ("cascateada") para os seus contatos.
    // orphanRemoval=true: Se um contato for removido da lista (ex: pessoa.getContatos().remove(contato)),
    // ele também será deletado do banco. ESSENCIAL para a exclusão funcionar bem.

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Contato> contatos = new ArrayList<>();

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Endereco> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Documento> documentos = new ArrayList<>();

    // Construtor protegido para uso pelas subclasses (se necessário)
    protected Pessoa(String nome, String observacao, Situacao situacao, TiposPessoas tiposPessoas) {
        this.nome = nome;
        this.observacao = observacao;
        this.situacao = situacao;
        this.tiposPessoas = tiposPessoas;
    }
}