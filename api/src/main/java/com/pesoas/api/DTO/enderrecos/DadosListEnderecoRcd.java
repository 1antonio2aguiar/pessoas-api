package com.pesoas.api.DTO.enderrecos;

import com.pesoas.api.entity.Endereco;

public record DadosListEnderecoRcd(
        Long id,
        String pessoaNome,
        Integer tipoEndereco, // Descrição do Enum
        String principal,
        Long tipoLogradouroId,
        String tipoLogradouro,
        Long logradouroId,
        String logradouroNome,
        Long numero,
        String complemento,
        Long bairroId,
        String bairroNome,
        String distritoNome,
        String cidadeNome,
        String uf,
        Long cepId,
        String cep
) {
    public DadosListEnderecoRcd(Endereco endereco) {
        this(endereco.getId(),
            endereco.getPessoaFisica().getNome(),
            endereco.getTipoEndereco() != null ? endereco.getTipoEndereco().getCodigo() : null,
            endereco.getPrincipal(),

            endereco.getLogradouro().getTipoLogradouro().getId(),
            endereco.getLogradouro().getTipoLogradouro().getSigla(),

            endereco.getLogradouro().getId(),
            endereco.getLogradouro().getNome(),

            endereco.getNumero(),
            endereco.getComplemento(),

            endereco.getBairro() != null ? endereco.getBairro().getId() : null,
            endereco.getBairro() != null ? endereco.getBairro().getNome() : null,

            endereco.getLogradouro().getDistrito().getNome(),
            endereco.getLogradouro().getDistrito().getCidade().getNome(),
            endereco.getLogradouro().getDistrito().getCidade().getEstado().getUf(),
                endereco.getCep().getId(),
            endereco.getCep().getCep()
        );
    }
    record BairroInfoRcd(Long id, String nome) {
    }
}
