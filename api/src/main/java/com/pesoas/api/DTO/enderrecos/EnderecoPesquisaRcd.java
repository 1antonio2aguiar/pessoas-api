package com.pesoas.api.DTO.enderrecos;

import com.pesoas.api.entity.Endereco;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record EnderecoPesquisaRcd(
        String tipoLogradouro,
        long logradouroId,
        String logradouroNome,
        List<BairroInfoRcd> bairros,
        String cidadeNome,
        String uf,
        Long cepId,
        String cep
) {
    public EnderecoPesquisaRcd(Endereco endereco) {
        this(

            endereco.getLogradouro().getTipoLogradouro().getSigla(),
            endereco.getLogradouro().getId(),
            endereco.getLogradouro().getNome(),

            endereco.getLogradouro().getBairros() != null ?
                endereco.getLogradouro().getBairros().stream()
                    .map(bairro -> new BairroInfoRcd(bairro.getId(), bairro.getNome()))
                    .collect(Collectors.toList()) :
                Collections.emptyList(),

            endereco.getLogradouro().getDistrito().getCidade().getNome(),
            endereco.getLogradouro().getDistrito().getCidade().getEstado().getUf(),
            endereco.getCep().getId(),
            endereco.getCep().getCep()
        );
    }
    record BairroInfoRcd(Long id, String nome) {
    }
}
