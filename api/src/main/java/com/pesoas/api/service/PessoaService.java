package com.pesoas.api.service;

import com.pesoas.api.DTO.pessoas.fj.DadosListPessoaGeralRcd;
import com.pesoas.api.DTO.pessoas.fj.DadosPessoaFjReduzidoRcd;
import com.pesoas.api.filter.pessoas.PessoaFilter;
import com.pesoas.api.repository.PessoaRepository;
import com.pesoas.api.entity.Pessoa;
import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.PessoaJuridica;
import com.pesoas.api.service.exceptions.DatabaseException;
import com.pesoas.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.function.Function;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PessoaService {
    @Autowired private PessoaRepository pessoaRepository;

    //////////////////////////////////////////////////

    /*@Transactional(readOnly = true)
    public DadosListPessoaGeralRcd findById(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa não encontrada com o ID: " + id));

        return DadosListPessoaGeralRcd.fromPessoa(pessoa);
    }*/

    @Transactional(readOnly = true)
    public DadosPessoaFjReduzidoRcd findByIdReduzido(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa não encontrada com o ID: " + id));

        return DadosPessoaFjReduzidoRcd.fromPessoa(pessoa);
    }

    @Transactional(readOnly = true)
    public DadosListPessoaGeralRcd findByIdCompleto(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa não encontrada com o ID: " + id));

        return DadosListPessoaGeralRcd.fromPessoa(pessoa);
    }

    //////////////////////////////////////////////////////


    // Este método será executado pela sua PessoaRepositoryImpl original
    @Transactional(readOnly = true)
    public Page<DadosListPessoaGeralRcd> buscarComFiltro(PessoaFilter filter, Pageable pageable) {
        Page<Pessoa> paginaDePessoas = pessoaRepository.filtrar(filter, pageable);

        return paginaDePessoas.map(DadosListPessoaGeralRcd::fromPessoa);
    }

    // Este novo método será executado pela nova PessoaAutocompleteRepositoryImpl
    @Transactional(readOnly = true)
    public List<DadosPessoaFjReduzidoRcd> pesquisarPorNomeCpfCnpj(String termo) {
        // Chama o método genérico passando a função de mapeamento para o DTO Reduzido
        return pesquisarPorTermoETransformar(termo, DadosPessoaFjReduzidoRcd::fromPessoa);
    }

    /**
     * Busca pessoas por termo e retorna um DTO geral.
     */
    @Transactional(readOnly = true)
    public List<DadosListPessoaGeralRcd> pesquisarPorNomeCpfCnpjAll(String termo) {
        // Chama o método genérico passando a função de mapeamento para o DTO Geral
        return pesquisarPorTermoETransformar(termo, DadosListPessoaGeralRcd::fromPessoa);
    }

    private <T> List<T> pesquisarPorTermoETransformar(String termo, Function<Pessoa, T> mapper) {
        // 1. Validação de negócio
        if (!StringUtils.hasText(termo) || termo.trim().length() < 3) {
            return Collections.emptyList();
        }

        // 2. Chamada ao repositório (a única chamada ao banco de dados)
        List<Pessoa> pessoasEncontradas = pessoaRepository.pesquisarPorNomeCpfCnpj(termo.trim());

        // 3. Mapeamento para o DTO genérico (T) usando a função 'mapper' fornecida
        return pessoasEncontradas.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }


    // Pessoa por id
    @Transactional(readOnly = true)
    public List<DadosPessoaFjReduzidoRcd> findByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. Busca as entidades Pessoa. O JPA/Hibernate cuidará de trazer os dados corretos
        // das tabelas filhas (PessoaFisica ou PessoaJuridica).
        List<Pessoa> pessoasEncontradas = pessoaRepository.findAllById(ids);

        // 2. Mapeia a lista de Pessoas para o DTO.
        return pessoasEncontradas.stream()
                .map(this::mapToDadosPessoasRcd) // Usa um método auxiliar para o mapeamento
                .collect(Collectors.toList());
    }
    private DadosPessoaFjReduzidoRcd mapToDadosPessoasRcd(Pessoa pessoa) {
        if (pessoa instanceof PessoaFisica) {
            PessoaFisica pf = (PessoaFisica) pessoa;
            // Lógica para mapear PessoaFisica para o DTO
            return new DadosPessoaFjReduzidoRcd(pf.getId(), pf.getNome(), "F", pf.getCpf(), pf.getDataNascimento(), null /* CNPJ */,pf.getCpf());
        } else if (pessoa instanceof PessoaJuridica) {
            PessoaJuridica pj = (PessoaJuridica) pessoa;
            // Lógica para mapear PessoaJuridica para o DTO
            return new DadosPessoaFjReduzidoRcd(pj.getId(), pj.getNome(), "J", null /* CPF */, null, pj.getCnpj(),pj.getCnpj());
        } else {
            // Caso base (Pessoa abstrata, não deveria acontecer se a busca retornar algo)
            // ou para um novo tipo de pessoa que você venha a criar.
            return new DadosPessoaFjReduzidoRcd(pessoa.getId(), pessoa.getNome(), "?", null, null, null,null);
        }
    }

    // Delete
    public void delete(Long id){
        Pessoa pessoaFisicaDel = pessoaRepository.findById(id)
                .orElseThrow(() -> new com.pesoas.api.service.exceptions.ObjectNotFoundException("Pessoa não cadastrada. Id: " + id));
        try {
            pessoaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new com.pesoas.api.service.exceptions.ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}