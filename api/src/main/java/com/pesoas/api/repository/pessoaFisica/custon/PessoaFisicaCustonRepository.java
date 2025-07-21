package com.pesoas.api.repository.pessoaFisica.custon;

import com.pesoas.api.entity.Pessoa;
import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.filter.pessoas.PessoaFisicaFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Repository
public class PessoaFisicaCustonRepository {
    @PersistenceContext
    private EntityManager manager;

    public Page<PessoaFisica> pessoaFisicaNotInEquipes(PessoaFisicaFilter filter, Pageable pageable) {
        //System.err.println("FILTER " + filter.getDataNascimento());

        String nomeFilter = filter.getNome();
        String cpfFilter = filter.getCpf();

        Date dataNascimentoFilter = filter.getDataNascimento();
        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        String queryStr = "SELECT pf FROM PessoaFisica pf WHERE NOT EXISTS (SELECT 1 FROM Atleta a WHERE a.pessoa.id = pf.id)";
        String countQueryStr = "SELECT COUNT(pf) FROM PessoaFisica pf WHERE NOT EXISTS (SELECT 1 FROM Atleta a WHERE a.pessoa.id = pf.id)";

        if (nomeFilter != null && !nomeFilter.isEmpty()) {
            queryStr += " AND UPPER(pf.nome) LIKE UPPER(:nome)";
            countQueryStr += " AND UPPER(pf.nome) LIKE UPPER(:nome)";
        }

        if (cpfFilter != null && !cpfFilter.isEmpty()) {
            queryStr += " AND UPPER(pf.cpf) LIKE UPPER(:cpf)"; // Correção: dadosPessoaFisica.cpf
            countQueryStr += " AND UPPER(pf.cpf) LIKE UPPER(:cpf)"; // Correção: dadosPessoaFisica.cpf
        }

        if (dataNascimentoFilter != null) {
            // Define o formato da data recebida do frontend
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            // Converte a data para LocalDate
            dataInicio = Instant.ofEpochMilli(dataNascimentoFilter.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            dataFim = dataInicio.plusDays(1);

            queryStr += " AND pf.dataNascimento BETWEEN :dataInicio AND :dataFim";
            countQueryStr += " AND pf.dataNascimento BETWEEN :dataInicio AND :dataFim";
        }

        Query query = manager.createQuery(queryStr, Pessoa.class);
        Query countQuery = manager.createQuery(countQueryStr);

        if (nomeFilter != null && !nomeFilter.isEmpty()) {
            query.setParameter("nome", "%" + nomeFilter.toUpperCase() + "%");
            countQuery.setParameter("nome", "%" + nomeFilter.toUpperCase() + "%");
        }

        if (cpfFilter != null && !cpfFilter.isEmpty()) {
            query.setParameter("cpf", "%" + cpfFilter.toUpperCase() + "%");
            countQuery.setParameter("cpf", "%" + cpfFilter.toUpperCase() + "%");
        }

        if (dataNascimentoFilter != null) {
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            countQuery.setParameter("dataInicio", dataInicio);
            countQuery.setParameter("dataFim", dataFim);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<PessoaFisica> pessoa = query.getResultList();
        Long total = (Long) countQuery.getSingleResult();

        return new PageImpl<>(pessoa, pageable, total);
    }
}
