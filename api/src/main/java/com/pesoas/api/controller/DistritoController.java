package com.pesoas.api.controller;

import com.pesoas.api.DTO.cidades.DadosListCidadeRcd;
import com.pesoas.api.DTO.distritos.DadosListDistritoRcd;
import com.pesoas.api.filter.enderecos.DistritoFilter;
import com.pesoas.api.service.DistritoService;
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
@RequestMapping("/distrito")
public class DistritoController {
    @Autowired private DistritoService distritoService;

    // Listar Distrito
    @GetMapping
    public Page<DadosListDistritoRcd> findall(@PageableDefault(sort={"nome"}) Pageable paginacao) {
        return distritoService.findAllPaginated(paginacao);
    }

    // Lista de Distrito com filter
    @GetMapping("/list")
    public List<DadosListDistritoRcd> pesquisar(DistritoFilter filter) {
        return distritoService.pesquisarSemPaginacao(filter);
    }

    // Lista de distritos com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListDistritoRcd> pesquisar(DistritoFilter filter, Pageable pageable) {
        return distritoService.pesquisarComPaginacao(filter, pageable);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DadosListDistritoRcd> findById(@PathVariable Long id) {
        DadosListDistritoRcd distritoDto = distritoService.findById(id);
        return ResponseEntity.ok().body(distritoDto);
    }

}


