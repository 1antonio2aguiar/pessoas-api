package com.pesoas.api.DTO.distritos;

import com.pesoas.api.entity.Distrito;

import java.util.Optional;

public record DadosListDistritoRcd(
        long id,
        String nome,
        Long cidadeId,
        String cidadeNome
) {
    public DadosListDistritoRcd(Distrito distrito) {
        this(distrito.getId(),
        distrito.getNome(),
        distrito.getCidade().getId(),
        distrito.getCidade().getNome());
    }

    public DadosListDistritoRcd(Optional<Distrito> distrito) {
        this(distrito.get().getId(),
        distrito.get().getNome(),
        distrito.get().getCidade().getId(),
        distrito.get().getCidade().getNome());
    }

    public String getNome(){
        return nome.toUpperCase();
    }

}
