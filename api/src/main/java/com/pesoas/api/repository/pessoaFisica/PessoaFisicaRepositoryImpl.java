package com.pesoas.api.repository.pessoaFisica;

import com.pesoas.api.entity.PessoaFisica;
import com.pesoas.api.entity.PessoaFisica_;
import com.pesoas.api.filter.pessoas.PessoaFisicaFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PessoaFisicaRepositoryImpl implements PessoaFisicaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<PessoaFisica> filtrar(PessoaFisicaFilter filter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PessoaFisica> criteria = builder.createQuery(PessoaFisica.class);
        Root<PessoaFisica> root = criteria.from(PessoaFisica.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<PessoaFisica> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(filter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<PessoaFisica> filtrar(PessoaFisicaFilter pessoaFisicaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PessoaFisica> criteria = builder.createQuery(PessoaFisica.class);
        Root<PessoaFisica> root = criteria.from(PessoaFisica.class);

        Predicate[] predicates = criarRestricoes(pessoaFisicaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<PessoaFisica> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            PessoaFisicaFilter pessoaFisicaFilter, CriteriaBuilder builder, Root<PessoaFisica> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(pessoaFisicaFilter.getId() != null) {
            predicates.add(builder.equal(root.get(PessoaFisica_.ID), pessoaFisicaFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(pessoaFisicaFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.upper(root.get(PessoaFisica_.NOME)),
                            "%" + pessoaFisicaFilter.getNome().toUpperCase() + "%"));
        }

        // CPF
        if (StringUtils.hasLength(pessoaFisicaFilter.getCpf())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(PessoaFisica_.CPF)),
                            "%" + pessoaFisicaFilter.getCpf() + "%"));
        }

        //DATA NASCIMENTO
        if(pessoaFisicaFilter.getDataNascimento() != null){
            // Deve vir do frontEnd no formato MM/dd/yyyy

            Date data = pessoaFisicaFilter.getDataNascimento();

            Date startDate = DateUtils.truncate(data, Calendar.DATE);
            Date endDate = DateUtils.addSeconds(DateUtils.addDays(startDate, 1), -1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Note o formato
            LocalDate startDateLocalDate = Instant.ofEpochMilli(startDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate endDateLocalDate = Instant.ofEpochMilli(endDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            endDateLocalDate = startDateLocalDate.plusDays(0);

            System.err.println("Datas " + startDateLocalDate + " " + endDateLocalDate);

            predicates.add(
                    builder.between(root.get(PessoaFisica_.DATA_NASCIMENTO), startDateLocalDate, endDateLocalDate));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }
    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(PessoaFisicaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<PessoaFisica> root = criteria.from(PessoaFisica.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
