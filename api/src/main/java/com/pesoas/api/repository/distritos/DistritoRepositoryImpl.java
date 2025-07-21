package com.pesoas.api.repository.distritos;

import com.pesoas.api.entity.Cidade_;
import com.pesoas.api.entity.Distrito;
import com.pesoas.api.entity.Distrito_;
import com.pesoas.api.filter.enderecos.DistritoFilter;
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

public class DistritoRepositoryImpl implements DistritoRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Distrito> filtrar(DistritoFilter distritoFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Distrito> criteria = builder.createQuery(Distrito.class);
        Root<Distrito> root = criteria.from(Distrito.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(distritoFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Distrito> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(distritoFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Distrito> filtrar(DistritoFilter distritoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Distrito> criteria = builder.createQuery(Distrito.class);
        Root<Distrito> root = criteria.from(Distrito.class);

        Predicate[] predicates = criarRestricoes(distritoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Distrito> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            DistritoFilter distritoFilter, CriteriaBuilder builder, Root<Distrito> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(distritoFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Distrito_.ID), distritoFilter.getId()));
        }

        // NOME DO DISTRITO
        if (StringUtils.hasLength(distritoFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Distrito_.NOME)),
                            "%" + distritoFilter.getNome().toLowerCase() + "%"));
        }

        // NOME DA CIDADE
        if (StringUtils.hasLength(distritoFilter.getCidadeFilter().getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Distrito_.CIDADE).get(Cidade_.NOME)),
                            "%" + distritoFilter.getCidadeFilter().getNome().toLowerCase() + "%"));
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

    private Long total(DistritoFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Distrito> root = criteria.from(Distrito.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}