package com.pesoas.api.repository;


import com.pesoas.api.entity.TiposPessoas;
import com.pesoas.api.repository.tiposPessoas.TiposPessoasRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TiposPessoasRepository extends JpaRepository<TiposPessoas, Long>, TiposPessoasRepositoryQuery {

}
