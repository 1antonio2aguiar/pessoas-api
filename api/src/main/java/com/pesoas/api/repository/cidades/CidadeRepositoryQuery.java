package com.pesoas.api.repository.cidades;

import com.pesoas.api.entity.Cidade;
import com.pesoas.api.filter.enderecos.CidadeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CidadeRepositoryQuery {
    public Page<Cidade> filtrar(CidadeFilter cidadeFilter, Pageable pageable);

    public List<Cidade> filtrar(CidadeFilter Filter);
}
