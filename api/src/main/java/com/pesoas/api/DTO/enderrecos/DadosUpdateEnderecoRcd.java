package com.pesoas.api.DTO.enderrecos;

public record DadosUpdateEnderecoRcd(
        // Campos da entidade Pessoa (pai)
        Long numero,
        String principal,
        Integer tipoEndereco,
        String complemento,
        Long cepId,
        Long logradouroId,
        Long bairroId
) {
    // Construtor canônico é gerado automaticamente pelo record.
    // Não são necessários métodos de fábrica como fromPessoa aqui, pois este DTO é para ENTRADA de dados.
}