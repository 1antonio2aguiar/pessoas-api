package com.pesoas.api.DTO.bairros;

import com.pesoas.api.entity.Bairro;

import java.util.Optional;

public record DadosListBairroRcd(
        long id,
        String nome,
        Long distritoId,
        String distritoNome,
        Long cidadeId,
        String cidadeNome
) {
    public DadosListBairroRcd(Bairro bairro) {
        this(bairro.getId(),
            bairro.getNome(),
            bairro.getDistrito().getId(),
            bairro.getDistrito().getNome(),
            bairro.getDistrito().getCidade().getId(),
            bairro.getDistrito().getCidade().getNome()
        );
    }

    public DadosListBairroRcd(Optional<Bairro> bairro) {
        this(bairro.get().getId(),
            bairro.get().getNome(),
            bairro.get().getDistrito().getId(),
            bairro.get().getDistrito().getNome(),
            bairro.get().getDistrito().getCidade().getId(),
            bairro.get().getDistrito().getCidade().getNome()
        );
    }

    public String getNome(){
        return nome.toUpperCase();
    }

}
