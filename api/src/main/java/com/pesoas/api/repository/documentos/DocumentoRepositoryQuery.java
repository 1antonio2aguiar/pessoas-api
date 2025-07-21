package com.pesoas.api.repository.documentos;

import com.pesoas.api.entity.Documento;
import com.pesoas.api.filter.enderecos.DocumentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepositoryQuery {
    public Page<Documento> filtrar(DocumentoFilter documentoFilter, Pageable pageable);

    public List<Documento> filtrar(DocumentoFilter Filter);
}
