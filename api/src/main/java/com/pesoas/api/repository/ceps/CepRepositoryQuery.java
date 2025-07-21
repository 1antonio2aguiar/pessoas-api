package com.pesoas.api.repository.ceps;

import com.pesoas.api.entity.Cep;
import com.pesoas.api.filter.enderecos.CepFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CepRepositoryQuery {
    public Page<Cep> filtrar(CepFilter cepFilter, Pageable pageable);

    public List<Cep> filtrar(CepFilter Filter);
}
