package com.pesoas.api.service;

import com.pesoas.api.DTO.ceps.DadosListCepsRcd;
import com.pesoas.api.entity.Cep;
import com.pesoas.api.filter.enderecos.CepFilter;
import com.pesoas.api.repository.CepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CepService {
    @Autowired private CepRepository cepRepository;

    @Transactional(readOnly = true)
    public Page<DadosListCepsRcd> findAllPaginated(Pageable paginacao) {
        Page<Cep> cepPage = cepRepository.findAll(paginacao);
        return cepPage.map(DadosListCepsRcd::new);
    }

    @Transactional(readOnly = true)
    public List<DadosListCepsRcd> pesquisarSemPaginacao(CepFilter filter) {
        List<Cep> ceps = cepRepository.filtrar(filter);
        return ceps.stream()
                .map(DadosListCepsRcd::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DadosListCepsRcd> pesquisarComPaginacao(CepFilter filter, Pageable pageable) {
        Page<Cep> cepPage = cepRepository.filtrar(filter, pageable);
        return cepPage.map(DadosListCepsRcd::new);
    }

    @Transactional(readOnly = true) // Importante para operações de leitura
    public DadosListCepsRcd findById(Long id) {
        Cep cep = cepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cep não encontrado: " + id));
        return new DadosListCepsRcd(cep);
    }

    @Transactional(readOnly = true)
    public DadosListCepsRcd findByCep(String cep) {
        Cep cepEntidade = cepRepository.findByCep(cep)
                .orElseThrow(() -> new RuntimeException("Cep não encontrado: " + cep));
        return new DadosListCepsRcd(cepEntidade);
    }
}