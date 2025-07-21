package com.pesoas.api.DTO.logradouros;

import com.pesoas.api.entity.Logradouro;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record DadosListLogradouroRcd(
        long id,
        String tipoLogradouro,
        String nome,
        List<BairroInfoRcd> bairros,

        //List<Bairro> bairrosB,
        Long distritoId,
        String distritoNome,
        Long cidadeId,
        String cidadeNome,
        String uf
) {
    public DadosListLogradouroRcd(Logradouro logradouro) {
        this(logradouro.getId(),
            logradouro.getTipoLogradouro().getSigla(),
            logradouro.getNome(),

            logradouro.getBairros() != null ?
                    logradouro.getBairros().stream()
                            .map(bairro -> new BairroInfoRcd(bairro.getId(), bairro.getNome()))
                            .collect(Collectors.toList()) :
                    Collections.emptyList(),

                //(List<Bairro>) logradouro.getBairros().stream()
                  //              .map(bairro -> new Bairro()),

            logradouro.getDistrito().getId(),
            logradouro.getDistrito().getNome(),
            logradouro.getDistrito().getCidade().getId(),
            logradouro.getDistrito().getCidade().getNome(),
            logradouro.getDistrito().getCidade().getEstado().getUf()
        );
    }

    record BairroInfoRcd(Long id, String nome) {
    }

    public String getNome(){
        return nome.toUpperCase();
    }

}
