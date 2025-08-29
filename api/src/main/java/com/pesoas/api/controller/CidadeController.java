package com.pesoas.api.controller;

import com.pesoas.api.DTO.cidades.DadosListCidadeRcd;
import com.pesoas.api.DTO.logradouros.LogradouroPesquisaRcd;
import com.pesoas.api.filter.enderecos.CidadeFilter;
import com.pesoas.api.service.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cidade")
public class CidadeController {
    @Autowired private CidadeService cidadeService;

    // Listar cidades
    @GetMapping
    public Page<DadosListCidadeRcd> findall(@PageableDefault(sort={"nome"}) Pageable paginacao) {
        return cidadeService.findAllPaginated(paginacao);
    }

    // Lista de cidades com filter
    @GetMapping("/list")
    public List<DadosListCidadeRcd> pesquisar(CidadeFilter filter) {
        return cidadeService.pesquisarSemPaginacao(filter);
    }

    // Lista de cidades com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListCidadeRcd> pesquisar(CidadeFilter filter, Pageable pageable) {
        return cidadeService.pesquisarComPaginacao(filter, pageable);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DadosListCidadeRcd> findById(@PathVariable Long id) {
        DadosListCidadeRcd cidadeDto = cidadeService.findById(id);
        return ResponseEntity.ok().body(cidadeDto);
    }
}


