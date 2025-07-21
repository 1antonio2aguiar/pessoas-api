package com.pesoas.api.repository;

import com.pesoas.api.entity.Distrito;
import com.pesoas.api.repository.distritos.DistritoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DistritoRepository extends JpaRepository<Distrito, Long>, DistritoRepositoryQuery {

}
