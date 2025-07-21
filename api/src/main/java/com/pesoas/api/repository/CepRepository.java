package com.pesoas.api.repository;

import com.pesoas.api.entity.Cep;
import com.pesoas.api.repository.ceps.CepRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CepRepository extends JpaRepository<Cep, Long>, CepRepositoryQuery {
    Optional<Cep> findByCep(String cep);
}
