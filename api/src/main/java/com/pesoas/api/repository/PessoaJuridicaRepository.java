package com.pesoas.api.repository;


import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.PessoaJuridica;
import com.pesoas.api.repository.pessoaFisica.PessoaFisicaRepositoryQuery;
import com.pesoas.api.repository.pessoaJuridica.PessoaJuridicaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long>, PessoaJuridicaRepositoryQuery {

}
