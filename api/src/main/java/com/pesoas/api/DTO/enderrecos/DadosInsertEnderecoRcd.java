package com.pesoas.api.DTO.enderrecos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DadosInsertEnderecoRcd(
        // Campos da entidade Pessoa (pai)
        Long id, // Nulo para insert, preenchido para update
        Long numero,
        String principal,
        Integer tipoEndereco,
        String complemento,
        Long pessoaId,
        Long cepId,
        Long logradouroId,
        Long bairroId
) {
    // Construtor canônico é gerado automaticamente pelo record.
    // Não são necessários métodos de fábrica como fromPessoa aqui, pois este DTO é para ENTRADA de dados.
}