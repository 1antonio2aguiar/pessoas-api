package com.pesoas.api.service;

import com.pesoas.api.DTO.cidades.DadosListCidadeRcd;
import com.pesoas.api.entity.Cidade;
import com.pesoas.api.filter.enderecos.CidadeFilter;
import com.pesoas.api.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CidadeService {
    @Autowired private CidadeRepository cidadeRepository;
    @Transactional(readOnly = true)
    public Page<DadosListCidadeRcd> findAllPaginated(Pageable paginacao) {
        Page<Cidade> cidadesPage = cidadeRepository.findAll(paginacao);
        return cidadesPage.map(DadosListCidadeRcd::new);
    }
    @Transactional(readOnly = true)
    public List<DadosListCidadeRcd> pesquisarSemPaginacao(CidadeFilter filter) {
        List<Cidade> cidades = cidadeRepository.filtrar(filter);
        return cidades.stream()
                .map(DadosListCidadeRcd::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DadosListCidadeRcd> pesquisarComPaginacao(CidadeFilter filter, Pageable pageable) {
        Page<Cidade> cidadesPage = cidadeRepository.filtrar(filter, pageable);
        return cidadesPage.map(DadosListCidadeRcd::new);
    }

    // cidade por id
    @Transactional(readOnly = true)
    public DadosListCidadeRcd findById(Long id) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cidade não encontrada com id: " + id));
        return new DadosListCidadeRcd(cidade);
    }
}