package com.pesoas.api.repository.contatos;

import com.pesoas.api.entity.Contato;
import com.pesoas.api.filter.enderecos.ContatoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepositoryQuery {
    public Page<Contato> filtrar(ContatoFilter contatoFilter, Pageable pageable);

    public List<Contato> filtrar(ContatoFilter Filter);
}
