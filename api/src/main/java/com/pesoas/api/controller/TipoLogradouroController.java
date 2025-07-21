package com.pesoas.api.controller;

import com.pesoas.api.entity.TipoLogradouro;
import com.pesoas.api.repository.TipoLogradouroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tipoLogradouro")
public class TipoLogradouroController {
    @Autowired
    private TipoLogradouroRepository tipoLogradouroRepository;

    // Listar pessoas
    // Listar tipos modalidade
    @GetMapping
    public List<TipoLogradouro> pesquisar() {
        // Cria um objeto de ordenação e o passa para o método findAll.
        return tipoLogradouroRepository.findAll(Sort.by(Sort.Direction.ASC, "descricao"));
    }
}


