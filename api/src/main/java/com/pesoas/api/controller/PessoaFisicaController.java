package com.pesoas.api.controller;

import com.pesoas.api.DTO.pessoas.*;
import com.pesoas.api.filter.pessoas.PessoaFisicaFilter;
import com.pesoas.api.repository.PessoaFisicaRepository;
import com.pesoas.api.service.PessoaFisicaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/pessoaFisica")
public class PessoaFisicaController {
    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    // Listar pessoas
    /*@GetMapping
    public Page<DadosPessoasRcd> findall(@PageableDefault(sort = {"nome"}) Pageable paginacao) {
        return pessoasService.findall(paginacao);
    }*/

    @GetMapping
    @Transactional(readOnly = true)
    public Page<DadosListPessoaFisicaRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return pessoaFisicaRepository.findAll(paginacao).map(DadosListPessoaFisicaRcd::fromPessoaFisica);
    }
    @GetMapping("/filter")
    @Transactional(readOnly = true)
    public Page<DadosListPessoaFisicaRcd> pesquisar(PessoaFisicaFilter filter, Pageable pageable) {
        return pessoaFisicaService.pesquisar(filter, pageable);
    }

    /*
        Este metodo filtra/traz apenas pessoas que não pertencem a nenhuma equipe.
        ele esta sendo usado na tela de cadastro de atletas, onde um mesmo atleta não pode pertencer a mais de uma equipe.
     */
    @GetMapping("/pessoaFisicaNotInEquipes")
    @Transactional(readOnly = true)
    public Page<DadosPessoaFisicaReduzRcd> pesquisarByPessoa(PessoaFisicaFilter filter, Pageable pageable) {
        return pessoaFisicaService.pessoaFisicaNotInEquipes(filter, pageable);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<DadosListPessoaFisicaRcd> findById(@PathVariable Long id) {
        DadosListPessoaFisicaRcd dados = pessoaFisicaService.findById(id);
        return dados != null
                ? ResponseEntity.ok(dados)
                : ResponseEntity.notFound().build();
    }

    /*@GetMapping
    public ResponseEntity<List<DadosPessoaReduzidoRcd>> findByIds(
            @RequestParam("ids") Set<Long> ids) { // Recebe os IDs como um Set

        // Chama um método no seu serviço que busca uma lista de pessoas por uma lista de IDs
        List<DadosPessoaReduzidoRcd> pessoasEncontradas = pessoaService.findByIds(ids);

        return ResponseEntity.ok(pessoasEncontradas);
    }*/

    //Insert
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertPessoaFisicaRcd dados){
        var pessoaSalva = pessoaFisicaService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(pessoaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(DadosListPessoaFisicaRcd.fromPessoaFisica(pessoaSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdatePessoaFisicaRcd dados){
        var salva = pessoaFisicaService.update(id, dados);
        return ResponseEntity.ok().body(DadosListPessoaFisicaRcd.fromPessoaFisica(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        pessoaFisicaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


