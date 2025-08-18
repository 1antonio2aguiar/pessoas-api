package com.pesoas.api.entity;

import com.pesoas.api.entity.enuns.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(of = "id")
@Entity(name = "Documento")
@Table(name = "documentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Documento implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;
    private String orgaoExpedidor;
    private LocalDate dataExpedicao;
    private LocalDate dataValidade;
    private String complemento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;
}