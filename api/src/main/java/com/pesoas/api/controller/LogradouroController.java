package com.pesoas.api.controller;

import com.pesoas.api.DTO.logradouros.DadosListLogradouroRcd;
import com.pesoas.api.DTO.logradouros.LogradouroPesquisaRcd;
import com.pesoas.api.filter.enderecos.LogradouroFilter;
import com.pesoas.api.service.LogradouroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logradouro")
public class LogradouroController {
    @Autowired private LogradouroService logradouroService;

    // Listar de Logradouros
    @GetMapping
    public Page<DadosListLogradouroRcd> findall(@PageableDefault(sort={"nome"}) Pageable paginacao) {
        return logradouroService.findAllPaginated(paginacao);
    }

    // Lista de Logradouros com filter
    @GetMapping("/list")
    public List<DadosListLogradouroRcd> pesquisar(LogradouroFilter filter) {
        return logradouroService.pesquisarSemPaginacao(filter);
    }

    // Lista deLogradouro com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListLogradouroRcd> pesquisar(LogradouroFilter filter, Pageable pageable) {
        return logradouroService.pesquisarComPaginacao(filter, pageable);
    }

    // Logradouro por id
    @GetMapping(value = "/{id}")
    public ResponseEntity<DadosListLogradouroRcd> findById(@PathVariable Long id) {
        DadosListLogradouroRcd logradouroDto = logradouroService.findById(id);
        return ResponseEntity.ok().body(logradouroDto);
    }
    /*@GetMapping("/logradouroPesquisa")
    public ResponseEntity<List<LogradouroPesquisaRcd>> logradouroPesquisa(
            @RequestParam(name = "cidadeNome", required = false) String cidadeNome,
            @RequestParam(name = "tipoLogradouroId", required = false) Long tipoLogradouroId,
            @RequestParam(name = "nome", required = false) String nome) {

        List<LogradouroPesquisaRcd> listaResultados = logradouroService.logradouroPesquisa(
                cidadeNome,tipoLogradouroId,nome);
        return ResponseEntity.ok(listaResultados);
    }*/

    @GetMapping("/por-cidade-tipo-nome") // Nome de endpoint mais descritivo
    public ResponseEntity<List<LogradouroPesquisaRcd>> logradouroPesquisaPorId(
            @RequestParam Long cidadeId,
            @RequestParam Long tipoLogradouroId,
            @RequestParam String nome,
            @RequestParam String cidadeNome
    ) {
        // Chama o método do serviço correspondente à busca por ID
        List<LogradouroPesquisaRcd> resultados = logradouroService.logradouroPesquisaPorId(
                cidadeId,
                tipoLogradouroId,
                nome, cidadeNome
        );

        // Retorna a lista de resultados com status HTTP 200 (OK)
        return ResponseEntity.ok(resultados);
    }
}
