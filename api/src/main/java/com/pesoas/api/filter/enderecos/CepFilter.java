package com.pesoas.api.filter.enderecos;
import lombok.Data;

@Data
public class CepFilter {
    private Long id;
    private String cep;

    private LogradouroFilter logradouroFilter = new LogradouroFilter();
}
