package com.pesoas.api.filter.enderecos;
import lombok.Data;

@Data
public class BairroFilter {
    private Long id;
    private String nome;

    private DistritoFilter distritoFilter = new DistritoFilter();
}
