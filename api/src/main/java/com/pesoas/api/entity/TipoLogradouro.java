package com.pesoas.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "TipoLogradouro")
@Table(name = "tipos_logradouros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TipoLogradouro implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private Long id;
    private String descricao;
    private String sigla;
}