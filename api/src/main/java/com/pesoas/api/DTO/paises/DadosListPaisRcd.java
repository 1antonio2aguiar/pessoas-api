package com.pesoas.api.DTO.paises;

import com.pesoas.api.entity.Pais;

import java.util.Optional;

public record DadosListPaisRcd(
        long id,
        String nome,
        String sigla,
        String nacionalidade
) {
    public DadosListPaisRcd(Pais pais) {
        this(pais.getId(),
             pais.getNome(),
             pais.getSigla(),
             pais.getNacionalidade());
    }

    public DadosListPaisRcd(Optional<Pais> paisOptional) {
        this(paisOptional.orElseThrow(
                () -> new IllegalArgumentException("Optional<Pais> não pode estar vazio para criar DadosListPaisRcd"))
        );
    }

    public String getNome(){
        return this.nome != null ? this.nome.toUpperCase() : null;
    }

    public String getSigla(){
        return this.sigla != null ? this.sigla.toUpperCase() : null;
    }
    public String getNacionalidade() {
        return this.nacionalidade != null ? this.nacionalidade.toUpperCase() : null;
    }
}
