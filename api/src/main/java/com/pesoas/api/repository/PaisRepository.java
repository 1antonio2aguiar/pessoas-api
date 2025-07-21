package com.pesoas.api.repository;


import com.pesoas.api.entity.Pais;
import com.pesoas.api.repository.paises.PaisRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaisRepository extends JpaRepository<Pais, Long>, PaisRepositoryQuery {

}
