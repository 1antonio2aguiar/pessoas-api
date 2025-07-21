package com.pesoas.api.DTO.contatos;

public record DadosUpdateContatoRcd(
        // Campos da entidade Pessoa (pai)
        Long id, // Nulo para insert, preenchido para update
        Integer tipoContato,
        String contato,
        String complemento,
        String principal
) {
    // Construtor canônico é gerado automaticamente pelo record.
    // Não são necessários métodos de fábrica como fromPessoa aqui, pois este DTO é para ENTRADA de dados.
}