package com.pesoas.api.service;

import com.pesoas.api.DTO.bairros.BairroInfoRcd;
import com.pesoas.api.DTO.logradouros.DadosListLogradouroRcd;
import com.pesoas.api.DTO.logradouros.LogradouroPesquisaRcd;
import com.pesoas.api.entity.Logradouro;
import com.pesoas.api.filter.enderecos.LogradouroFilter;
import com.pesoas.api.repository.LogradouroRepository;
import com.pesoas.api.repository.logradouros.custon.LogradouroByCidadeIdTipoNome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogradouroService {
    @Autowired private LogradouroRepository logradouroRepository;
    @Autowired private LogradouroByCidadeIdTipoNome logradouroByCidadeIdTipoNome;

    @Transactional(readOnly = true)
    public Page<DadosListLogradouroRcd> findAllPaginated(Pageable paginacao) {
        Page<Logradouro> logradouroPage = logradouroRepository.findAll(paginacao);
        return logradouroPage.map(DadosListLogradouroRcd::new);
    }

    @Transactional(readOnly = true)
    public List<DadosListLogradouroRcd> pesquisarSemPaginacao(LogradouroFilter filter) {
        List<Logradouro> logradouros = logradouroRepository.filtrar(filter);
        return logradouros.stream()
                .map(DadosListLogradouroRcd::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DadosListLogradouroRcd> pesquisarComPaginacao(LogradouroFilter filter, Pageable pageable) {
        Page<Logradouro> logradouroPage = logradouroRepository.filtrar(filter, pageable);
        return logradouroPage.map(DadosListLogradouroRcd::new);
    }

    @Transactional(readOnly = true) // Importante para operações de leitura
    public DadosListLogradouroRcd findById(Long id) {
        Logradouro logradouro = logradouroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Logradouro não encontrado: " + id));
        return new DadosListLogradouroRcd(logradouro);
    }

    /*@Transactional(readOnly = true)
    public Page<LogradouroPesquisaRcd> logradouroPesquisa(String nome, Long tipoLogradouroId, Pageable pageable) {
        Page<LogradouroPesquisaRcd> paginaDeResultados = logradouroRepository.LogradouroPesquisa(nome,tipoLogradouroId, pageable);
        return paginaDeResultados;
    }*/

    /*@Transactional(readOnly = true)
    public List<LogradouroPesquisaRcd> logradouroPesquisa(String nome, Long tipoLogradouroId, String cidadeNome) {
        List<LogradouroPesquisaRcd> listaDeResultados = logradouroRepository.logradouroPesquisa(
                nome,
                tipoLogradouroId,
                cidadeNome
        );

        // Extrair os IDs dos logradouros da lista inicial.
        List<Long> logradouroIds = listaDeResultados.stream()
                .map(LogradouroPesquisaRcd::id)
                .collect(Collectors.toList());

        if (logradouroIds.isEmpty()) {
            return listaDeResultados; // Retorna a lista como está (sem bairros) se não houver resultados.
        }

        // Fazer uma única query para buscar todos os logradouros com seus bairros associados.
        List<Logradouro> logradourosComBairros = logradouroRepository.findAllByIdWithBairros(logradouroIds);

        // Mapear os bairros encontrados para cada DTO na lista de resultados.
        List<LogradouroPesquisaRcd> listaFinal = listaDeResultados.stream().map(dto -> {
            // Encontra o Logradouro completo correspondente ao DTO atual
            Logradouro logradouroCompleto = logradourosComBairros.stream()
                    .filter(l -> l.getId().equals(dto.id()))
                    .findFirst()
                    .orElse(null);

            // Se encontrou o logradouro completo, cria um novo DTO com a lista de bairros
            if (logradouroCompleto != null && logradouroCompleto.getBairros() != null) {
                List<BairroInfoRcd> bairrosInfo = logradouroCompleto.getBairros().stream()
                        .map(bairro -> new BairroInfoRcd(bairro.getId(), bairro.getNome()))
                        .collect(Collectors.toList());

                // Reconstrói o DTO com a lista de bairros populada.
                // Certifique-se que o construtor de LogradouroPesquisaRcd tem esta assinatura.
                return new LogradouroPesquisaRcd(
                        dto.id(),
                        dto.tipoLogradouro(),
                        dto.logradouroNome(),
                        bairrosInfo, // << A lista de bairros populada
                        dto.cidadeNome(),
                        dto.uf(),
                        dto.cepId(),
                        dto.cep()
                );
            }
            // Se não encontrou o logradouro completo (não deveria acontecer), retorna o DTO original
            return dto;
        }).collect(Collectors.toList());

        return listaFinal;
    }*/

    @Transactional(readOnly = true)
    public List<LogradouroPesquisaRcd> logradouroPesquisaPorId(
            Long cidadeId,
            Long tipoLogradouroId,
            String nome,
            String cidadeNome) {

        LogradouroFilter filter = new LogradouroFilter();
        filter.setNome(nome.trim());
        filter.setTipoLogradouroId(tipoLogradouroId);
        filter.setCidadeId(cidadeId);
        filter.setCidadeNome(cidadeNome);

        // Busca inicial
        List<LogradouroPesquisaRcd> listaDeResultadosBase = logradouroByCidadeIdTipoNome.logradouroPesquisaPorId(filter);

        if (listaDeResultadosBase == null || listaDeResultadosBase.isEmpty()) {
            return Collections.emptyList();
        }

        // Busca os logradouros completos para obter os bairros
        List<Long> logradouroIds = listaDeResultadosBase.stream()
                .map(LogradouroPesquisaRcd::id)
                .collect(Collectors.toList());

        List<Logradouro> logradourosComBairros = logradouroRepository.findAllByIdWithBairros(logradouroIds);

        // Mapeia a lista base, enriquecendo com os bairros ou com o código 3877
        return listaDeResultadosBase.stream().map(dto -> {

            Logradouro logradouroCompleto = logradourosComBairros.stream()
                    .filter(l -> l.getId().equals(dto.id()))
                    .findFirst()
                    .orElse(null);

            List<BairroInfoRcd> bairrosParaDto;

            // Se o logradouro foi encontrado e sua lista de bairros não é vazia
            if (logradouroCompleto != null && logradouroCompleto.getBairros() != null && !logradouroCompleto.getBairros().isEmpty()) {
                // Mapeia os bairros reais para BairroInfoRcd
                bairrosParaDto = logradouroCompleto.getBairros().stream()
                        .map(bairro -> new BairroInfoRcd(bairro.getId(), bairro.getNome()))
                        .collect(Collectors.toList());
            } else {
                bairrosParaDto = Collections.singletonList(
                        new BairroInfoRcd(3877L, "Sem Bairro Associado") // Mensagem descritiva
                );
            }

            // Reconstrói o DTO com a lista de bairros apropriada (real ou fictícia)
            return new LogradouroPesquisaRcd(
                    dto.id(),
                    dto.tipoLogradouro(),
                    dto.logradouroNome(),
                    bairrosParaDto, // << A lista de bairros (real ou com o código 3877)
                    dto.cidadeNome(),
                    dto.uf(),
                    dto.cepId(),
                    dto.cep()
            );

        }).collect(Collectors.toList());
    }

        // Chama o método do repositório customizado injetado
        //return logradouroByCidadeIdTipoNome.logradouroPesquisaPorId(filter);
    //}
}