package com.pesoas.api.repository.logradouros.custon;

import com.pesoas.api.DTO.logradouros.LogradouroPesquisaRcd;
import com.pesoas.api.filter.enderecos.LogradouroFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogradouroByCidadeIdTipoNome {
    @PersistenceContext
    private EntityManager manager;

    public List<LogradouroPesquisaRcd> logradouroPesquisaPorId(LogradouroFilter filter) {
        //System.err.println("FILTER " + filter.getDataNascimento());

        Long tipoLogradouroId = filter.getTipoLogradouroId();
        Long cidadeId         = filter.getCidadeId();
        String nomeFilter     = filter.getNome();
        String cidadeNome       = filter.getCidadeNome();

        String queryStr =
            "SELECT new com.pesoas.api.DTO.logradouros.LogradouroPesquisaRcd(\n" +
            " l.id,\n" +
            " l.tipoLogradouro.sigla,\n" +
            " l.nome,\n" +
            " l.distrito.cidade.nome,\n" +
            " l.distrito.cidade.estado.uf,\n" +
            // Usando subselects que são mais portáveis que LIMIT 1, mas LIMIT 1 funciona no Hibernate
            "(SELECT c2.id FROM Cep c2 WHERE c2.logradouro.id = l.id ORDER BY c2.id ASC LIMIT 1),\n" +
            " (SELECT c2.cep FROM Cep c2 WHERE c2.logradouro.id = l.id ORDER BY c2.id ASC LIMIT 1)\n" +
            " )\n" +
            " FROM Logradouro l\n" +
            // Cláusula WHERE para buscar pelos IDs
            " WHERE l.tipoLogradouro.id = :tipoLogradouroId\n" +
            " AND l.distrito.cidade.id = (:cidadeId)\n" +
            // E pelo nome
            " AND UPPER(l.nome) LIKE UPPER(CONCAT('%', :nome, '%'))\n" +
            " ORDER BY l.nome ASC";

        TypedQuery<LogradouroPesquisaRcd> query = manager.createQuery(queryStr, LogradouroPesquisaRcd.class);

        query.setParameter("nome", "%" + nomeFilter + "%");
        query.setParameter("tipoLogradouroId", tipoLogradouroId);
        query.setParameter("cidadeId", cidadeId);

        //List<LogradouroPesquisaRcd> logradouroPesquisa = query.getResultList();

        return query.getResultList();
    }
}
