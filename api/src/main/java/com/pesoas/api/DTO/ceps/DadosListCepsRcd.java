package com.pesoas.api.DTO.ceps;

import com.pesoas.api.entity.Cep;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record DadosListCepsRcd(
        long id,
        String cep,
        Long numero_ini,
        Long numero_fin,
        String identificacao,
        Long tipoLogradouroId,
        String tipoLogradouro,
        Long logradouroId,
        String logradouroNome,
        List<BairroInfoRcd> bairros,
        Long distritoId,
        String distritoNome,
        Long cidadeId,
        String cidadeNome,
        String uf
) {
    public DadosListCepsRcd(Cep cep) {
        this(cep.getId(),
            cep.getCep(),
            cep.getNumero_fin(),
            cep.getNumero_fin(),
            cep.getIdentificacao(),
           cep.getLogradouro().getTipoLogradouro().getId(),
            cep.getLogradouro().getTipoLogradouro().getSigla(),
            cep.getLogradouro().getId(),
            cep.getLogradouro().getNome(),

            cep.getLogradouro().getBairros() != null ?
                    cep.getLogradouro().getBairros().stream()
                            .map(bairro -> new BairroInfoRcd(bairro.getId(), bairro.getNome()))
                            .collect(Collectors.toList()) :
                    Collections.emptyList(),

            cep.getLogradouro().getDistrito().getId(),
            cep.getLogradouro().getDistrito().getNome(),
            cep.getLogradouro().getDistrito().getCidade().getId(),
            cep.getLogradouro().getDistrito().getCidade().getNome(),
            cep.getLogradouro().getDistrito().getCidade().getEstado().getUf()
        );
    }

    record BairroInfoRcd(Long id, String nome) {
    }

}
