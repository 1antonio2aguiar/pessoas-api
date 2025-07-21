package com.pesoas.api.filter.enderecos;
import lombok.Data;

@Data
public class LogradouroFilter {
    private Long id;
    private String nome;
    private Long tipoLogradouroId;
    private Long cidadeId;
    private String cidadeNome;

    private DistritoFilter distritoFilter = new DistritoFilter();
    private BairroFilter bairroFilter = new BairroFilter();
}
