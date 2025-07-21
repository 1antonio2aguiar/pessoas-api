package com.pesoas.api.DTO.pessoas.fj;

import com.pesoas.api.entity.Pessoa;
import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.PessoaJuridica;

import java.time.LocalDate;
import java.util.Optional;

public record DadosPessoaFjReduzidoRcd(
        // Campos Comuns (da entidade Pessoa)
        Long id,
        String nome,
        String fisicaJuridica, // "F" ou "J"

        // Campos Específicos de Pessoa Física (podem ser null se for Pessoa Jurídica)
        String cpf,
        LocalDate dataNascimento,

        // Campos Específicos de Pessoa Jurídica (podem ser null se for Pessoa Física)
        String cnpj

) {
    /**
     * Método de fábrica para converter uma entidade Pessoa (que pode ser
     * PessoaFisica ou PessoaJuridica) para este DTO genérico.
     */
    public static DadosPessoaFjReduzidoRcd fromPessoa(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        // Campos específicos de Pessoa Física
        String cpf = null;
        String fisicaJuridica = null;
        LocalDate dataNascimento = null;

        // Campos específicos de Pessoa Jurídica
        String cnpj = null;
        String nomeFantasia = null;

        // Verifica o tipo real da instância e preenche os campos específicos
        if (pessoa instanceof PessoaFisica pf) { // Usa pattern matching (Java 16+)
            cpf = pf.getCpf();
            dataNascimento = pf.getDataNascimento();
        } else if (pessoa instanceof PessoaJuridica pj) { // Usa pattern matching
            cnpj = pj.getCnpj();
            nomeFantasia = pj.getNomeFantasia();
        }

        return new DadosPessoaFjReduzidoRcd(
                pessoa.getId(),
                pessoa.getNome(),
                fisicaJuridica,
                cpf,
                dataNascimento,
                cnpj
        );
    }

    // Opcional: Se você tiver um Optional<Pessoa>
    public static DadosPessoaFjReduzidoRcd fromOptionalPessoa(Optional<Pessoa> pessoaOptional) {
        return pessoaOptional.map(DadosPessoaFjReduzidoRcd::fromPessoa).orElse(null);
    }
}