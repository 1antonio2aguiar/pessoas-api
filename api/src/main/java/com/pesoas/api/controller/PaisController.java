package com.pesoas.api.controller;

import com.pesoas.api.DTO.paises.DadosListPaisRcd;
import com.pesoas.api.entity.Pais;
import com.pesoas.api.filter.enderecos.PaisFilter;
import com.pesoas.api.repository.PaisRepository;
import com.pesoas.api.service.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pais")
public class PaisController {
    @Autowired
    private PaisRepository paisRepository;
    @Autowired private PaisService paisService;

    // Listar paises
    @GetMapping
    public Page<DadosListPaisRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return paisRepository.findAll(paginacao).map(DadosListPaisRcd::new);
    }
    @GetMapping("/list")
    public List<DadosListPaisRcd> pesquisar(PaisFilter filter) {
        List<Pais> paises = paisRepository.filtrar(filter);

        return paises.stream()
                .map(DadosListPaisRcd::new)
                .collect(Collectors.toList());
    }

    // Lista de paises com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListPaisRcd> pesquisar(PaisFilter filter, Pageable pageable) {
        Page<Pais> paisPage = paisRepository.filtrar(filter, pageable);
        Page<DadosListPaisRcd> dadosListPaisPage = paisPage.map(DadosListPaisRcd::new);

        return dadosListPaisPage;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        Pais pais = paisService.findById(id);
        return ResponseEntity.ok().body(new DadosListPaisRcd(pais));
    }

}


