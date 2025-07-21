package com.pesoas.api.DTO.pessoas;

import com.pesoas.api.entity.PessoaFisica;

import java.time.LocalDate;

public record DadosPessoaFisicaReduzRcd(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento

) {
    public static DadosPessoaFisicaReduzRcd fromPessoaFisica(PessoaFisica dados) {
        return new DadosPessoaFisicaReduzRcd(
                dados.getId(),
                dados.getNome(),
                dados.getCpf(),
                dados.getDataNascimento());
    }
}
