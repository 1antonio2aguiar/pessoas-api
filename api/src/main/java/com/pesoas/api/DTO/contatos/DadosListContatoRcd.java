package com.pesoas.api.DTO.contatos;

import com.pesoas.api.entity.Contato;

public record DadosListContatoRcd(
        Long id,
        String pessoaNome,
        Integer tipoContato, // codigo do Enum
        String tipoContatoDescricao, // Descrição do Enum
        String principal,
        String contato,
        String complemento

) {
    public DadosListContatoRcd(Contato contato) {
        this(contato.getId(),
                contato.getPessoaFisica().getNome(),
                contato.getTipoContato() != null ? contato.getTipoContato().getCodigo() : null,
                contato.getTipoContato() != null ? contato.getTipoContato().getDescricao() : null,
                contato.getPrincipal(),
                contato.getContato(),
                contato.getComplemento()
        );
    }
}
