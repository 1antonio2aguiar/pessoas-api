package com.pesoas.api.repository.distritos;

import com.pesoas.api.entity.Distrito;
import com.pesoas.api.filter.enderecos.DistritoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistritoRepositoryQuery {
    public Page<Distrito> filtrar(DistritoFilter distritoFilter, Pageable pageable);

    public List<Distrito> filtrar(DistritoFilter Filter);
}
