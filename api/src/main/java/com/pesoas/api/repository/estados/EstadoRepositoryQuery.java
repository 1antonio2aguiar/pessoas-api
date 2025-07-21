package com.pesoas.api.repository.estados;

import com.pesoas.api.entity.Estado;
import com.pesoas.api.filter.enderecos.EstadoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoRepositoryQuery {
    public Page<Estado> filtrar(EstadoFilter estadoFilter, Pageable pageable);

    public List<Estado> filtrar(EstadoFilter Filter);
}
