package com.pesoas.api.repository.logradouros;

import com.pesoas.api.entity.Cidade_;
import com.pesoas.api.entity.Logradouro_;
import com.pesoas.api.entity.TipoLogradouro_;
import com.pesoas.api.filter.enderecos.LogradouroFilter;
import com.pesoas.api.entity.Logradouro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LogradouroRepositoryImpl implements LogradouroRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Logradouro> filtrar(LogradouroFilter logradouroFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Logradouro> criteria = builder.createQuery(Logradouro.class);
        Root<Logradouro> root = criteria.from(Logradouro.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(logradouroFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Logradouro> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(logradouroFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Logradouro> filtrar(LogradouroFilter logradouroFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Logradouro> criteria = builder.createQuery(Logradouro.class);
        Root<Logradouro> root = criteria.from(Logradouro.class);

        Predicate[] predicates = criarRestricoes(logradouroFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Logradouro> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            LogradouroFilter logradouroFilter, CriteriaBuilder builder, Root<Logradouro> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(logradouroFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Logradouro_.ID), logradouroFilter.getId()));
        }

        if (logradouroFilter.getTipoLogradouroId() != null) {
            predicates.add(builder.equal(root.get(Logradouro_.TIPO_LOGRADOURO).get(TipoLogradouro_.ID), logradouroFilter.getTipoLogradouroId()));
        }

        // NOME DO LOGRADOURO
        if (StringUtils.hasLength(logradouroFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Logradouro_.NOME)),
                            "%" + logradouroFilter.getNome().toLowerCase() + "%"));
        }

        // NOME DA CIDADE
        if (StringUtils.hasLength(logradouroFilter.getDistritoFilter().getCidadeFilter().getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Logradouro_.DISTRITO).get(Cidade_.NOME)),
                            "%" + logradouroFilter.getDistritoFilter().getCidadeFilter().getNome().toLowerCase() + "%"));
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

    private Long total(LogradouroFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Logradouro> root = criteria.from(Logradouro.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}