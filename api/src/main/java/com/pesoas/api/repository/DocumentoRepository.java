package com.pesoas.api.repository;

import com.pesoas.api.entity.Documento;
import com.pesoas.api.repository.documentos.DocumentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long>, DocumentoRepositoryQuery {
    List<Documento> findByPessoaId(Long pessoaId);

}
