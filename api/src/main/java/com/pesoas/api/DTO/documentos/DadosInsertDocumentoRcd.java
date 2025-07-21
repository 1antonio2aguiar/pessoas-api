package com.pesoas.api.DTO.documentos;

import java.time.LocalDate;

public record DadosInsertDocumentoRcd(
        // Campos da entidade Pessoa (pai)
        Long id, // Nulo para insert, preenchido para update
        Integer tipoDocumento,
        String numeroDocumento,
        String orgaoExpedidor,
        String complemento,
        LocalDate dataExpedicao,
        LocalDate dataValidade,
        Long pessoaId
) {

}