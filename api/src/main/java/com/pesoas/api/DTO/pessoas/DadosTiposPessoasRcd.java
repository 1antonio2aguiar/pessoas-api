package com.pesoas.api.DTO.pessoas;

import com.pesoas.api.entity.TiposPessoas;

import java.util.Optional;

public record DadosTiposPessoasRcd(
        Long id,
        String nome
) {
    public DadosTiposPessoasRcd(TiposPessoas tiposPessoas) {
        this(tiposPessoas.getId(),
                tiposPessoas.getNome());
    }

    public DadosTiposPessoasRcd(Optional<TiposPessoas> tiposPessoas) {
        this(tiposPessoas.get().getId(),
                tiposPessoas.get().getNome());
    }
    public String getNome(){
                return nome.toUpperCase();
        }
}
