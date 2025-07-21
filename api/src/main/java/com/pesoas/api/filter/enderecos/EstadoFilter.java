package com.pesoas.api.filter.enderecos;
import lombok.Data;

@Data
public class EstadoFilter {
    private Long Id;
    private String Nome;
    private String uf;

    private PaisFilter paisFilter = new PaisFilter();
}
