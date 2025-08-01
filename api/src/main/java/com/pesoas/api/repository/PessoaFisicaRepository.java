package com.pesoas.api.repository;


import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.repository.pessoaFisica.PessoaFisicaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long>, PessoaFisicaRepositoryQuery {

}
