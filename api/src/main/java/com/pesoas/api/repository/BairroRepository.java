package com.pesoas.api.repository;

import com.pesoas.api.entity.Bairro;
import com.pesoas.api.repository.bairros.BairroRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BairroRepository extends JpaRepository<Bairro, Long>, BairroRepositoryQuery {

}
