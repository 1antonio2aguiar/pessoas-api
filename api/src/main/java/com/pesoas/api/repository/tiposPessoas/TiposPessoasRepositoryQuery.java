package com.pesoas.api.repository.tiposPessoas;

import com.pesoas.api.entity.TiposPessoas;
import com.pesoas.api.filter.pessoas.TiposPessoasFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TiposPessoasRepositoryQuery {
    public Page<TiposPessoas> filtrar(TiposPessoasFilter tiposPessoasFilter, Pageable pageable);
    public List<TiposPessoas> filtrar(TiposPessoasFilter Filter);
}
