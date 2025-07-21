package com.pesoas.api.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Cep")
@Table(name = "ceps")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cep implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String cep;
    private Long numero_ini;
    private Long numero_fin;
    private String identificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logradouro_id", nullable = false)
    private Logradouro logradouro;

}