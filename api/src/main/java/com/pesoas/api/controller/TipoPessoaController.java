package com.pesoas.api.controller;

import com.pesoas.api.entity.TiposPessoas;
import com.pesoas.api.filter.pessoas.TiposPessoasFilter;
import com.pesoas.api.repository.TiposPessoasRepository;
import com.pesoas.api.service.TipoPessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipoPessoa")
public class TipoPessoaController {
    @Autowired
    private TiposPessoasRepository tiposPessoasRepository;
    @Autowired
    private TipoPessoaService tipoPessoaService;

    // Listar pessoas
    // Listar tipos modalidade
    @GetMapping
    public List<TiposPessoas> pesquisar(TiposPessoasFilter filter ) {
        return tiposPessoasRepository.filtrar(filter);
    }

}


