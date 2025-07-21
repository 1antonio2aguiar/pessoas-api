package com.pesoas.api.filter.enderecos;
import lombok.Data;

@Data
public class CidadeFilter {
    private Long id;
    private String nome;

    private EstadoFilter estadoFilter = new EstadoFilter();
}
