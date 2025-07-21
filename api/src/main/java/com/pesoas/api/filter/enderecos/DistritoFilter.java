package com.pesoas.api.filter.enderecos;
import lombok.Data;

@Data
public class DistritoFilter {
    private Long id;
    private String nome;

    private CidadeFilter cidadeFilter = new CidadeFilter();
}
