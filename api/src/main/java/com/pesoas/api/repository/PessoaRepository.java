package com.pesoas.api.repository;


import com.pesoas.api.entity.Pessoa;
import com.pesoas.api.repository.pessoas.PessoaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

}
