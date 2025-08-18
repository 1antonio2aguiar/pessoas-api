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
    @Autowired private PessoaRepository pessoaRepository;
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
        if (!pessoaRepository.existsById(pessoaId)) {
            throw new ObjectNotFoundException("Pessoa com ID " + pessoaId + " não encontrada.");
        }
        // Usa o método corrigido do EnderecoRepository
        List<Endereco> enderecos = enderecoRepository.findByPessoaId(pessoaId);
        return enderecos.stream()
                .map(DadosListEnderecoRcd::new)
                .collect(Collectors.toList());
    }

    //Insert
    public Endereco insert(DadosInsertEnderecoRcd dados){
        System.err.println("O que esta chagando aqui " + dados);
        Endereco endereco = new Endereco();
        BeanUtils.copyProperties(dados, endereco, "id");

        //Busco a pessoa
        Pessoa pessoa = pessoaRepository.findById(dados.pessoaId())
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa com ID " + dados.pessoaId() + " não encontrada."));
        endereco.setPessoa(pessoa);

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
        // A validação da existência da pessoa já está implícita na busca

        // Esta chamada agora usa a query corrigida que opera em 'pessoa.id'
        enderecoRepository.marcarTodosComoNaoPrincipalParaPessoa(pessoaId);

        Endereco enderecoParaAtualizar = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ObjectNotFoundException("Endereço com ID " + enderecoId + " não encontrado."));

        // A validação de posse agora é mais simples e robusta
        if (!enderecoParaAtualizar.getPessoa().getId().equals(pessoaId)) {
            throw new IllegalArgumentException("O endereço não pertence à pessoa especificada.");
        }

        enderecoParaAtualizar.setPrincipal("S");
        enderecoRepository.save(enderecoParaAtualizar);
    }
}