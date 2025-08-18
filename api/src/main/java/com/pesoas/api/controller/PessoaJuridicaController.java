package com.pesoas.api.controller;

import com.pesoas.api.DTO.pessoas.*;
import com.pesoas.api.DTO.pessoas.DadosPessoaJuridicaRcd;
import com.pesoas.api.service.PessoaJuridicaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pessoaJuridica")
public class PessoaJuridicaController {

    @Autowired
    private PessoaJuridicaService pessoaJuridicaService;

    //Insert
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosPessoaJuridicaRcd dados){
        var pessoaSalva = pessoaJuridicaService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(pessoaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(DadosListPessoaJuridicaRcd.fromPessoaJuridica(pessoaSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosPessoaJuridicaRcd dados){
        var salva = pessoaJuridicaService.update(id, dados);
        return ResponseEntity.ok().body(DadosListPessoaJuridicaRcd.fromPessoaJuridica(salva));
    }

}


