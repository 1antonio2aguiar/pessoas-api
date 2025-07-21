package com.pesoas.api.repository;


import com.pesoas.api.entity.Cidade;
import com.pesoas.api.repository.cidades.CidadeRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepository extends JpaRepository<Cidade, Long>, CidadeRepositoryQuery {

}
