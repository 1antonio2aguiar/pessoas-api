package com.pesoas.api.DTO.tiposPessoas;

import com.pesoas.api.entity.TiposPessoas;
import java.util.Optional;

public record DadosListTipoPessoaRcd(
        long id,
        String nome
) {
    public DadosListTipoPessoaRcd(TiposPessoas tipoPessoa) {
        this(tipoPessoa.getId(),
             tipoPessoa.getNome());
    }

    public DadosListTipoPessoaRcd(Optional<TiposPessoas> tipoPessoa) {
        this(tipoPessoa.get().getId(),
             tipoPessoa.get().getNome());
    }

    public String getNome(){
        return nome.toUpperCase();
    }
}
