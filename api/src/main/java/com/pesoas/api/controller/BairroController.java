package com.pesoas.api.controller;

import com.pesoas.api.DTO.bairros.DadosListBairroRcd;
import com.pesoas.api.filter.enderecos.BairroFilter;
import com.pesoas.api.service.BairroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bairro")
public class BairroController {
    @Autowired private BairroService bairroService;

    // Listar de bairros
    @GetMapping
    public Page<DadosListBairroRcd> findall(@PageableDefault(sort={"nome"}) Pageable paginacao) {
        return bairroService.findAllPaginated(paginacao);
    }

    // Lista de  Bairro com filter
    @GetMapping("/list")
    public List<DadosListBairroRcd> pesquisar(BairroFilter filter) {
        return bairroService.pesquisarSemPaginacao(filter);
    }

    // Lista de Bairro com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListBairroRcd> pesquisar(BairroFilter filter, Pageable pageable) {
        return bairroService.pesquisarComPaginacao(filter, pageable);
    }

    // Bairro por id
    @GetMapping(value = "/{id}")
    public ResponseEntity<DadosListBairroRcd> findById(@PathVariable Long id) {
        DadosListBairroRcd bairroDto = bairroService.findById(id);
        return ResponseEntity.ok().body(bairroDto);
    }

}


