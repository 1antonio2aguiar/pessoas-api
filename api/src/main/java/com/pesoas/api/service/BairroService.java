package com.pesoas.api.service;

import com.pesoas.api.DTO.bairros.DadosListBairroRcd;
import com.pesoas.api.entity.Bairro;
import com.pesoas.api.filter.enderecos.BairroFilter;
import com.pesoas.api.repository.BairroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BairroService {
    @Autowired private BairroRepository bairroRepository;
    @Transactional(readOnly = true)
    public Page<DadosListBairroRcd> findAllPaginated(Pageable paginacao) {
        Page<Bairro> bairroPage = bairroRepository.findAll(paginacao);
        return bairroPage.map(DadosListBairroRcd::new);
    }

    @Transactional(readOnly = true)
    public List<DadosListBairroRcd> pesquisarSemPaginacao(BairroFilter filter) {
        List<Bairro> bairros = bairroRepository.filtrar(filter);
        return bairros.stream()
                .map(DadosListBairroRcd::new)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public Page<DadosListBairroRcd> pesquisarComPaginacao(BairroFilter filter, Pageable pageable) {
        Page<Bairro> bairrosPage = bairroRepository.filtrar(filter, pageable);
        return bairrosPage.map(DadosListBairroRcd::new);
    }

    // Bairro por id
    @Transactional(readOnly = true)
    public DadosListBairroRcd findById(Long id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado com id: " + id));
        return new DadosListBairroRcd(bairro);
    }
}