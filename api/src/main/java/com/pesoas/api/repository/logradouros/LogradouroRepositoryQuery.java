package com.pesoas.api.repository.logradouros;

import com.pesoas.api.entity.Logradouro;
import com.pesoas.api.filter.enderecos.LogradouroFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogradouroRepositoryQuery {
    public Page<Logradouro> filtrar(LogradouroFilter distritoFilter, Pageable pageable);

    public List<Logradouro> filtrar(LogradouroFilter Filter);
}
