package com.pesoas.api.controller;

import com.pesoas.api.DTO.ceps.DadosListCepsRcd;
import com.pesoas.api.DTO.logradouros.DadosListLogradouroRcd;
import com.pesoas.api.filter.enderecos.CepFilter;
import com.pesoas.api.filter.enderecos.LogradouroFilter;
import com.pesoas.api.service.CepService;
import com.pesoas.api.service.LogradouroService;
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
@RequestMapping("/cep")
public class CepController {
    @Autowired private CepService cepService;

    // Listar de ceps
    @GetMapping
    public Page<DadosListCepsRcd> findall(@PageableDefault(sort={"cep"}) Pageable paginacao) {
        return cepService.findAllPaginated(paginacao);
    }

    // Lista de Ceps com filter
    @GetMapping("/list")
    public List<DadosListCepsRcd> pesquisar(CepFilter filter) {
        return cepService.pesquisarSemPaginacao(filter);
    }

    // Lista de ceps com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListCepsRcd> pesquisar(CepFilter filter, Pageable pageable) {
        return cepService.pesquisarComPaginacao(filter, pageable);
    }

    // cep por id
    @GetMapping(value = "/id/{id}")
    public ResponseEntity<DadosListCepsRcd> findById(@PathVariable Long id) {
        DadosListCepsRcd cepDto = cepService.findById(id);
        return ResponseEntity.ok().body(cepDto);
    }

    // cep por cep
    @GetMapping(value = "/numero/{cep}")
    public ResponseEntity<DadosListCepsRcd> findByCep(@PathVariable String cep) {
        DadosListCepsRcd cepDto = cepService.findByCep(cep);
        return ResponseEntity.ok().body(cepDto);
    }

}


