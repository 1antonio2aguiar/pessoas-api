package com.pesoas.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Pais")
@Table(name = "paises")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pais implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private Long id;
    private String nome;
    private String sigla;
    private String nacionalidade;
}