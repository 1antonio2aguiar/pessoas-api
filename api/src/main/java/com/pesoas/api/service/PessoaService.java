package com.pesoas.api.service;

import com.pesoas.api.DTO.pessoas.fj.DadosListPessoaGeralRcd;
import com.pesoas.api.DTO.pessoas.fj.DadosPessoaFjReduzidoRcd;
import com.pesoas.api.filter.pessoas.PessoaFilter;
import com.pesoas.api.repository.PessoaRepository;
import com.pesoas.api.entity.Pessoa;
import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.PessoaJuridica;
import com.pesoas.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PessoaService {
    @Autowired private PessoaRepository pessoaRepository;

    // Este método será executado pela sua PessoaRepositoryImpl original
    @Transactional(readOnly = true)
    public Page<DadosListPessoaGeralRcd> buscarComFiltro(PessoaFilter filter, Pageable pageable) {
        Page<Pessoa> paginaDePessoas = pessoaRepository.filtrar(filter, pageable);

        return paginaDePessoas.map(DadosListPessoaGeralRcd::fromPessoa);
    }

    // Este novo método será executado pela nova PessoaAutocompleteRepositoryImpl
    @Transactional(readOnly = true)
    public List<DadosPessoaFjReduzidoRcd> pesquisarPorNomeCpfCnpj(String termo) {

        // Regra de negócio: não buscar se o termo for muito curto.
        // Sua implementação no repositório já faz uma verificação, mas é bom ter aqui também.
        if (!StringUtils.hasText(termo) || termo.trim().length() < 3) {
            return Collections.emptyList();
        }

        // Chama o método do repositório que você implementou com Criteria API.
        // A lógica de remover caracteres não numéricos e limitar os resultados já está lá.
        List<Pessoa> pessoasEncontradas = pessoaRepository.pesquisarPorNomeCpfCnpj(termo.trim());

        // Converte a lista de entidades para uma lista de DTOs
        return pessoasEncontradas.stream()
                .map(DadosPessoaFjReduzidoRcd::fromPessoa)
                .collect(Collectors.toList());
    }

    // Pessoa por id
    @Transactional(readOnly = true)
    public DadosPessoaFjReduzidoRcd findById(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa não encontrada com o ID: " + id));

        return DadosPessoaFjReduzidoRcd.fromPessoa(pessoa);
    }
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
            return new DadosPessoaFjReduzidoRcd(pf.getId(), pf.getNome(), "F", pf.getCpf(), pf.getDataNascimento(), null /* CNPJ */);
        } else if (pessoa instanceof PessoaJuridica) {
            PessoaJuridica pj = (PessoaJuridica) pessoa;
            // Lógica para mapear PessoaJuridica para o DTO
            return new DadosPessoaFjReduzidoRcd(pj.getId(), pj.getNome(), "J", null /* CPF */, null, pj.getCnpj());
        } else {
            // Caso base (Pessoa abstrata, não deveria acontecer se a busca retornar algo)
            // ou para um novo tipo de pessoa que você venha a criar.
            return new DadosPessoaFjReduzidoRcd(pessoa.getId(), pessoa.getNome(), "?", null, null, null);
        }
    }




}