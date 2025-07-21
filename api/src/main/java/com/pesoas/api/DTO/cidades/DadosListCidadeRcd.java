package com.pesoas.api.DTO.cidades;

import com.pesoas.api.entity.Cidade;

import java.util.Optional;

public record DadosListCidadeRcd(
        long id,
        String cidadeNome,
        String uf
) {
    public DadosListCidadeRcd(Cidade cidade) {
        this(cidade.getId(),
        cidade.getNome(),
        cidade.getEstado().getUf());
    }

    public DadosListCidadeRcd(Optional<Cidade> cidade) {
        this(cidade.get().getId(),
                cidade.get().getNome(),
                cidade.get().getEstado().getUf());
    }

    public String getNome(){
        return cidadeNome.toUpperCase();
    }

}
