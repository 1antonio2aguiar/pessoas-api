package com.pesoas.api.DTO.logradouros;

import com.pesoas.api.DTO.bairros.BairroInfoRcd;
import com.pesoas.api.entity.Logradouro;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
// Importar BairroInfoRcd se estiver em um pacote diferente

public record LogradouroPesquisaRcd(
        Long id,
        String tipoLogradouro,
        String logradouroNome,
        List<BairroInfoRcd> bairros, // Agora se refere ao BairroInfoRcd externo
        String cidadeNome,
        String uf,
        Long cepId,
        String cep
) {
    // Construtor usado pela query JPQL
    public LogradouroPesquisaRcd(Long id, String tipoLogradouro, String logradouroNome,
                                 String cidadeNome, String uf, Long cepId, String cep) {
        this(id, tipoLogradouro, logradouroNome, Collections.emptyList(), // Inicializa bairros como vazio
                cidadeNome, uf, cepId, cep);
    }

    // Construtor que recebe um Logradouro completo (para testes ou outros usos)
    public LogradouroPesquisaRcd(Logradouro logradouro) {
        this(
                logradouro.getId(),
                logradouro.getTipoLogradouro().getSigla(),
                logradouro.getNome(),
                logradouro.getBairros() != null ?
                        logradouro.getBairros().stream()
                                .map(bairro -> new BairroInfoRcd(bairro.getId(), bairro.getNome()))
                                .collect(Collectors.toList()) :
                        Collections.emptyList(),
                logradouro.getDistrito().getCidade().getNome(),
                logradouro.getDistrito().getCidade().getEstado().getUf(),
                null,
                null
        );
    }

    // O record aninhado foi REMOVIDO daqui

    public String getlogradouroNome(){
        return logradouroNome.toUpperCase();
    }
}