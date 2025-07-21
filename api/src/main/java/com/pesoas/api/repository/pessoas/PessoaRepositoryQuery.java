package com.pesoas.api.repository.pessoas;

import com.pesoas.api.filter.pessoas.PessoaFilter;
import com.pesoas.api.entity.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepositoryQuery {
    public Page<Pessoa> filtrar(PessoaFilter filter, Pageable pageable);

    public List<Pessoa> filtrar(PessoaFilter Filter);

    List<Pessoa> pesquisarPorNomeCpfCnpj(String termo);
}
