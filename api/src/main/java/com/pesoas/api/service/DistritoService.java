package com.pesoas.api.service;

import com.pesoas.api.DTO.distritos.DadosListDistritoRcd;
import com.pesoas.api.entity.Distrito;
import com.pesoas.api.filter.enderecos.DistritoFilter;
import com.pesoas.api.repository.DistritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistritoService {
    @Autowired private DistritoRepository distritoRepository;
    @Transactional(readOnly = true)
    public Page<DadosListDistritoRcd> findAllPaginated(Pageable paginacao) {
        Page<Distrito> distritosPage = distritoRepository.findAll(paginacao);
        return distritosPage.map(DadosListDistritoRcd::new);
    }

    @Transactional(readOnly = true)
    public List<DadosListDistritoRcd> pesquisarSemPaginacao(DistritoFilter filter) {
        List<Distrito> distritos = distritoRepository.filtrar(filter);
        return distritos.stream()
                .map(DadosListDistritoRcd::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DadosListDistritoRcd> pesquisarComPaginacao(DistritoFilter filter, Pageable pageable) {
        Page<Distrito> distritosPage = distritoRepository.filtrar(filter, pageable);
        return distritosPage.map(DadosListDistritoRcd::new);
    }

    // Distrito por id
    @Transactional(readOnly = true)
    public DadosListDistritoRcd findById(Long id) {
        Distrito distrito = distritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distrito não encontrada com id: " + id));
        return new DadosListDistritoRcd(distrito);
    }
}