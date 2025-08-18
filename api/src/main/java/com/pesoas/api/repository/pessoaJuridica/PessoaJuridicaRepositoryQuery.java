package com.pesoas.api.repository.pessoaJuridica;

import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.PessoaJuridica;
import com.pesoas.api.filter.pessoas.PessoaFilter;
import com.pesoas.api.filter.pessoas.PessoaFisicaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaJuridicaRepositoryQuery {
    public Page<PessoaJuridica> filtrar(PessoaFilter filter, Pageable pageable);

    public List<PessoaJuridica> filtrar(PessoaFilter Filter);
}
