package com.pesoas.api.repository.pessoaFisica;

import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.filter.pessoas.PessoaFisicaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaFisicaRepositoryQuery {
    public Page<PessoaFisica> filtrar(PessoaFisicaFilter filter, Pageable pageable);

    public List<PessoaFisica> filtrar(PessoaFisicaFilter Filter);
}
