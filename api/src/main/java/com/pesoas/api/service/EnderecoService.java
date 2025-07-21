package com.pesoas.api.service;

import com.pesoas.api.DTO.enderrecos.DadosInsertEnderecoRcd;
import com.pesoas.api.DTO.enderrecos.DadosListEnderecoRcd;
import com.pesoas.api.DTO.enderrecos.DadosUpdateEnderecoRcd;
import com.pesoas.api.entity.*;
import com.pesoas.api.entity.enuns.TipoEndereco;
import com.pesoas.api.filter.enderecos.EnderecoFilter;
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
public class EnderecoService {
    @Autowired private EnderecoRepository enderecoRepository;
    @Autowired private CepRepository cepRepository;
    @Autowired private PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired private LogradouroRepository logradouroRepository;
    @Autowired private BairroRepository bairroRepository;

    @Transactional(readOnly = true)
    public Page<DadosListEnderecoRcd> findAllPaginated(Pageable paginacao) {
        Page<Endereco> enderecoPage = enderecoRepository.findAll(paginacao);
        return enderecoPage.map(DadosListEnderecoRcd::new);
    }

    @Transactional(readOnly = true)
    public List<DadosListEnderecoRcd> pesquisarSemPaginacao(EnderecoFilter filter) {
        List<Endereco> enderecos = enderecoRepository.filtrar(filter);
        return enderecos.stream()
                .map(DadosListEnderecoRcd::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DadosListEnderecoRcd> pesquisarComPaginacao(EnderecoFilter filter, Pageable pageable) {
        Page<Endereco> enderecoPage = enderecoRepository.filtrar(filter, pageable);
        return enderecoPage.map(DadosListEnderecoRcd::new);
    }

    @Transactional(readOnly = true) // Importante para operações de leitura
    public DadosListEnderecoRcd findById(Long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereco não encontrado: " + id));
        return new DadosListEnderecoRcd(endereco);
    }

    @Transactional(readOnly = true)
    public List<DadosListEnderecoRcd> findEnderecosByPessoaId(Long pessoaId) {
        // Verificar se a pessoa existe
        if (!pessoaFisicaRepository.existsById(pessoaId)) {
            throw new ObjectNotFoundException("Pessoa com ID " + pessoaId + " não encontrada.");
        }
        List<Endereco> enderecos = enderecoRepository.findByPessoaFisicaId(pessoaId);
        return enderecos.stream()
                .map(DadosListEnderecoRcd::new)
                .collect(Collectors.toList());
    }

    /*@Transactional(readOnly = true)
    public DadosListEnderecoRcd findByCep(String cep) {
        Endereco enderecoEntidade = enderecoRepository.findByCep(cep)
                .orElseThrow(() -> new RuntimeException("Cep não encontrado em enderecos: " + cep));
        return new DadosListEnderecoRcd(enderecoEntidade);
    }*/

    //Insert
    public Endereco insert(DadosInsertEnderecoRcd dados){
        System.err.println("O que esta chagando aqui " + dados);
        Endereco endereco = new Endereco();
        BeanUtils.copyProperties(dados, endereco, "id");

        //Busco a pessoa
        PessoaFisica pessoaFisica = pessoaFisicaRepository.findById(dados.pessoaId())
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa com ID " + dados.pessoaId() + " não encontrada."));
        endereco.setPessoaFisica(pessoaFisica);

        //Busco cep
        Cep cep = cepRepository.findById(dados.cepId())
                .orElseThrow(() -> new ObjectNotFoundException("CEP com ID " + dados.cepId() + " não encontrado."));
        endereco.setCep(cep);

        //Busco logradouro
        Logradouro logradouro = logradouroRepository.findById(dados.logradouroId())
                .orElseThrow(() -> new ObjectNotFoundException("Logradouro com ID " + dados.logradouroId() + " não encontrado."));
        endereco.setLogradouro(logradouro);

        //Busco bairro
        Bairro bairro = bairroRepository.findById(dados.bairroId())
                .orElseThrow(() -> new ObjectNotFoundException("Bairro com ID " + dados.bairroId() + " não encontrado."));
        endereco.setBairro(bairro);

        // set o enum
        endereco.setTipoEndereco(TipoEndereco.toTipoEnderecoEnum(dados.tipoEndereco()));

        Endereco enderecoInsert = enderecoRepository.save(endereco);
        return enderecoInsert;
    }

    // update
    public Endereco update(Long id, DadosUpdateEnderecoRcd dados){
        
        Endereco enderecoUpd = enderecoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Endereço não cadastrado. Id: " + id));
        BeanUtils.copyProperties(dados, enderecoUpd, "id");

        //Busco cep
        Cep cep = cepRepository.findById(dados.cepId())
                .orElseThrow(() -> new ObjectNotFoundException("CEP com ID " + dados.cepId() + " não encontrado."));
        enderecoUpd.setCep(cep);

        //Busco logradouro
        Logradouro logradouro = logradouroRepository.findById(dados.logradouroId())
                .orElseThrow(() -> new ObjectNotFoundException("Logradouro com ID " + dados.logradouroId() + " não encontrado."));
        enderecoUpd.setLogradouro(logradouro);

        //Busco bairro
        Bairro bairro = bairroRepository.findById(dados.bairroId())
                .orElseThrow(() -> new ObjectNotFoundException("Bairro com ID " + dados.bairroId() + " não encontrado."));
        enderecoUpd.setBairro(bairro);

        // set o enum
        enderecoUpd.setTipoEndereco(TipoEndereco.toTipoEnderecoEnum(dados.tipoEndereco()));

        return enderecoRepository.save(enderecoUpd);
    }

    // Delete
    public void delete(Long id){
        Endereco enderecoDel = enderecoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Endereço não cadastrado. Id: " + id));
        try {
            enderecoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public void definirEnderecoComoPrincipal(Long pessoaId, Long enderecoId) {
        int registrosAtualizados = enderecoRepository.marcarTodosComoNaoPrincipalParaPessoa(pessoaId);

        Optional<Endereco> enderecoOpt = enderecoRepository.findById(enderecoId);
        if (enderecoOpt.isPresent()) {
            Endereco enderecoParaAtualizar = enderecoOpt.get();
            if (!enderecoParaAtualizar.getPessoaFisica().getId().equals(pessoaId)) { // Ajuste conforme sua entidade
                throw new IllegalArgumentException("O endereço não pertence à pessoa especificada.");
            }
            enderecoParaAtualizar.setPrincipal("S"); // Ou true
            enderecoRepository.save(enderecoParaAtualizar);
        }else {
            throw new ObjectNotFoundException("Endereço com ID " + enderecoId + " não encontrado.");
        }
    }
}