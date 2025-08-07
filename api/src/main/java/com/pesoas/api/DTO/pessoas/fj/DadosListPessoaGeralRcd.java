package com.pesoas.api.DTO.pessoas.fj;

import com.pesoas.api.entity.Pessoa;
import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.PessoaJuridica;
import com.pesoas.api.entity.TiposPessoas;

import java.time.LocalDate;
import java.util.Optional;

public record DadosListPessoaGeralRcd(
        // Campos Comuns (da entidade Pessoa)
        Long id,
        String nome,
        String fisicaJuridica, // "F" ou "J"
        Integer situacaoId,
        String situacao,       // Descrição do Enum
        Long tipoPessoaId,
        String tipoPessoaNome,
        String observacao,

        // Campos Específicos de Pessoa Física (podem ser null se for Pessoa Jurídica)
        String cpf,
        String sexo,
        Integer estadoCivilId,
        String estadoCivil,
        LocalDate dataNascimento,
        String nomeMae,
        String nomePai,

        // Campos Específicos de Pessoa Jurídica (podem ser null se for Pessoa Física)
        String cnpj,
        String nomeFantasia,
        String objetoSocial,
        String microEmpresa,
        Integer tipoEmpresa,
        String cpfCnpj
) {
    /**
     * Método de fábrica para converter uma entidade Pessoa (que pode ser
     * PessoaFisica ou PessoaJuridica) para este DTO genérico.
     */
    public static DadosListPessoaGeralRcd fromPessoa(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        TiposPessoas tp = pessoa.getTiposPessoas();
        String tipoPessoaNome = (tp != null) ? tp.getNome() : null;
        Long tipoPessoaId = (tp != null) ? tp.getId() : null;

        Integer situacaoId     = pessoa.getSituacao() != null ? pessoa.getSituacao().getCodigo() : null;
        String situacaoDesc = pessoa.getSituacao() != null ? pessoa.getSituacao().getDescricao() : null;

        String fisicaJuridica = null;

        // Campos específicos de Pessoa Física
        String cpf = null;
        String sexo = null;
        Integer estadoCivilId = null;
        String estadoCivil = null;
        LocalDate dataNascimento = null;
        String nomeMae = null;
        String nomePai = null;

        // Campos específicos de Pessoa Jurídica
        String cnpj = null;
        String nomeFantasia = null;
        String objetoSocial = null;
        String microEmpresa = null;
        Integer tipoEmpresa = null;
        String cpfCnpj = null;

        // Verifica o tipo real da instância e preenche os campos específicos
        if (pessoa instanceof PessoaFisica pf) { // Usa pattern matching (Java 16+)
            fisicaJuridica = "F";
            cpf = pf.getCpf();
            sexo = pf.getSexo();
            estadoCivilId = pf.getEstadoCivil() != null ? pf.getEstadoCivil().getCodigo() : null;
            estadoCivil   = pf.getEstadoCivil() != null ? pf.getEstadoCivil().getDescricao() : null;
            dataNascimento = pf.getDataNascimento();
            nomeMae = pf.getNomeMae();
            nomePai = pf.getNomePai();
            cpfCnpj = pf.getCpf();
        } else if (pessoa instanceof PessoaJuridica pj) { // Usa pattern matching
            fisicaJuridica = "J";
            cnpj = pj.getCnpj();
            nomeFantasia = pj.getNomeFantasia();
            objetoSocial = pj.getObjetoSocial();
            microEmpresa = pj.getMicroEmpresa();
            tipoEmpresa = pj.getTipoEmpresa();
            cpfCnpj = pj.getCnpj();
        }

        return new DadosListPessoaGeralRcd(
                pessoa.getId(),
                pessoa.getNome(),
                fisicaJuridica,
                situacaoId,
                situacaoDesc,
                tipoPessoaId,
                tipoPessoaNome,
                pessoa.getObservacao(),
                // Pessoa Física
                cpf,
                sexo,
                estadoCivilId,
                estadoCivil,
                dataNascimento,
                nomeMae,
                nomePai,
                // Pessoa Jurídica
                cnpj,
                nomeFantasia,
                objetoSocial,
                microEmpresa,
                tipoEmpresa,
                cpfCnpj
        );
    }

    // Opcional: Se você tiver um Optional<Pessoa>
    public static DadosListPessoaGeralRcd fromOptionalPessoa(Optional<Pessoa> pessoaOptional) {
        return pessoaOptional.map(DadosListPessoaGeralRcd::fromPessoa).orElse(null);
    }
}