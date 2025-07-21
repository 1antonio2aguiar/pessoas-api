package com.pesoas.api.repository;


import com.pesoas.api.entity.TipoLogradouro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoLogradouroRepository extends JpaRepository<TipoLogradouro, Long> {
    List<TipoLogradouro> findAllByOrderByDescricaoAsc();
}
