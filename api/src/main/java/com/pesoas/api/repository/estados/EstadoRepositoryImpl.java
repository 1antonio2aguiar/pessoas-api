package com.pesoas.api.repository.estados;

import com.pesoas.api.entity.Estado;
import com.pesoas.api.entity.Estado_;
import com.pesoas.api.filter.enderecos.EstadoFilter;
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

public class EstadoRepositoryImpl implements EstadoRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Estado> filtrar(EstadoFilter estadoFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Estado> criteria = builder.createQuery(Estado.class);
        Root<Estado> root = criteria.from(Estado.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(estadoFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Estado> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(estadoFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Estado> filtrar(EstadoFilter estadoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Estado> criteria = builder.createQuery(Estado.class);
        Root<Estado> root = criteria.from(Estado.class);

        Predicate[] predicates = criarRestricoes(estadoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Estado> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            EstadoFilter estadoFilter, CriteriaBuilder builder, Root<Estado> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(estadoFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Estado_.ID), estadoFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(estadoFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Estado_.NOME)),
                            "%" + estadoFilter.getNome().toLowerCase() + "%"));
        }

        // NOME
        if (StringUtils.hasLength(estadoFilter.getUf())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Estado_.UF)),
                            "%" + estadoFilter.getUf().toLowerCase() + "%"));
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

    private Long total(EstadoFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Estado> root = criteria.from(Estado.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}