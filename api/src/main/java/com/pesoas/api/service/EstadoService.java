package com.pesoas.api.service;

import com.pesoas.api.DTO.estados.DadosListEstadoRcd;
import com.pesoas.api.entity.Estado;
import com.pesoas.api.filter.enderecos.EstadoFilter;
import com.pesoas.api.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoService {
    @Autowired private EstadoRepository estadoRepository;
    @Transactional(readOnly = true)
    public Page<DadosListEstadoRcd> findAllPaginated(Pageable paginacao) {
        Page<Estado> estadosPage = estadoRepository.findAll(paginacao);
        return estadosPage.map(DadosListEstadoRcd::new);
    }
    @Transactional(readOnly = true)
    public List<DadosListEstadoRcd> pesquisarSemPaginacao(EstadoFilter filter) {
        List<Estado> estados = estadoRepository.filtrar(filter);
        return estados.stream()
                .map(DadosListEstadoRcd::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DadosListEstadoRcd> pesquisarComPaginacao(EstadoFilter filter, Pageable pageable) {
        Page<Estado> estadoPage = estadoRepository.filtrar(filter, pageable);
        return estadoPage.map(DadosListEstadoRcd::new);
    }

    // estado por id
    @Transactional(readOnly = true)
    public DadosListEstadoRcd findById(Long id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado não encontrado com id: " + id));
        return new DadosListEstadoRcd(estado);
    }
}