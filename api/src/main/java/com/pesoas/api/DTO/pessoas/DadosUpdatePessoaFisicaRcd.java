package com.pesoas.api.DTO.pessoas;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DadosUpdatePessoaFisicaRcd(
        // Campos da entidade Pessoa (pai)
        Long id, // Nulo para insert, preenchido para update

        @Size(max = 200, message = "Nome deve ter no máximo 200 caracteres")
        String nome,

        @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
        String observacao,

        @NotNull(message = "Situação é obrigatória")
        Integer situacao, // Recebe o Enum Situacao

        Long tipoPessoaId, // ID da entidade TiposPessoas

        // Campos da entidade DadosPessoaFisica (filha)
        @Size(min = 11, max = 11, message = "CPF deve ter 11 caracteres")
        String cpf,

        @Size(max = 1, message = "Sexo deve ter no máximo 1 caractere")
        String sexo,

        @Min(value = 0, message = "Código do estado civil deve ser no mínimo 0")
        @Max(value = 5, message = "Código do estado civil deve ser no máximo 5")
        Integer estadoCivil,

        @PastOrPresent(message = "Data de nascimento deve ser no passado ou presente")
        LocalDate dataNascimento,

        @Size(max = 100, message = "Nome da mãe deve ter no máximo 100 caracteres")
        String nomeMae,

        @Size(max = 100, message = "Nome do pai deve ter no máximo 100 caracteres")
        String nomePai
) {
    // Construtor canônico é gerado automaticamente pelo record.
    // Não são necessários métodos de fábrica como fromPessoa aqui, pois este DTO é para ENTRADA de dados.
}