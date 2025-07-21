package com.pesoas.api.repository.bairros;

import com.pesoas.api.entity.Bairro;
import com.pesoas.api.filter.enderecos.BairroFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BairroRepositoryQuery {
    public Page<Bairro> filtrar(BairroFilter distritoFilter, Pageable pageable);

    public List<Bairro> filtrar(BairroFilter Filter);
}
