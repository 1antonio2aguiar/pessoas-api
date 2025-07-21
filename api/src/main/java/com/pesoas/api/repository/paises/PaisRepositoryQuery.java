package com.pesoas.api.repository.paises;

import com.pesoas.api.entity.Pais;
import com.pesoas.api.filter.enderecos.PaisFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaisRepositoryQuery {
    public Page<Pais> filtrar(PaisFilter paisFilter, Pageable pageable);

    public List<Pais> filtrar(PaisFilter Filter);
}
