package com.pesoas.api.controller;

import com.pesoas.api.DTO.estados.DadosListEstadoRcd;
import com.pesoas.api.filter.enderecos.EstadoFilter;
import com.pesoas.api.repository.EstadoRepository;
import com.pesoas.api.service.EstadoService;
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
@RequestMapping("/estado")
public class EstadoController {
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired private EstadoService estadoService;

    // Listar estados
    @GetMapping
    public Page<DadosListEstadoRcd> findall(@PageableDefault(sort={"nome"}) Pageable paginacao) {
        return estadoService.findAllPaginated(paginacao);
    }
    @GetMapping("/list")
    public List<DadosListEstadoRcd> pesquisar(EstadoFilter filter) {
        return estadoService.pesquisarSemPaginacao(filter);
    }

    // Lista de estados com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListEstadoRcd> pesquisar(EstadoFilter filter, Pageable pageable) {
        return estadoService.pesquisarComPaginacao(filter, pageable);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DadosListEstadoRcd> findById(@PathVariable Long id) { // Retornar ResponseEntity<DadosListEstadoRcd>
        DadosListEstadoRcd estadoDto = estadoService.findById(id);
        return ResponseEntity.ok().body(estadoDto);
    }

}


