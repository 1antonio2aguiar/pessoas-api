package com.pesoas.api.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Distrito")
@Table(name = "distritos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Distrito implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private Long id;
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cidade_id")
    private Cidade cidade;

}