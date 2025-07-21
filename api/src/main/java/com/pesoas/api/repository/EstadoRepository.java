package com.pesoas.api.repository;


import com.pesoas.api.entity.Estado;
import com.pesoas.api.repository.estados.EstadoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long>, EstadoRepositoryQuery {

}
