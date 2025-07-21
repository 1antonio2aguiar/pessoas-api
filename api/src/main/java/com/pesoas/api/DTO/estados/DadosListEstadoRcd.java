package com.pesoas.api.DTO.estados;

import com.pesoas.api.entity.Estado;

import java.util.Optional;

public record DadosListEstadoRcd(
        long id,
        String pais,
        String nome,
        String uf
) {
    public DadosListEstadoRcd(Estado estado) {
        this(estado.getId(),
             estado.getPais().getNome(),
             estado.getNome(),
             estado.getUf());
    }

    public DadosListEstadoRcd(Optional<Estado> estado) {
        this(estado.get().getId(),
             estado.get().getPais().getNome(),
             estado.get().getNome(),
             estado.get().getUf());
    }

    public String getNome(){
        return nome.toUpperCase();
    }

    public String getUf(){
        return uf.toUpperCase();
    }
}
