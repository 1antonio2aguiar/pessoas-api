package com.pesoas.api.service;

import com.pesoas.api.repository.TiposPessoasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoPessoaService {
    @Autowired private TiposPessoasRepository tiposPessoasRepository;

}