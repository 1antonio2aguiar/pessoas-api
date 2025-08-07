package com.pesoas.api.DTO.pessoas.fj;

import com.pesoas.api.entity.Pessoa;
import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.PessoaJuridica;

import java.time.LocalDate;
import java.util.Optional;

public record DadosPessoaFjReduzidoRcd(
        Long id,
        String nome,
        String fisicaJuridica, // "F" ou "J"
        String cpf,
        LocalDate dataNascimento,
        String cnpj,
        String cpfCnpj
) {
    public static DadosPessoaFjReduzidoRcd fromPessoa(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        // Variáveis locais
        String cpf = null;
        String fisicaJuridica = null;
        LocalDate dataNascimento = null;
        String cnpj = null;
        String cpfCnpj = null;

        // Verifica o tipo real da instância e preenche os campos específicos
        if (pessoa instanceof PessoaFisica pf) {
            fisicaJuridica = "F";
            cpf = pf.getCpf();
            dataNascimento = pf.getDataNascimento();
            cpfCnpj = pf.getCpf();
        } else if (pessoa instanceof PessoaJuridica pj) {
            fisicaJuridica = "J";
            cnpj = pj.getCnpj();
            cpfCnpj = pj.getCnpj();
        }

        return new DadosPessoaFjReduzidoRcd(
                pessoa.getId(),
                pessoa.getNome(),
                fisicaJuridica,
                cpf,
                dataNascimento,
                cnpj,
                cpfCnpj
        );
    }
}