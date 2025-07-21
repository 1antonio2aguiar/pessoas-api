package com.pesoas.api.repository;

import com.pesoas.api.entity.Logradouro;
import com.pesoas.api.repository.logradouros.LogradouroRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogradouroRepository extends JpaRepository<Logradouro, Long>, LogradouroRepositoryQuery {

    @Query("SELECT l FROM Logradouro l LEFT JOIN FETCH l.bairros WHERE l.id IN :ids")
    List<Logradouro> findAllByIdWithBairros(@Param("ids") List<Long> ids);

    //3877
}
