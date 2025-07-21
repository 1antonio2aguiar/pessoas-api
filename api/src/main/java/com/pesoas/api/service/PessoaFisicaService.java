package com.pesoas.api.service;

import com.pesoas.api.DTO.pessoas.DadosInsertPessoaFisicaRcd;
import com.pesoas.api.DTO.pessoas.DadosListPessoaFisicaRcd;
import com.pesoas.api.DTO.pessoas.DadosPessoaFisicaReduzRcd;
import com.pesoas.api.DTO.pessoas.DadosUpdatePessoaFisicaRcd;
import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.TiposPessoas;
import com.pesoas.api.entity.enuns.EstadoCivil;
import com.pesoas.api.entity.enuns.Situacao;
import com.pesoas.api.filter.pessoas.PessoaFisicaFilter;
import com.pesoas.api.repository.PessoaFisicaRepository;
import com.pesoas.api.repository.TiposPessoasRepository;
import com.pesoas.api.repository.pessoaFisica.custon.PessoaFisicaCustonRepository;
import com.pesoas.api.service.exceptions.DatabaseException;
import com.pesoas.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PessoaFisicaService {
    @Autowired private PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired private PessoaFisicaCustonRepository pessoaFisicaCustonRepository;
    @Autowired private TiposPessoasRepository tiposPessoasRepository;

    public PessoaFisicaService(PessoaFisicaRepository pessoaFisicaRepository) {
        this.pessoaFisicaRepository = pessoaFisicaRepository;
    }

    //Metodo filtrar
    public Page<DadosListPessoaFisicaRcd> pesquisar(PessoaFisicaFilter filter, Pageable pageable) {
        Page<PessoaFisica> pessoaPage = pessoaFisicaRepository.filtrar(filter, pageable);

        // Mapeia a lista de Pessoas para uma lista de DadosPessoasRcd usando o método de fábrica
        List<DadosListPessoaFisicaRcd> pessoasDTOList = pessoaPage.getContent().stream()
                .map(DadosListPessoaFisicaRcd::fromPessoaFisica)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosPessoasRcd> com os dados mapeados
        return new PageImpl<>(pessoasDTOList, pageable, pessoaPage.getTotalElements());
    }

    public Page<DadosPessoaFisicaReduzRcd> pessoaFisicaNotInEquipes(PessoaFisicaFilter filter, Pageable pageable) {
        // Recupera somente pessoas que não pertecem a nenhuma equipe.
        Page<PessoaFisica> pessoaPage = pessoaFisicaCustonRepository.pessoaFisicaNotInEquipes(filter, pageable);

        // Mapeia a lista de Pessoas para uma lista de DadosPessoasRcd usando o método de fábrica
        List<DadosPessoaFisicaReduzRcd> pessoasDTOList = pessoaPage.getContent().stream()
                .map(DadosPessoaFisicaReduzRcd::fromPessoaFisica)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosPessoasRcd> com os dados mapeados
        return new PageImpl<>(pessoasDTOList, pageable, pessoaPage.getTotalElements());
    }

    // Pessoa fisica por id
    public DadosListPessoaFisicaRcd findById(Long id) {
        Optional<PessoaFisica> pfOptional = pessoaFisicaRepository.findById(id);
        return pfOptional.map(DadosListPessoaFisicaRcd::fromPessoaFisica).orElse(null);
    }

    //Insert
    public PessoaFisica insert(DadosInsertPessoaFisicaRcd dados){
        PessoaFisica pessoaFisica = new PessoaFisica();

        BeanUtils.copyProperties(dados, pessoaFisica, "id");

        //Busco tipo pessoa
        TiposPessoas tiposPessoas = tiposPessoasRepository.findById(dados.tipoPessoaId()).get();
        pessoaFisica.setTiposPessoas(tiposPessoas);

        pessoaFisica.setSituacao(Situacao.toSituacaoEnum(dados.situacao()));
        pessoaFisica.setEstadoCivil(EstadoCivil.toEstadocivilEnum(dados.estadoCivil()));

        return pessoaFisicaRepository.save(pessoaFisica);
    }

    //Update
    public PessoaFisica update(Long id, DadosUpdatePessoaFisicaRcd dados){
        PessoaFisica pfUpd = pessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa não cadastrada. Id: " + id));

        BeanUtils.copyProperties(dados, pfUpd, "id");
        //Busco tipo pessoa
        TiposPessoas tiposPessoas = tiposPessoasRepository.findById(dados.tipoPessoaId()).get();
        pfUpd.setTiposPessoas(tiposPessoas);

        pfUpd.setSituacao(Situacao.toSituacaoEnum(dados.situacao()));
        pfUpd.setEstadoCivil(EstadoCivil.toEstadocivilEnum(dados.estadoCivil()));

        return pessoaFisicaRepository.save(pfUpd);
    }

    // Delete
    public void delete(Long id){
        PessoaFisica pessoaFisicaDel = pessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new com.pesoas.api.service.exceptions.ObjectNotFoundException("Pessoa não cadastrada. Id: " + id));
        try {
            pessoaFisicaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new com.pesoas.api.service.exceptions.ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}