package com.pesoas.api.service;

import com.pesoas.api.DTO.contatos.DadosInsertContatoRcd;
import com.pesoas.api.DTO.contatos.DadosListContatoRcd;
import com.pesoas.api.DTO.contatos.DadosUpdateContatoRcd;
import com.pesoas.api.entity.Contato;
import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.enuns.TipoContato;
import com.pesoas.api.filter.enderecos.ContatoFilter;
import com.pesoas.api.repository.*;
import com.pesoas.api.service.exceptions.DatabaseException;
import com.pesoas.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContatoService {
    @Autowired private ContatoRepository contatoRepository;
    @Autowired private PessoaFisicaRepository pessoaFisicaRepository;

    @Transactional(readOnly = true)
    public Page<DadosListContatoRcd> findAllPaginated(Pageable paginacao) {
        Page<Contato> contatoPage = contatoRepository.findAll(paginacao);
        return contatoPage.map(DadosListContatoRcd::new);
    }

    @Transactional(readOnly = true)
    public List<DadosListContatoRcd> pesquisarSemPaginacao(ContatoFilter filter) {
        List<Contato> contatos = contatoRepository.filtrar(filter);
        return contatos.stream()
                .map(DadosListContatoRcd::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DadosListContatoRcd> pesquisarComPaginacao(ContatoFilter filter, Pageable pageable) {
        Page<Contato> contatoPage = contatoRepository.filtrar(filter, pageable);
        return contatoPage.map(DadosListContatoRcd::new);
    }

    @Transactional(readOnly = true) // Importante para operações de leitura
    public DadosListContatoRcd findById(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado: " + id));
        return new DadosListContatoRcd(contato);
    }

    @Transactional(readOnly = true)
    public List<DadosListContatoRcd> findContatosByPessoaId(Long pessoaId) {
        // Verificar se a pessoa existe
        if (!pessoaFisicaRepository.existsById(pessoaId)) {
            throw new ObjectNotFoundException("Pessoa com ID " + pessoaId + " não encontrada.");
        }
        List<Contato> contatos = contatoRepository.findByPessoaFisicaId(pessoaId);
        return contatos.stream()
                .map(DadosListContatoRcd::new)
                .collect(Collectors.toList());
    }

    //Insert
    public Contato insert(DadosInsertContatoRcd dados){
        Contato contato = new Contato();
        BeanUtils.copyProperties(dados, contato, "id");

        //Busco a pessoa
        PessoaFisica pessoaFisica = pessoaFisicaRepository.findById(dados.pessoaId())
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa com ID " + dados.pessoaId() + " não encontrada."));
        contato.setPessoaFisica(pessoaFisica);


        // set o enum
        contato.setTipoContato(TipoContato.toTipoContatoEnum(dados.tipoContato()));

        Contato contatoInsert = contatoRepository.save(contato);
        return contatoInsert;
    }

    // update
    public Contato update(Long id, DadosUpdateContatoRcd dados){
        
        Contato contatoUpd = contatoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Contato não cadastrado. Id: " + id));
        BeanUtils.copyProperties(dados, contatoUpd, "id");


        // set o enum
        contatoUpd.setTipoContato(TipoContato.toTipoContatoEnum(dados.tipoContato()));

        return contatoRepository.save(contatoUpd);
    }

    // Delete
    public void delete(Long id){
        Contato contatoDel = contatoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Contato não cadastrado. Id: " + id));
        try {
            contatoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public void definirContatoComoPrincipal(Long pessoaId, Long contatoId) {
        int registrosAtualizados = contatoRepository.marcarTodosComoNaoPrincipalParaPessoa(pessoaId);

        Optional<Contato> contatoOpt = contatoRepository.findById(contatoId);
        if (contatoOpt.isPresent()) {
            Contato contatoParaAtualizar = contatoOpt.get();
            if (!contatoParaAtualizar.getPessoaFisica().getId().equals(pessoaId)) { // Ajuste conforme sua entidade
                throw new IllegalArgumentException("O contato não pertence à pessoa especificada.");
            }
            contatoParaAtualizar.setPrincipal("S"); // Ou true
            contatoRepository.save(contatoParaAtualizar);
        }else {
            throw new ObjectNotFoundException("Contato com ID " + contatoId + " não encontrado.");
        }
    }
}