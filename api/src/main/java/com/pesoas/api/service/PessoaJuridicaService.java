package com.pesoas.api.service;

import com.pesoas.api.DTO.pessoas.DadosPessoaJuridicaRcd;
import com.pesoas.api.entity.PessoaJuridica;
import com.pesoas.api.entity.TiposPessoas;
import com.pesoas.api.entity.enuns.Situacao;
import com.pesoas.api.repository.PessoaJuridicaRepository;
import com.pesoas.api.repository.TiposPessoasRepository;
import com.pesoas.api.service.exceptions.DatabaseException;
import com.pesoas.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaJuridicaService {
    @Autowired private PessoaJuridicaRepository pessoaJuridicaRepository;
    @Autowired private TiposPessoasRepository tiposPessoasRepository;

    //Insert
    public PessoaJuridica insert(DadosPessoaJuridicaRcd dados){
        PessoaJuridica pessoaJuridica = new PessoaJuridica();

        BeanUtils.copyProperties(dados, pessoaJuridica, "id");

        //Busco tipo pessoa
        TiposPessoas tiposPessoas = tiposPessoasRepository.findById(dados.tipoPessoaId()).get();
        pessoaJuridica.setTiposPessoas(tiposPessoas);

        pessoaJuridica.setSituacao(Situacao.toSituacaoEnum(dados.situacao()));

        return pessoaJuridicaRepository.save(pessoaJuridica);
    }

    //Update
    public PessoaJuridica update(Long id, DadosPessoaJuridicaRcd dados){
        PessoaJuridica pjUpd = pessoaJuridicaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa não cadastrada. Id: " + id));

        BeanUtils.copyProperties(dados, pjUpd, "id");
        //Busco tipo pessoa
        TiposPessoas tiposPessoas = tiposPessoasRepository.findById(dados.tipoPessoaId()).get();
        pjUpd.setTiposPessoas(tiposPessoas);

        pjUpd.setSituacao(Situacao.toSituacaoEnum(dados.situacao()));

        return pessoaJuridicaRepository.save(pjUpd);
    }
}