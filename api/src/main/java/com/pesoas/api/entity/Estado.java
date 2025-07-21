package com.pesoas.api.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Estado")
@Table(name = "estados")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Estado implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private Long id;
    private String nome;
    private String uf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id")
    private Pais pais;

}